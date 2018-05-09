package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.PlayerNotFoundException;
import exception.SamePlayerException;
import junit.framework.TestCase;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoundTest extends TestCase {

    private static final Random random = new Random();
    private int nPlayer = random.nextInt(3)+2;
    private Board board = new Board(nPlayer);
    private int id = random.nextInt();

    public RoundTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testAddPlayer() throws SamePlayerException, EmptyException, PlayerNotFoundException {
        Round round = new Round();
        Player p = new Player(id, board);

        assertTrue(round.addPlayer(p));
        assertSame(p, round.findPlayer(p));
    }

    public void testSamePlayerException() throws SamePlayerException {
        Round round = new Round();
        Player p = new Player(id, board);

        assertTrue(round.addPlayer(p));
        assertThrows(SamePlayerException.class, () -> round.addPlayer(p));
    }

    public void testPlayerNotFoundException() throws SamePlayerException {
        Round round = new Round();
        Player p = new Player(id, board);
        Player pDiff = new Player(id+1, board);

        assertTrue(round.addPlayer(p));
        assertThrows(PlayerNotFoundException.class, () -> round.findPlayer(pDiff));
    }

    public void testRmPlayer() throws SamePlayerException, EmptyException {
        Round round = new Round();
        Player p = new Player(id, board);
        Player pDiff = new Player(id+1, board);

        assertThrows(EmptyException.class, () -> round.rmPlayer(p));
        assertTrue(round.addPlayer(p));
        assertTrue(round.rmPlayer(p));
        assertTrue(round.addPlayer(pDiff));
        assertThrows(PlayerNotFoundException.class, () -> round.findPlayer(p));
    }

    public void testNextPlayer() throws SamePlayerException {
        Round round = new Round();
        Player player1 = new Player(id, board);
        Player player2 = new Player(id+1, board);

        assertTrue(round.addPlayer(player1));
        assertTrue(round.addPlayer(player2));

        assertSame(player1, round.nextPlayer());
        assertSame(player2, round.nextPlayer());
        assertSame(player2, round.nextPlayer());
        assertSame(player1, round.nextPlayer());
        assertNull(round.nextPlayer());
    }


    public void testNextRound() throws EmptyException, SamePlayerException {
        Round round = new Round();
        Player player1 = new Player(id, board);
        Player player2 = new Player(id+1, board);

        assertTrue(round.addPlayer(player1));
        assertTrue(round.addPlayer(player2));

        assertSame(player1, round.nextPlayer());
        assertSame(player2, round.nextPlayer());
        assertSame(player2, round.nextPlayer());
        assertSame(player1, round.nextPlayer());
        assertNull(round.nextPlayer());

        round.nextRound();

        assertSame(player2, round.nextPlayer());
        assertSame(player1, round.nextPlayer());
        assertSame(player1, round.nextPlayer());
        assertSame(player2, round.nextPlayer());
        assertNull(round.nextPlayer());
    }
}