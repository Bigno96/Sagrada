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
        draft.fillDraft();
        assertEquals(90-nDice, db.diceRemaining());
        assertEquals(nDice, draft.diceRemaining());
    }

    public void testRollDraft() {           // testing rolling dices of draft
        draft.fillDraft();
        draft.rollDraft();
        for (Iterator<Dice> itr = draft.itrDraft(); itr.hasNext();) {
            Dice d = itr.next();
            assertTrue(0 < d.getValue() && 7 > d.getValue());
        }
    }

    public void testModifyingDraft() {          // testing add e and removing Dice from draft
        draft.fillDraft();
        Iterator<Dice> itr = draft.itrDraft();
        Dice d = itr.next();
        int length = draft.diceRemaining();
        assertTrue(draft.rmDice(d));
        assertEquals(length-1, draft.diceRemaining());
        assertSame(null, draft.findDice(d.getID()));
        assertTrue(draft.addDice(d));
        assertEquals(length, draft.diceRemaining());
        assertSame(d, draft.findDice(d.getID()));
    }

    public void testToString() {
        assertEquals("it.polimi.model.Draft@ " + draft.hashCode(), draft.toString());
    }

    public void testDump() {
        draft.fillDraft();
        draft.dump();
    }
}
