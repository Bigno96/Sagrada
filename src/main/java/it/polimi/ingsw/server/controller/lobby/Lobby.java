package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class Lobby {
    enum gameState { WAITING, STARTING, STARTED }
    private gameState currentState;

    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private HashMap<String, CheckDisconnectionDaemon> checkerDisconnection;
    private HashMap<String, RemovePlayerDaemon> checkerRemoving;
    private Game game;

    public Lobby() {
        this.players = new HashMap<>();
        this.speakers = new HashMap<>();
        this.checkerDisconnection = new HashMap<>();
        this.checkerRemoving = new HashMap<>();
        this.game = new Game();
    }

    public void startLobby() {
        currentState = gameState.WAITING;
        Timer starting = new Timer();
        starting.scheduleAtFixedRate(new CheckStartGameDaemon(players, this), 0, 1000);
    }

    public synchronized void addPlayer(String username, ClientSpeaker speaker) throws SamePlayerException, GameAlreadyStartedException, TooManyPlayersException {
        if (players.containsKey(username)) {
            System.out.println(players.get(username).isDisconnected());
            if (players.get(username).isDisconnected())
                reconnectPlayer(username);
            else
                throw new SamePlayerException();
        }

        else if (currentState.equals(gameState.STARTED))
            throw new GameAlreadyStartedException();

        else if (players.size() >= 4)
            throw new TooManyPlayersException();

        players.put(username, new Player(username));
        speakers.put(username, speaker);

        Timer disconnection = new Timer();
        CheckDisconnectionDaemon daemon = new CheckDisconnectionDaemon(speaker, this, username);
        disconnection.scheduleAtFixedRate(daemon, 0, 1000);
        checkerDisconnection.put(username, daemon);

        speaker.loginSuccess("Welcome " + username);
        speaker.tell("Game will start when enough players are connected");
    }

    public synchronized void disconnectPlayer(String username) {
        if(currentState.equals(gameState.WAITING)) {
            Timer removing = new Timer();
            RemovePlayerDaemon daemon = new RemovePlayerDaemon(username);
            removing.schedule(daemon, 120000);
            checkerRemoving.put(username, daemon);

        } else
            players.get(username).setDisconnected(true);
    }

    public synchronized void reconnectPlayer(String username) {
        players.get(username).setDisconnected(false);
        speakers.get(username).tell("Welcome back " + username);
    }

    public synchronized void purgeRemoving(String username) {
        if (checkerRemoving.containsKey(username))
            checkerRemoving.get(username).cancel();
    }

    public synchronized void removePlayer(String username) {
        if (!currentState.equals(gameState.WAITING)) {
            try {
                game.rmPlayer(players.get(username));
            } catch (EmptyException e) {
                out.println(e.getMessage());
            }
        }

        checkerDisconnection.get(username).cancel();
        checkerDisconnection.remove(username);
        speakers.remove(username);
        players.remove(username);

        out.println("Removed " + username);
    }

    public class RemovePlayerDaemon extends TimerTask {

        private String username;

        private RemovePlayerDaemon(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            removePlayer(username);
        }
    }

    synchronized void startingGame() {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            try {
                game.addPlayer(entry.getValue());
            } catch (SamePlayerException e) {
                out.println(e.getMessage());
            }
            speakers.get(entry.getKey()).tell("Game timer is on");
        }
        currentState = gameState.STARTING;

        Timer start = new Timer();
        start.schedule(new StartGame(this), 180000);
    }

    synchronized void startGame() {
        for (Map.Entry<String,Player> entry : players.entrySet()) {   // for every player in the lobby
            speakers.get(entry.getKey()).tell("Game is starting");
        }

        currentState = gameState.STARTED;
        game.startGame();
    }
}



