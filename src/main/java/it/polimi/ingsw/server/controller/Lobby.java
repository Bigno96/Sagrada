package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class Lobby {

    private HashMap<String, Player> players;
    private HashMap<Integer, ClientSpeaker> speakers;
    private Game game;
    private int nPlayer;
    private int id;
    private boolean gameStarted;
    private Timer lobbyTimer;
    private Timer disconnectionTimer;

    public Lobby() {
        game = new Game();
        players = new HashMap<>();
        speakers = new HashMap<>();
        nPlayer = 0;
        id = 0;
        gameStarted = false;
    }

    public synchronized ClientSpeaker getSpeaker(Player p) {
        return speakers.get(p.getId());
    }

    public synchronized void addPlayerLobby(String username, ClientSpeaker speaker) throws GameAlreadyStartedException, SamePlayerException {
        if (gameStarted)
            throw new GameAlreadyStartedException();

        if (players.containsKey(username) || nPlayer > 4)
            throw new SamePlayerException();

        Player p = new Player(id);
        players.put(username, p);
        speakers.put(id, speaker);
        nPlayer++;
        id++;

        if (nPlayer >= 2)
            startLobbyTimer();
    }

    private synchronized void rmPlayerLobby(String username) throws PlayerNotFoundException {
        if (!players.containsKey(username))
            throw new PlayerNotFoundException();

        Player p = players.get(username);
        speakers.remove(p.getId());
        players.remove(username);
        nPlayer--;
    }

    public void disconnection(String username) {
        disconnectionTimer = new Timer(username);
        disconnectionTimer.schedule(new DisconnectUser(username), 120000);
    }

    private void startLobbyTimer() {
        lobbyTimer = new Timer();
        lobbyTimer.schedule(new StartGame(), 180000);
    }

    private class StartGame extends TimerTask {
        @Override
        public synchronized void run()  {
            Enumeration userList = (Enumeration) players.keySet();
            try {
                    while (userList.hasMoreElements()) {
                        String user = (String) userList.nextElement();
                        game.addPlayer(players.get(user));
                    }

            } catch (SamePlayerException e) {
                emptyLobby();
                out.println(e.getMessage());
            }

            gameStarted = true;
            game.startGame();
        }

        private synchronized void emptyLobby() {
            players.clear();
            speakers.clear();
            nPlayer = 0;
            id = 0;
            game = new Game();
        }
    }

    private class DisconnectUser extends TimerTask {
        String username;

        private DisconnectUser(String username) {
            this.username = username;
        }

        @Override
        public synchronized void run() {
            Player p = players.get(username);
            speakers.remove(p.getId());
            players.remove(username);

            if (gameStarted)
                try {
                    game.rmPlayer(p);
                } catch (EmptyException e) {
                    out.println(e.getMessage());
                }
            else
                nPlayer--;

        }
    }

}
