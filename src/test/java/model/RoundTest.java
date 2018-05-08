package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.util.logging.Logger;

public class RoundTest extends TestCase {

    private Board board;
    private static final Logger logger = Logger.getLogger(DiceBag.class.getName());

    {
        try {
            board = new Board(4);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    public RoundTest(String testName) {
        super(testName);
    }

    public void testNextTurn(){
        Round round = new Round();
        Player player1 = new Player(1, board);
        Player player2 = new Player(2, board);

        round.addPlayer(player1);
        round.addPlayer(player2);

        assertTrue(player1.isFirstTurn());
        assertEquals(player1, round.nextPlayer());
        assertFalse(player1.isFirstTurn());
        assertEquals(player2, round.nextPlayer());
        assertEquals(player2, round.nextPlayer());
        assertEquals(player1, round.nextPlayer());
    }


    public void testNextRound() throws EmptyException {
        Round round = new Round();
        Player player1 = new Player(1, board);
        Player player2 = new Player(2, board);

        round.addPlayer(player1);
        round.addPlayer(player2);

        round.nextRound();

        assertTrue(player2.isFirstTurn());
        assertEquals(player2, round.nextPlayer());
        assertFalse(player2.isFirstTurn());
        assertTrue(player1.isFirstTurn());
        assertEquals(player1, round.nextPlayer());
        assertEquals(player1, round.nextPlayer());
        assertEquals(player2, round.nextPlayer());

    }
}