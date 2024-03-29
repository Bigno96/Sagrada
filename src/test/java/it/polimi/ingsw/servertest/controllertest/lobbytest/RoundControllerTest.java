package it.polimi.ingsw.servertest.controllertest.lobbytest;

import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.controller.game.RoundController;
import it.polimi.ingsw.server.controller.game.TimerTurnDaemon;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import junit.framework.TestCase;

public class RoundControllerTest extends TestCase {

    private static final String USERNAME1 = "Test1";
    private static final String USERNAME2 = "Test2";

    public RoundControllerTest(String testName) {
        super(testName);
    }

    /**
     * Testing correct function of counting round
     * @throws SamePlayerException when trying to add a player with the same id
     */
    public void testCountingRound() throws SamePlayerException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        lobby.startingGame();
        Game game = lobby.getGame();
        TimerTurnDaemon timerTurn = new TimerTurnDaemon(lobby);
        RoundController roundController = new RoundController(lobby, game, timerTurn);
        game.addPlayer(new Player(USERNAME1));
        game.addPlayer(new Player(USERNAME2));

        game.startGame();

        roundController.nextTurn();
        assertSame(USERNAME1, roundController.getCurrentPlayer().getId());

        roundController.nextTurn();
        assertSame(USERNAME2, roundController.getCurrentPlayer().getId());

        roundController.nextTurn();
        assertSame(USERNAME2, roundController.getCurrentPlayer().getId());

        roundController.nextTurn();
        assertSame(USERNAME1, roundController.getCurrentPlayer().getId());
    }
}
