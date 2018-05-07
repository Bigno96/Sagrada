package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

public class RoundTrackTest extends TestCase {

    public RoundTrackTest( String testName ) {
        super( testName );
    }

    public void testFindDice() throws IDNotFoundException {
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
        RoundTrack roundTrack = new RoundTrack();

        assertTrue(roundTrack.findDice(dice1G));
        assertFalse(roundTrack.findDice(dice2B));
    }
}