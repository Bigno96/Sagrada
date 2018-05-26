package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.*;

import static java.lang.System.*;

public class Lobby {

    private HashMap<String, Player> players;
    private HashMap<Integer, ClientSpeaker> speakers;
    private Game game;
    private int nPlayer;
    private int id;
    private boolean gameStarted;
    private boolean timerGameStarted;
    private Timer lobbyTimer;
    private HashMap<String, Timer> disconnectedPlayer;

    public Lobby() {
        game = new Game();
        players = new HashMap<>();
        speakers = new HashMap<>();
        nPlayer = 0;
        id = 0;
        gameStarted = false;
        timerGameStarted = false;
        disconnectedPlayer = new HashMap<>();
        lobbyTimer = new Timer();
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

            Player p = new Player(id);
            players.put(username, p);
            speakers.put(id, speaker);
            nPlayer++;
            id++;

            speaker.tell("Welcome " + username);
            speaker.tell("Game will start when enough player are connected");
        }
    }

    private void startGameDaemon() {
        Timer daemonTimer = new Timer();
        daemonTimer.scheduleAtFixedRate(new GameDaemon(), 0, 10);
    }

    public class GameDaemon extends TimerTask {

        @Override
        public void run() {

        }

        /**
         * Check if game is ready to be create
         * @return true if at least 2 player are connected, false else
         */
        private synchronized boolean checkStartGame() {
            int nConnected = 0;

            for(String s : players.keySet())
                if (!disconnectedPlayer.containsKey(s))
                    nConnected++;

            return nConnected >= 2;
        }

    }

    /**
     * Disconnection of a player. Wait for 2 minutes before quitting him from Lobby
     * @param username lobby.contains(username)
     */
    public synchronized void disconnection(String username) {
        Timer disconnectionTimer = new Timer();
        disconnectedPlayer.put(username, disconnectionTimer);
        disconnectionTimer.schedule(new DisconnectUser(username), 120000);
    }

    /**
     * Reconnect to the lobby a previously connected player after his disconnection
     * @param username lobby.contains(username)
     */
    private synchronized void reconnectPlayer(String username) {
        disconnectedPlayer.remove(username);
        disconnectedPlayer.get(username).purge();
    }

    /**
     * Start timer for Game preparation, 3 minutes
     */
    private void startLobbyTimer() {
        for (Map.Entry<Integer,ClientSpeaker> entry : speakers.entrySet()) {   // for every player in the lobby
            entry.getValue().tell("Game timer is on: 3 minutes before game starts");
        }
        lobbyTimer.schedule(new StartGame(), 180000);
    }

    /**
     * Task class for starting game
     * Executed when lobby timer expires
     */
    private class StartGame extends TimerTask {

        @Override
        public synchronized void run()  {
            try {
                for (Map.Entry<String,Player> entry : players.entrySet()) {   // for every player in the lobby
                    game.addPlayer(entry.getValue());                           // add to the game
                    speakers.get(entry.getValue().getId()).tell("Game is starting");
                }

            } catch (SamePlayerException e) {
                emptyLobby();
                out.println(e.getMessage());
            }

            gameStarted = true;
            game.startGame();                               // start game
        }

        /**
         * Empty the lobby when errors occur while starting game
         */
        private synchronized void emptyLobby() {
            players.clear();
            speakers.clear();
            nPlayer = 0;
            id = 0;
            game = new Game();
            disconnectedPlayer.clear();
        }
    }

    /**
     * Task class for disconnection
     * Execute when disconnection Timer for that player expires
     */
    private class DisconnectUser extends TimerTask {

        String username;

        private DisconnectUser(String username) {
            this.username = username;
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
            if (gameStarted)
                game.rmPlayer(p);
            speakers.remove(p.getId());
            players.remove(username);
            nPlayer--;                              // 1 player less in the lobby
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

        @Override
        public synchronized void run() {
            try {
                rmPlayerLobby(username);        // remove player
                if (players.size() < 2)         // if 1 or none player are left in the game, close it
                    closeGame();
            } catch (PlayerNotFoundException | EmptyException e) {
                out.println(e.getMessage());
            }

        }
    }

}
