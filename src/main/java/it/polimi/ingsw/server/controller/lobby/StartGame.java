package it.polimi.ingsw.server.controller.lobby;

import java.util.*;

public class StartGame extends TimerTask {

    private Lobby lobby;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        lobby.startGame();
    }
}

