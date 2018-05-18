package it.polimi.ingsw.servertest.modeltest.roundtracktest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;

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

    public void testFindDice() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        draft.addDice(d);

        roundTrack.moveDraft(round);

        assertNotSame(draft.toString(), roundTrack.toString());
        assertSame(d.getID(), roundTrack.findDice(id).getID());
        assertSame(d.getColor(), roundTrack.findDice(id).getColor());
        assertNotSame(d, roundTrack.findDice(id));
    }

    public void testFindColor() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        Colors wrongCol;
        draft.addDice(d);

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(col) || wrongCol.equals(Colors.NULL));

        roundTrack.moveDraft(round);

        assertTrue(roundTrack.findColor(col));
        assertFalse(roundTrack.findColor(wrongCol));
    }

    public void testGetRound() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        int id1 = (id+1)%90;
        Dice d1 = new Dice(id1, col);
        int round = random.nextInt(10);
        roundTrack.addDice(d, round);

        assertSame(round, roundTrack.getRound(d));
        assertThrows(IDNotFoundException.class, () -> roundTrack.getRound(d1));
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

    public void testAddDice() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        int roundNeg = random.nextInt() - (random.nextInt()+1);
        int roundOver = random.nextInt() + 10;
        int roundDiff = (round+1)%10;

        assertTrue(roundTrack.addDice(d, round));
        assertSame(id, roundTrack.findDice(id).getID());
        assertTrue(roundTrack.addDice(d, roundDiff));
        assertSame(id, roundTrack.findDice(id).getID());
        assertThrows(SameDiceException.class, () -> roundTrack.addDice(d, round));
        assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.addDice(d, roundNeg));
        assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.addDice(d, roundOver));
    }

    public void testRmDice() throws IDNotFoundException, SameDiceException, EmptyException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        int roundNeg = random.nextInt() - (random.nextInt()+1);
        int roundOver = random.nextInt() + 10;
        int roundDiff = (round+1)%10;
        int idDiff = (id+1)%90;

        Dice dDiff = new Dice(idDiff, col);

        roundTrack.addDice(d, round);
        roundTrack.addDice(dDiff, round);

        assertThrows(EmptyException.class, () -> roundTrack.rmDice(d, roundDiff));
        assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.rmDice(d, roundNeg));
        assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.rmDice(d, roundOver));
        assertTrue(roundTrack.rmDice(d, round));
        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(id));
        assertThrows(IDNotFoundException.class, () -> roundTrack.rmDice(d, round));
    }
 }