package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;

import static java.lang.System.*;

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
        Player p = null;
        try {
            p = game.nextPlayer();
        } catch (SameDiceException | EmptyException | IDNotFoundException e) {
            out.println(e.getMessage());
        }

        if (p == null)      // when nextPlayer() is null, it means all rounds and all turns have been played
            lobby.endGame();
        else {
            timerTurn.resetCount();
            currentPlayer = p;
        }
    }

}
