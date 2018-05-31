package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.server.model.game.Game;

import java.util.TimerTask;

public class CheckEndGameDaemon extends TimerTask {

    private Game game;
    private Lobby lobby;

    CheckEndGameDaemon(Game game, Lobby lobby) {
        this.game = game;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        if (checkEndGame())
            lobby.endGame();
    }

    private synchronized boolean checkEndGame() {
        return game.getNPlayer()<2;
    }
}
