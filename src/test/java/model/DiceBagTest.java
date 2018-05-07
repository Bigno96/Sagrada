package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.util.Random;

public class DiceBagTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(90);

    public DiceBagTest(String testName) {
        super(testName);
    }

    public void testFindDice() throws IDNotFoundException {            // testing finding a Dice based on his id
        DiceBag db1 = new DiceBag();
        int idOver = random.nextInt(1)+90;

        assertEquals(id, db1.findDice(id).getID());
        assertNull(db1.findDice(idOver));
    }

    public void testRandDice() throws IDNotFoundException {            // testing the extraction of a random Dice, without removing it from Bag
        DiceBag db1 = new DiceBag();
        Dice d = db1.randDice();

        assertEquals(0, db1.randDice().getValue());

        d.rollDice();

        assertNotSame(d, db1.randDice());
        assertEquals(90, db1.diceRemaining());
    }

    public void testDiceRemaining() throws IDNotFoundException {
        DiceBag db1 = new DiceBag();
        int length = db1.diceRemaining();

        for (int i=0; i<90; i++) {
            db1.rmDice(db1.findDice(i));

            assertEquals(89-i, db1.diceRemaining());
        }
    }

    public void testDiceAdding() throws IDNotFoundException {          // testing addDice
        DiceBag db1 = new DiceBag();
        Dice d = db1.findDice(random.nextInt(90));

        assertTrue(db1.rmDice(d));
        assertFalse(db1.rmDice(d));
        assertTrue(db1.addDice(d));
        assertFalse(db1.addDice(d));
    }

    public void testDiceRemoving() throws IDNotFoundException {       // test removeDice
        DiceBag db1 = new DiceBag();
        assertEquals(90, db1.diceRemaining());
        Dice d = db1.findDice(random.nextInt(90));

        for (int i = 0; i < 90; i++) {
            Dice itr = db1.findDice(i);

            assertTrue(db1.rmDice(itr));

        }
        assertEquals(0, db1.diceRemaining());
        assertFalse(db1.rmDice(d));
        assertNull(db1.randDice());
    }

}
