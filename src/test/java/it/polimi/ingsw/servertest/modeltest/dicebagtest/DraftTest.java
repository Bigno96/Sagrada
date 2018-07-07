package it.polimi.ingsw.servertest.modeltest.dicebagtest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import junit.framework.TestCase;

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

    /**
     * Testing fillDraft with size of diceBag before and after and with size of draft
     * @throws IDNotFoundException when creating dices with wrong ids
     * @throws EmptyException when trying to fill draft from an empty bag
     */
    public void testFillDraft() throws IDNotFoundException, EmptyException {
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);

        assertEquals(90, db.diceRemaining());

        assertTrue(draft.fillDraft());

        assertEquals(90-nDice, db.diceRemaining());
        assertEquals(nDice, draft.diceRemaining());

        draft.getDraftList().forEach(dice -> assertNull(db.findDice(dice.getID())));
    }

    /**
     * Testing throwing empty exception
     * @throws IDNotFoundException when creating dices with wrong ids
     * @throws SameDiceException when trying to add same dice when is already in the bag
     * @throws ValueException when trying to add a dice with an invalid id
     */
    public void testEmptyException() throws IDNotFoundException, SameDiceException, ValueException {
        DiceBag db = new DiceBag(true);
        Draft draft = new Draft(db, nDice);
        Dice d = new Dice(id, col);

        assertThrows(EmptyException.class, draft::fillDraft);
        assertThrows(EmptyException.class, () -> draft.rmDice(d));
        assertNull(draft.findDice(id));

        db.addDice(d);
        assertThrows(EmptyException.class, draft::fillDraft);
    }

    /**
     * Testing rolling value of all dices in the draft
     * @throws IDNotFoundException when creating dices with wrong ids
     * @throws EmptyException when trying to fill draft from an empty bag
     */
    public void testRollDraft() throws IDNotFoundException, EmptyException {
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);

        assertTrue(draft.fillDraft());

        draft.rollDraft();
        for (Iterator<Dice> itr = draft.itrDraft(); itr.hasNext();) {
            Dice d = itr.next();

            assertTrue(0 < d.getValue() && 7 > d.getValue());
        }
    }

    /**
     * Testing adding and removing dices in the draft
     * @throws IDNotFoundException when creating dices with wrong ids
     * @throws SameDiceException when trying to add same dice when is already in the bag
     * @throws EmptyException when trying to fill draft from an empty bag
     */
    public void testModifyingDraft() throws IDNotFoundException, SameDiceException, EmptyException {
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);

        int length = draft.diceRemaining();
        int wrongId = (id+1)%90;

        Dice d = new Dice(id, col);
        Dice dWrong = new Dice(wrongId, col);

        assertTrue(draft.addDice(d));
        assertThrows(SameDiceException.class, () -> draft.addDice(d));
        assertThrows(IDNotFoundException.class, () -> draft.rmDice(dWrong));

        assertEquals(length+1, draft.diceRemaining());
        assertSame(d.getID(), draft.findDice(d.getID()).getID());

        assertTrue(draft.rmDice(d));

        assertNull(draft.findDice(d.getID()));
    }

    /**
     * Testing setter of numberDice of draft
     * @throws IDNotFoundException when creating dices with wrong ids
     */
    public void testSetNumberDice() throws IDNotFoundException {
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);
        int n = random.nextInt(9);

        assertSame(nDice, draft.getNumberDice());
        draft.setNumberDice(n);
        assertSame(n, draft.getNumberDice());
    }

}
