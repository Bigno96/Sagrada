package model;

import junit.framework.TestCase;

public class ListRoundTest extends TestCase {

    public void testListRound(){
        Dice dice1G = new Dice(1, Dice.colors.YELLOW);
        Dice dice2B = new Dice(2, Dice.colors.BLUE);
        ListRound listRound = new ListRound();
        DiceBag db = DiceBag.getInstance();
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
