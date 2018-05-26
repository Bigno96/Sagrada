package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.*;

import static java.lang.System.*;

public class Lobby {

    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private Game game;
    private int nPlayer;
    private boolean gameStarted;
    private boolean timerGameStarted;
    private Timer preGameTimer;
    private HashMap<String, Timer> disconnectedPlayer;

    public Lobby() {
        game = new Game();
        players = new HashMap<>();
        speakers = new HashMap<>();
        gameStarted = false;
        timerGameStarted = false;
        disconnectedPlayer = new HashMap<>();
        preGameTimer = new Timer();
        startGameDaemon();
    }

    /**
     * Get speaker for player passed as parameter
     * @param p game.contains(p)
     * @return ClientSpeaker for player p
     */
    public synchronized ClientSpeaker getSpeaker(Player p) {
        return speakers.get(p.getId());
    }

    /**
     * Add player to the Lobby, first connection or reconnection
     * @param username != null
     * @param speaker instanceof ClientSpeaker
     * @throws GameAlreadyStartedException when trying to enter after game has started
     * @throws TooManyPlayersException when adding a player on full lobby, 4 player
     */
    public synchronized void addPlayerLobby(String username, ClientSpeaker speaker) throws GameAlreadyStartedException, TooManyPlayersException {
        if (disconnectedPlayer.get(username) != null)       // if player has been disconnected
            reconnectPlayer(username);
        else {
            if (gameStarted)
                throw new GameAlreadyStartedException();

            if (nPlayer > 4)
                throw new TooManyPlayersException();

            Player p = new Player(username);
            players.put(username, p);
            speakers.put(username, speaker);
            nPlayer++;

            speaker.tell("Welcome " + username);
            speaker.tell("Game will start when enough player are connected");
        }
    }

    synchronized void setGameStarted() {
        gameStarted = true;
    }

    synchronized Boolean isGameStarted() {
        return this.gameStarted;
    }

    synchronized void reduceNPlayer() {
        nPlayer--;
    }

    private void startGameDaemon() {
        Timer daemonTimer = new Timer();
        daemonTimer.scheduleAtFixedRate(new CheckStartGameDaemon(players, disconnectedPlayer), 0, 100);
        out.println("chiamo pre game timer");
        startPreGameTimer();
    }

    /**
     * Empty the lobby when errors occur while starting game
     */
    synchronized void emptyLobby() {
        players.clear();
        speakers.clear();
        nPlayer = 0;
        game = new Game();
        disconnectedPlayer.clear();
    }

    /**
     * Start timer for Game preparation, 3 minutes
     */
    private void startPreGameTimer() {
        out.println("entro in pre game timer");
        for (Map.Entry<String,ClientSpeaker> entry : speakers.entrySet()) {   // for every player in the lobby
            entry.getValue().tell("Game timer is on: 3 minutes before game starts");
        }
        preGameTimer.schedule(new StartGame(players, speakers, disconnectedPlayer, game, this), 60000);
    }

    /**
     * Disconnection of a player. Wait for 2 minutes before quitting him from Lobby
     * @param username lobby.contains(username)
     */
    public synchronized void disconnection(String username) {
        Timer disconnectionTimer = new Timer();
        disconnectedPlayer.put(username, disconnectionTimer);
        disconnectionTimer.schedule(new DisconnectUser(username, players, speakers, disconnectedPlayer, game, this), 120000);
    }

    /**
     * Reconnect to the lobby a previously connected player after his disconnection
     * @param username lobby.contains(username)
     */
    private synchronized void reconnectPlayer(String username) {
        disconnectedPlayer.remove(username);
        disconnectedPlayer.get(username).purge();
    }


}
