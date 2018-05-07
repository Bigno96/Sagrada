package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

public class ListRoundTest extends TestCase {

    public void testListRound() throws IDNotFoundException {
        Dice dice1G = new Dice(1, Colors.YELLOW);
        Dice dice2B = new Dice(2, Colors.BLUE);
        ListRound listRound = new ListRound();
        DiceBag db = new DiceBag();
        int nDice = 9;

        Draft draft = new Draft(db, nDice);
        assertTrue(draft.addDice(dice1G));
        assertFalse(draft.addDice(dice1G));
        listRound.addDice();
        assertTrue(draft.addDice(dice1G));
        assertEquals(listRound.getDice(0), dice1G);
        assertTrue(listRound.rmDice(dice1G));
        assertFalse(listRound.rmDice(dice1G));

    }
}
