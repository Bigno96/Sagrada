package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import junit.framework.TestCase;

public class RoundTrackTest extends TestCase {

    public RoundTrackTest( String testName ) {
        super( testName );
    }

    public void testFindDice() throws IDNotFoundException, EmptyException, SameDiceException {
        Dice dice1G = new Dice(1, Colors.YELLOW);
        Dice dice2B = new Dice(2, Colors.BLUE);
        int nDice = 9;
        DiceBag db = new DiceBag();
        Draft draft = new Draft(db, nDice);
        RoundTrack roundTrack = new RoundTrack(draft);

        draft.addDice(dice1G);
        assertSame(1, draft.findDice(1).getID());

        roundTrack.moveDraft(0);

        draft.addDice(dice1G);
        assertSame(1, draft.findDice(1).getID());
        assertTrue(roundTrack.findDice(dice1G));
        assertFalse(roundTrack.findDice(dice2B));
    }
}