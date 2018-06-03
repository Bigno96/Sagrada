package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.server.model.game.Game;

import java.util.TimerTask;

/**
 * Daemon run periodically to check if less than two player are in the game. Disconnection is not considered like
 * intentionally leaving
 */
public class CheckEndGameDaemon extends TimerTask {

    private Game game;
    private Lobby lobby;

    CheckEndGameDaemon(Game game, Lobby lobby) {
        this.game = game;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        if (game.getNPlayer()<2)
            lobby.endGame();
    }
}
