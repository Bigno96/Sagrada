package model;

import junit.framework.TestCase;

public class RoundTest extends TestCase {

    private Round round = new Round();

    public RoundTest(String testName) {
        super(testName);
    }

    public void testNextTurn(){
        Player player1 = new Player(1);
        Player player2 = new Player(2);

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

         Player player1 = new Player(1);
         Player player2 = new Player(2);

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