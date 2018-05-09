package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import junit.framework.TestCase;

import java.net.IDN;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoundTrackTest extends TestCase {

    private static final Random random = new Random();
    private int nDice = random.nextInt(9)+1;
    private DiceBag db = new DiceBag();
    private Draft draft = new Draft(db, nDice);
    private int id = random.nextInt(90);
    private Colors col = Colors.random();
    private int round = random.nextInt(10);

    public RoundTrackTest( String testName ) throws IDNotFoundException {
        super( testName );
    }

    public void testFindDice() throws EmptyException, IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        draft.addDice(d);

        roundTrack.moveDraft(round);

        assertSame(d.getID(), roundTrack.findDice(id).getID());
        assertSame(d.getColor(), roundTrack.findDice(id).getColor());
        assertNotSame(d, roundTrack.findDice(id));
    }

    public void testIDNotFoundException() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        draft.addDice(d);
        int idDiff;

        do {
            idDiff = random.nextInt(90);
        } while (id == idDiff);

        Dice dDiff = new Dice(idDiff, col);

        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(d.getID()));
        
        roundTrack.moveDraft(round);

        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(dDiff.getID()));
    }
 }