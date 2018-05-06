package model;

import junit.framework.TestCase;

public class DiceTest extends TestCase {

    private Dice dice1G = new Dice(1, Dice.colors.YELLOW);
    private Dice dice2B = new Dice(2, Dice.colors.BLUE);
    private Dice dice3Ve = new Dice(3, Dice.colors.GREEN);
    private Dice dice4Vi = new Dice(4, Dice.colors.VIOLET);
    private Dice dice5R = new Dice(5, Dice.colors.RED);

    public DiceTest(String testName) {
        super(testName);
    }

    public void testGetID() {
        assertEquals(1, dice1G.getID());
    }

    public void testGetColor() {                // testing all 5 colors
        assertEquals(Dice.colors.YELLOW, dice1G.getColor());
        assertEquals(Dice.colors.BLUE, dice2B.getColor());
        assertEquals(Dice.colors.GREEN, dice3Ve.getColor());
        assertEquals(Dice.colors.VIOLET, dice4Vi.getColor());
        assertEquals(Dice.colors.RED, dice5R.getColor());
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
