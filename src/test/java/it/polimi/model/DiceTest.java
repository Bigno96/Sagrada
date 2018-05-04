package it.polimi.model;

import junit.framework.TestCase;

public class DiceTest extends TestCase {

    private Dice dice1G = new Dice(1, Dice.color.YELLOW);
    private Dice dice2B = new Dice(2, Dice.color.BLU);
    private Dice dice3Ve = new Dice(3, Dice.color.GREEN);
    private Dice dice4Vi = new Dice(4, Dice.color.VIOLET);
    private Dice dice5R = new Dice(5, Dice.color.RED);

    public DiceTest(String testName) {
        super(testName);
    }

    public void testGetID() {
        assertEquals(1, dice1G.getID());
    }

    public void testGetColor() {                // testing all 5 colors
        assertEquals(Dice.color.YELLOW, dice1G.getColor());
        assertEquals(Dice.color.BLU, dice2B.getColor());
        assertEquals(Dice.color.GREEN, dice3Ve.getColor());
        assertEquals(Dice.color.VIOLET, dice4Vi.getColor());
        assertEquals(Dice.color.RED, dice5R.getColor());
    }

    public void testRollDice() {            // roll Dice must produce value x | 0 < x < 7
        dice1G.rollDice();
        assertTrue(0 < dice1G.getValue() && 7 > dice1G.getValue());
    }

    public void testChangeValue() {         // testing Dice's value changing
        dice2B.rollDice();
        dice2B.changeValue(3);
        assertEquals(3, dice2B.getValue());
    }

}
