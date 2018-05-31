package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.server.model.game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class CheckStartGameDaemon extends TimerTask {

    private HashMap<String, Player> players;
    private Lobby lobby;

    CheckStartGameDaemon(HashMap<String, Player> players, Lobby lobby) {
        this.players = players;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        if (checkStartGame()) {
            lobby.startingGame();
            this.cancel();
        }
    }

    /**
     * Check if game is ready to be create
     * @return true if at least 2 player are connected, false else
     */
    private synchronized boolean checkStartGame() {
        int nConnected = 0;

        for(Map.Entry<String, Player> entry : players.entrySet())
            if (!entry.getValue().isDisconnected())
                nConnected++;

        //System.out.println("nConnected = " + nConnected);
        return nConnected >= 2;
    }
}

