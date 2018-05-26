package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Player;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CheckStartGameDaemon extends TimerTask {

    private HashMap<String, Player> players;
    private HashMap<String, Timer> disconnectedPlayer;

    CheckStartGameDaemon(HashMap<String, Player> players, HashMap<String, Timer> disconnectedPlayer) {
        this.players = players;
        this.disconnectedPlayer = disconnectedPlayer;
    }

    @Override
    public void run() {
        if (checkStartGame()) {
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
            if (!disconnectedPlayer.containsKey(s))
                nConnected++;

        return nConnected >= 2;
    }
}
