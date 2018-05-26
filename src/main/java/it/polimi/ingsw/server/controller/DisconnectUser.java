package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class DisconnectUser extends TimerTask {

    private String username;
    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private HashMap<String, Timer> disconnectedPlayer;
    private Game game;
    private Lobby lobby;

    DisconnectUser(String username, HashMap<String, Player> players, HashMap<String, ClientSpeaker> speakers,
                           HashMap<String, Timer> disconnectedPlayer, Game game, Lobby lobby) {
        this.username = username;
        this.players = players;
        this.disconnectedPlayer = disconnectedPlayer;
        this.speakers = speakers;
        this.game = game;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        try {
            rmPlayerLobby(username);        // remove player

            if (players.size() == 1) {    // if 1 or none player are left in the game, close it
                closeGame();
                for (Map.Entry<String, ClientSpeaker> entry : speakers.entrySet())
                    entry.getValue().tell("Insufficient player remained to continue the game\n\nCongratulations! You won");
            } else if (players.size() == 0)
                closeGame();

        } catch (PlayerNotFoundException | EmptyException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Remove dead player from lobby and from game, if started
     * @param username lobby.contains(username)
     * @throws PlayerNotFoundException when username doesn't correspond to any player
     * @throws EmptyException when trying to remove from an empty game
     */
    private synchronized void rmPlayerLobby(String username) throws PlayerNotFoundException, EmptyException {
        if (!players.containsKey(username))
            throw new PlayerNotFoundException();

        Player p = players.get(username);
        if (lobby.isGameStarted())
            game.rmPlayer(p);
        speakers.remove(p.getId());
        players.remove(username);
        lobby.reduceNPlayer();                           // 1 player less in the lobby
        disconnectedPlayer.remove(username);    // removed, not disconnected anymore
    }

    /**
     * Remove all player from game
     * @throws EmptyException when trying to remove from an empty game
     */
    private synchronized void closeGame() throws EmptyException {
        for (Map.Entry<String,Player> entry : players.entrySet())
            game.rmPlayer(entry.getValue());
    }
}
