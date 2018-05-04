package it.polimi.model;

import junit.framework.TestCase;

import java.util.Iterator;

public class DraftTest extends TestCase {

    private DiceBag db = DiceBag.getInstance();
    private int nDice = 9;
    private Draft draft = new Draft(db, nDice);

    public DraftTest(String testName) {
        super(testName);
    }

    public void testFillDraft() {           // testing fillDraft with size of diceBag before and after and with size of draft
        assertEquals(90, db.diceRemaining());
        assertTrue(draft.fillDraft());
        assertEquals(90-nDice, db.diceRemaining());
        assertEquals(nDice, draft.diceRemaining());
    }

    public void testRollDraft() {           // testing rolling dices of draft
        assertTrue(draft.fillDraft());
        draft.rollDraft();
        for (Iterator<Dice> itr = draft.itrDraft(); itr.hasNext();) {
            Dice d = itr.next();
            assertTrue(0 < d.getValue() && 7 > d.getValue());
        }
    }

    public void testModifyingDraft() {          // testing adding and removing
        int length = draft.diceRemaining();
        Dice d = new Dice(91, Dice.color.GREEN);

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
