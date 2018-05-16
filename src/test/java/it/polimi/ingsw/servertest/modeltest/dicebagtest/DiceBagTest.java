package it.polimi.ingsw.servertest.modeltest.dicebagtest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiceBagTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(90);

    public DiceBagTest(String testName) {
        super(testName);
    }

    public void testDiceBagEmpty() throws IDNotFoundException {
        DiceBag db = new DiceBag(true);
        DiceBag db1 = new DiceBag();

        assertNull(db.findDice(id));
        assertNotSame(db.toString(), db1.toString());
    }

    public void testFindDice() throws IDNotFoundException {            // testing finding a Dice based on his id
        DiceBag db1 = new DiceBag();
        int idOver = random.nextInt()+90;

        assertEquals(id, db1.findDice(id).getID());
        assertNull(db1.findDice(idOver));
    }

    public void testRandDice() throws IDNotFoundException {            // testing the extraction of a random Dice, without removing it from Bag
        DiceBag db1 = new DiceBag();
        Dice d = db1.randDice();

        assertEquals(0, db1.randDice().getValue());

        db1.findDice(d.getID()).rollDice();

        assertNotSame(d.getID(), db1.randDice().getID());
        assertEquals(90, db1.diceRemaining());
    }

    public void testDiceRemaining() throws IDNotFoundException, EmptyException {
        DiceBag db1 = new DiceBag();

        for (int i=0; i<90; i++) {
            db1.rmDice(db1.findDice(i));

            assertEquals(89-i, db1.diceRemaining());
        }
    }

    public void testDiceAdding() throws IDNotFoundException, EmptyException, SameDiceException, ValueException {          // testing addDice
        DiceBag db1 = new DiceBag();
        Dice d = db1.findDice(random.nextInt(90));

        db1.rmDice(d);

        assertNull(db1.findDice(d.getID()));
        assertThrows(IDNotFoundException.class, () -> db1.rmDice(d));

        db1.addDice(d);

        assertSame(d.getID(), db1.findDice(d.getID()).getID());
        assertThrows(SameDiceException.class, () -> db1.addDice(d));
    }

    public void testDiceRemoving() throws IDNotFoundException, EmptyException {       // test removeDice
        DiceBag db1 = new DiceBag();

        assertEquals(90, db1.diceRemaining());

        Dice d = db1.findDice(random.nextInt(90));

        for (int i = 0; i < 90; i++) {
            Dice itr = db1.findDice(i);
            db1.rmDice(itr);

            assertNull(db1.findDice(itr.getID()));
        }

        assertEquals(0, db1.diceRemaining());
        assertThrows(EmptyException.class, () -> db1.rmDice(d));
        assertNull(db1.randDice());
    }

}
