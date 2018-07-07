package it.polimi.ingsw.servertest.modeltest.dicebagtest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import junit.framework.TestCase;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiceBagTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(90);

    public DiceBagTest(String testName) {
        super(testName);
    }

    /**
     * Testing constructing an empty dice bag
     */
    public void testDiceBagEmpty() {
        DiceBag db = new DiceBag(true);

        assertNull(db.findDice(id));
    }

    /**
     * Testing finding a Dice based on his id
     * @throws IDNotFoundException thrown by dice constructor
     */
    public void testFindDice() throws IDNotFoundException {
        DiceBag db = new DiceBag();
        int idOver = random.nextInt()+90;

        assertEquals(id, db.findDice(id).getID());
        assertNull(db.findDice(idOver));
    }

    /**
     * Testing finding of a random Dice, without removing it from Bag
     * @throws IDNotFoundException thrown by dice constructor
     */
    public void testRandDice() throws IDNotFoundException {
        DiceBag db = new DiceBag();
        Dice d = db.randDice();

        assertEquals(0, db.randDice().getValue());

        db.findDice(d.getID()).rollDice();

        assertNotSame(d.getID(), db.randDice().getID());
        assertEquals(90, db.diceRemaining());
    }

    /**
     * Testing number of dices remaining in the bag
     * @throws IDNotFoundException thrown by dice constructor
     * @throws EmptyException thrown by remove dice
     */
    public void testDiceRemaining() throws IDNotFoundException, EmptyException {
        DiceBag db = new DiceBag();

        for (int i=0; i<90; i++) {
            db.rmDice(db.findDice(i));

            assertEquals(89-i, db.diceRemaining());
        }
    }

    /**
     * Testing adding dices in the bag
     * @throws IDNotFoundException thrown by dice constructor
     * @throws EmptyException thrown by remove dice
     * @throws SameDiceException when trying to add a dice with the same id already present in the bag
     * @throws ValueException thrown by adding dice
     */
    public void testDiceAdding() throws IDNotFoundException, EmptyException, SameDiceException, ValueException {
        DiceBag db = new DiceBag();
        Dice d = db.findDice(random.nextInt(90));

        db.rmDice(d);

        assertNull(db.findDice(d.getID()));
        assertEquals(89, db.diceRemaining());
        assertThrows(IDNotFoundException.class, () -> db.rmDice(d));

        db.addDice(d);

        assertSame(d.getID(), db.findDice(d.getID()).getID());
        assertEquals(90, db.diceRemaining());
        assertThrows(SameDiceException.class, () -> db.addDice(d));
    }

    /**
     * Testing removing dice from the bag
     * @throws IDNotFoundException thrown by dice constructor
     * @throws EmptyException thrown by remove dice
     */
    public void testDiceRemoving() throws IDNotFoundException, EmptyException {       // test removeDice
        DiceBag db = new DiceBag();

        assertEquals(90, db.diceRemaining());

        Dice d = db.findDice(random.nextInt(90));

        for (int i = 0; i < 90; i++) {
            Dice itr = db.findDice(i);
            db.rmDice(itr);

            assertNull(db.findDice(itr.getID()));
        }

        assertEquals(0, db.diceRemaining());
        assertThrows(EmptyException.class, () -> db.rmDice(d));
        assertNull(db.randDice());
    }

}
