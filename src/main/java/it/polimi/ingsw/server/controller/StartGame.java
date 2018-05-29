package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.*;

import static java.lang.System.*;

public class StartGame extends TimerTask {

    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private List<String> disconnectedPlayer;
    private Game game;
    private Lobby lobby;

    StartGame(HashMap<String, Player> players, HashMap<String, ClientSpeaker> speakers, List<String> disconnectedPlayer, Game game, Lobby lobby) {
        this.players = players;
        this.disconnectedPlayer = disconnectedPlayer;
        this.speakers = speakers;
        this.game = game;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        try {
            for (Map.Entry<String,Player> entry : players.entrySet()) {   // for every player in the lobby
                if (!disconnectedPlayer.contains(entry.getKey())) {
                    game.addPlayer(entry.getValue());                           // add to the game
                    speakers.get(entry.getValue().getId()).tell("Game is starting");
                }
            }
        } catch (SamePlayerException e) {
            out.println(e.getMessage());
            lobby.emptyLobby();
            this.cancel();
        }

        lobby.setGameStarted();
        game.startGame();                               // start game

        this.cancel();
    }
}

