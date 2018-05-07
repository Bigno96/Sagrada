package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.util.logging.Logger;

public class RoundTest extends TestCase {

    private Round round = new Round();
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
        Player player1 = new Player(1, board);
        Player player2 = new Player(2, board);

        round.addPlayer(player1);
        round.addPlayer(player2);

        assertTrue(player1.isFirstTurn());
        assertEquals(player1, round.nextTurn());
        assertFalse(player1.isFirstTurn());
        assertEquals(player2, round.nextTurn());
        assertEquals(player2, round.nextTurn());
        assertEquals(player1, round.nextTurn());
    }


    public void testNextRound(){
        round.nextRound();

         Player player1 = new Player(1, board);
         Player player2 = new Player(2, board);

        round.addPlayer(player1);
        round.addPlayer(player2);

        assertTrue(player2.isFirstTurn());
        assertEquals(player2, round.nextTurn());
        assertFalse(player1.isFirstTurn());
        assertEquals(player1, round.nextTurn());
        assertEquals(player1, round.nextTurn());
        assertEquals(player2, round.nextTurn());

    }
}