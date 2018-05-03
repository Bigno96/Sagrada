package it.polimi.model;

import junit.framework.TestCase;

public class DiceBagTest extends TestCase {

    private DiceBag db1 = DiceBag.getInstance();
    private DiceBag db2 = DiceBag.getInstance();

    public DiceBagTest( String testName ) {
        super( testName );
    }

    public void testGetInstance() {         // testing uniqueness of singleton DiceBag
        assertSame(db1, db2);
    }

    public void testFindDice() {            // testing finding a Dice based on his id
        Dice found = db1.findDice(10);
        assertEquals(10, found.getID());
    }

    public void testDiceRemaining() {       // test about removing and adding Dices, checking also Dices remaining in the bag
        Dice d = db1.findDice(14);
        assertEquals(90, db1.diceRemaining());
        assertTrue(db1.rmDice(d));
        assertEquals(89, db1.diceRemaining());
        assertSame(null, db1.findDice(d.getID()));
        assertTrue(db1.addDice(d));
        assertEquals(90, db1.diceRemaining());
        assertSame(d, db1.findDice(d.getID()));
    }

    public void testRandDice() {            // testing the extraction of a random Dice, without removing it from Bag
        int length = db1.diceRemaining();
        Dice d = db1.randDice();
        assertEquals(0, db1.randDice().getValue());
        d.rollDice();
        assertFalse(d == db1.randDice());
        assertEquals(length, db1.diceRemaining());
    }

    public void testToString() {
        assertEquals("it.polimi.model.DiceBag@ " + db1.hashCode(), db1.toString());
    }

    public void testDump() {
        db1.dump();
    }
}
