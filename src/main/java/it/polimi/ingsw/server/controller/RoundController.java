package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;

/**
 * Checks which player has to play turn
 */
public class RoundController {

    private Game game;
    private Player currentPlayer;

    public RoundController(Game game) {
        this.game = game;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void nextTurn() {
        currentPlayer = game.nextPlayer();
    }

}
