package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Player;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class CheckStartGameDaemon extends TimerTask {

    private HashMap<String, Player> players;
    private List<String> disconnectedPlayer;
    private Lobby lobby;

    CheckStartGameDaemon(HashMap<String, Player> players, List<String> disconnectedPlayer, Lobby lobby) {
        this.players = players;
        this.disconnectedPlayer = disconnectedPlayer;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        if (checkStartGame()) {
            lobby.startPreGameTimer();
            this.cancel();
        }
    }

    /**
     * Check if game is ready to be create
     * @return true if at least 2 player are connected, false else
     */
    private synchronized boolean checkStartGame() {
        int nConnected = 0;

        for(String s : players.keySet())
            if (!disconnectedPlayer.contains(s))
                nConnected++;

        return nConnected >= 2;
    }
}

