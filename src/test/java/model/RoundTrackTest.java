package model;

import junit.framework.TestCase;

public class RoundTrackTest extends TestCase {

    public RoundTrackTest( String testName ) {
        super( testName );
    }

    public void testFindDice(){
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
        RoundTrack roundTrack = new RoundTrack();

        assertTrue(roundTrack.findDice(dice1G));
        assertFalse(roundTrack.findDice(dice2B));
    }
}