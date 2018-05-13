package model.dicebag;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import junit.framework.TestCase;
import model.Colors;

import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DraftTest extends TestCase {

    private static final Random random = new Random();
    private int nDice = random.nextInt(9)+2;
    private int id = random.nextInt(90);
    private Colors col = Colors.random();

    public DraftTest(String testName) {
        super(testName);
    }

    public void testFillDraft() throws IDNotFoundException, EmptyException {           // testing fillDraft with size of diceBag before and after and with size of draft
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);

        assertNotSame(db.toString(), draft.toString());
        assertEquals(90, db.diceRemaining());

        assertTrue(draft.fillDraft());

        assertEquals(90-nDice, db.diceRemaining());
        assertEquals(nDice, draft.diceRemaining());

        for (Dice d : draft.copyDraft()) {
            assertNull(db.findDice(d.getID()));
        }
    }

    public void testEmptyException() throws IDNotFoundException, SameDiceException {
        DiceBag db = new DiceBag(true);
        Draft draft = new Draft(db, nDice);
        Dice d = new Dice(id, col);

        assertThrows(EmptyException.class, draft::fillDraft);
        assertThrows(EmptyException.class, () -> draft.rmDice(d));
        assertNull(draft.findDice(id));

        db.addDice(d);
        assertThrows(EmptyException.class, draft::fillDraft);
    }

    public void testRollDraft() throws IDNotFoundException, EmptyException {           // testing rolling dices of draft
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);

        assertTrue(draft.fillDraft());

        draft.rollDraft();
        for (Iterator<Dice> itr = draft.itrDraft(); itr.hasNext();) {
            Dice d = itr.next();

            assertTrue(0 < d.getValue() && 7 > d.getValue());
        }
    }

    public void testModifyingDraft() throws IDNotFoundException, SameDiceException, EmptyException {          // testing adding and removing
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);
        int length = draft.diceRemaining();
        int wrongId;
        Dice d = new Dice(id, col);

        do {
            wrongId = random.nextInt(90);
        } while (id == wrongId);

        Dice dWrong = new Dice(wrongId, col);

        assertTrue(draft.addDice(d));
        assertThrows(SameDiceException.class, () -> draft.addDice(d));
        assertThrows(IDNotFoundException.class, () -> draft.rmDice(dWrong));

        assertEquals(length+1, draft.diceRemaining());
        assertSame(d.getID(), draft.findDice(d.getID()).getID());

        assertTrue(draft.rmDice(d));

        assertNull(draft.findDice(d.getID()));
    }

    public void testSetnDice() throws IDNotFoundException {
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);
        int n = random.nextInt(9);

        assertSame(nDice, draft.getnDice());
        draft.setnDice(n);
        assertSame(n, draft.getnDice());
    }

}
