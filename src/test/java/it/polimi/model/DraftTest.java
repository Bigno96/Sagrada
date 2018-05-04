package it.polimi.model;

import exception.EmptyException;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.logging.Logger;

public class DraftTest extends TestCase {

    private DiceBag db = DiceBag.getInstance();
    private int nDice = 9;
    private Draft draft = new Draft(db, nDice);
    private static final Logger logger = Logger.getLogger(DraftTest.class.getName());

    public DraftTest(String testName) {
        super(testName);
    }

    public void testFillDraft() {           // testing fillDraft with size of diceBag before and after and with size of draft
        assertEquals(90, db.diceRemaining());
        try {
            assertTrue(draft.fillDraft());
        } catch (EmptyException e) {
            logger.info(e.getMessage());
        }
        assertEquals(90-nDice, db.diceRemaining());
        assertEquals(nDice, draft.diceRemaining());
    }

    public void testRollDraft() {           // testing rolling dices of draft
        try {
            assertTrue(draft.fillDraft());
        } catch (EmptyException e) {
            logger.info(e.getMessage());
        }
        draft.rollDraft();
        for (Iterator<Dice> itr = draft.itrDraft(); itr.hasNext();) {
            Dice d = itr.next();
            assertTrue(0 < d.getValue() && 7 > d.getValue());
        }
    }

    public void testModifyingDraft() {          // testing adding and removing
        int length = draft.diceRemaining();
        Dice d = new Dice(91, Dice.colors.GREEN);

        assertTrue(draft.addDice(d));
        assertFalse(draft.addDice(d));
        assertEquals(length+1, draft.diceRemaining());
        assertSame(d, draft.findDice(d.getID()));
        assertTrue(draft.rmDice(d));
        assertFalse(draft.rmDice(d));
    }

    public void testSetnDice() {
        assertSame(9, draft.getnDice());
        draft.setnDice(7);
        assertSame(7, draft.getnDice());
    }

}
