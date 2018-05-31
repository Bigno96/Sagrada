package it.polimi.ingsw.server.controller.lobby;

import java.util.*;

public class StartGame extends TimerTask {

    private Lobby lobby;
    private int count;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
        count = 0;
    }

    @Override
    public synchronized void run() {
        if (count < 9) {
            lobby.notifyAllPlayers("Timer is on: " + (180-count*20) + " seconds before game starts");
            count++;
        } else {
            this.cancel();
            lobby.startGame();
        }
    }
}

