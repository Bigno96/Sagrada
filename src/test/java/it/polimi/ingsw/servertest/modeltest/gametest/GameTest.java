package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.game.Round;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest extends TestCase {

    private String id1 = "Giovanni";
    private String id2 = "Federico";
    private String id3 = "Andrea";


    public GameTest(String testName) {
        super(testName);
    }

    public void testGetter() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);
        Player p2 = new Player(id3);
        List<Player> list = new ArrayList<>();
        list.add(p);
        list.add(p1);
        list.add(p2);
        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        int nRound = game.getNRound();
        int nPlayer = game.getNPlayer();
        Board board = game.getBoard();
        Round round = game.getRound();

        assertEquals(nRound, game.getNRound());
        assertEquals(nPlayer, game.getNPlayer());
        assertEquals(board, game.getBoard());
        assertEquals(round, game.getRound());
        assertEquals(list, game.getPlayerList());

    }

    public void testStartGame() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);
        Player p2 = new Player(id3);
        List<ObjectiveCard> list;
        List<ToolCard> listTool;

        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        assertNotNull(game.getRound());
        assertNotNull(game.getBoard());
        list = game.getBoard().getPublObj();
        assertEquals(list, game.getBoard().getPublObj());
        listTool = game.getBoard().getToolCard();
        assertEquals(listTool, game.getBoard().getToolCard());

    }

    public void testAddPlayer() throws SamePlayerException, EmptyException, PlayerNotFoundException {
        Game game = new Game();
        Player p = new Player(id1);

        assertTrue(game.addPlayer(p));
        assertTrue(game.findPlayer(p));
    }

    public void testSamePlayerException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);

        assertTrue(game.addPlayer(p));
        assertThrows(SamePlayerException.class, () -> game.addPlayer(p));
    }

    public void testPlayerNotFoundException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);
        Player pDiff = new Player(id2);

        assertTrue(game.addPlayer(p));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(pDiff));
    }

    public void testCurrentPlayer() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);
        game.addPlayer(p);
        game.addPlayer(p1);

        game.startGame();

        assertEquals(p, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());
        assertEquals(p, game.currentPlayer());

        game.getRound().nextRound();

        assertEquals(p1, game.currentPlayer());
        assertEquals(p, game.currentPlayer());
        assertEquals(p, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());

        game.setnRound(9);
        assertNull(game.currentPlayer());
    }

    public void testRmPlayer() throws SamePlayerException, EmptyException {
        Game game = new Game();
        Player p = new Player(id1);
        Player pDiff = new Player(id2);

        assertThrows(EmptyException.class, () -> game.rmPlayer(p));
        assertThrows(EmptyException.class, () -> game.findPlayer(p));
        assertTrue(game.addPlayer(p));
        assertTrue(game.rmPlayer(p));
        assertTrue(game.addPlayer(pDiff));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(p));
    }
}