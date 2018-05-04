package it.polimi.model;

import junit.framework.TestCase;

public class DiceBagTest extends TestCase {

    private DiceBag db1 = DiceBag.getInstance();
    private DiceBag db2 = DiceBag.getInstance();

    public DiceBagTest(String testName) {
        super(testName);
    }

    public void testGetInstance() {         // testing uniqueness of singleton DiceBag
        assertSame(db1, db2);
    }

    public void testFindDice() {            // testing finding a Dice based on his id
        assertEquals(10, db1.findDice(10).getID());
        assertSame(null, db1.findDice(100));
    }

    public void testRandDice() {            // testing the extraction of a random Dice, without removing it from Bag
        int length = db1.diceRemaining();
        Dice d = db1.randDice();
        assertEquals(0, db1.randDice().getValue());
        d.rollDice();
        assertFalse(d == db1.randDice());
        assertEquals(length, db1.diceRemaining());
    }

    public void testDiceAdding() {          // testing addDice
        Dice d = db1.findDice(14);
        assertTrue(db1.rmDice(d));
        assertFalse(db1.rmDice(d));
        assertTrue(db1.addDice(d));
        assertFalse(db1.addDice(d));
    }

    public void testDiceRemoving() {       // test removeDice
        assertEquals(90, db1.diceRemaining());
        Dice d = db1.findDice(14);

        for (int i = 0; i < 90; i++) {
            Dice itr = db1.findDice(i);
            assertTrue(db1.rmDice(itr));

        }
        assertEquals(0, db1.diceRemaining());
        assertFalse(db1.rmDice(d));
        assertSame(null, db1.randDice());
    }

}
