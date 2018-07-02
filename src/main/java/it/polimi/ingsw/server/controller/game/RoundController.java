package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;

/**
 * Checks which player has to play turn
 */
public class RoundController {

    private Game game;
    private Lobby lobby;
    private TimerTurnDaemon timerTurn;
    private Player currentPlayer;

    public RoundController(Lobby lobby, Game game, TimerTurnDaemon timerTurn) {
        this.lobby = lobby;
        this.game = game;
        this.timerTurn = timerTurn;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void nextTurn() {
        Player p = game.nextPlayer();

        if (p == null)      // when nextPlayer() is null, it means all rounds and all turns have been played
            lobby.endGame();
        else {
            timerTurn.resetCount();
            currentPlayer = p;
        }
    }

}
