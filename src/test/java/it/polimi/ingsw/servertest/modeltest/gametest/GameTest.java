package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.game.Round;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest extends TestCase {

    private int nPlayer = 2;
    private Board board = new Board(nPlayer);
    private static final Random random = new Random();
    private int id = random.nextInt(20);

    public GameTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testGetter() throws SamePlayerException, IDNotFoundException {
        Game game = new Game();
        Player p = new Player(id, board);
        Player p1 = new Player(id+1, board);
        Player p2 = new Player(id+2, board);
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

    }

    public void testStartGame() throws SamePlayerException, IDNotFoundException {
        Game game = new Game();
        Player p = new Player(id, board);
        Player p1 = new Player(id+1, board);
        Player p2 = new Player(id+2, board);
        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        assertNotNull(game.getRound());
        assertNotNull(game.getBoard());
    }

    public void testAddPlayer() throws SamePlayerException, EmptyException, PlayerNotFoundException, IDNotFoundException {
        Game game = new Game();
        Player p = new Player(id, board);

        assertTrue(game.addPlayer(p));
        assertSame(p, game.findPlayer(p));
    }

    public void testSamePlayerException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id, board);

        assertTrue(game.addPlayer(p));
        assertThrows(SamePlayerException.class, () -> game.addPlayer(p));
    }

    public void testPlayerNotFoundException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id, board);
        Player pDiff = new Player(id+1, board);

        assertTrue(game.addPlayer(p));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(pDiff));
    }

    public void testCurrentPlayer() throws SamePlayerException, IDNotFoundException {
        Game game = new Game();
        Player p = new Player(id, board);
        Player p1 = new Player(id+1, board);
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
        Player p = new Player(id, board);
        Player pDiff = new Player(id+1, board);

        assertThrows(EmptyException.class, () -> game.rmPlayer(p));
        assertThrows(EmptyException.class, () -> game.findPlayer(p));
        assertTrue(game.addPlayer(p));
        assertTrue(game.rmPlayer(p));
        assertTrue(game.addPlayer(pDiff));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(p));
    }
}