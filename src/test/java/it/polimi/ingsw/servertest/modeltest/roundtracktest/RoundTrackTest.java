package it.polimi.ingsw.servertest.modeltest.roundtracktest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.RoundNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import junit.framework.TestCase;

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

    /**
     * Testing the correct search of a dice in round Track
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
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

    /**
     * Testing the correct search of a color
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
    public void testFindColor() throws IDNotFoundException, SameDiceException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        Colors wrongCol;
        draft.addDice(d);

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(col) || wrongCol.equals(Colors.WHITE));

        roundTrack.moveDraft(round);

        assertTrue(roundTrack.findColor(col));
        assertFalse(roundTrack.findColor(wrongCol));
    }

    /**
     * Testing getRound
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     * @throws RoundNotFoundException when wrong round is requested
     */
    public void testGetRound() throws IDNotFoundException, SameDiceException, RoundNotFoundException {
        RoundTrack roundTrack = new RoundTrack(draft);
        Dice d = new Dice(id, col);
        int id1 = (id+1)%90;
        Dice d1 = new Dice(id1, col);
        int round = random.nextInt(10);
        roundTrack.addDice(d, round);

        assertSame(round, roundTrack.getRound(d));
        assertThrows(IDNotFoundException.class, () -> roundTrack.getRound(d1));
    }

    /**
     * Testing reaction when trying to find a dice not in round Track
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
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

    /**
     * Testing about adding dices and reaction to illegal adding attempts
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     * @throws RoundNotFoundException when wrong round is requested
     */
    public void testAddDice() throws IDNotFoundException, SameDiceException, RoundNotFoundException {
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
        assertThrows(RoundNotFoundException.class, () -> roundTrack.addDice(d, roundNeg));
        assertThrows(RoundNotFoundException.class, () -> roundTrack.addDice(d, roundOver));
    }

    /**
     * Testing about removing dices and reaction to illegal removing attempts
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     * @throws RoundNotFoundException when wrong round is requested
     * @throws EmptyException when trying to remove a dice from an empty round
     */
    public void testRmDice() throws IDNotFoundException, SameDiceException, EmptyException, RoundNotFoundException {
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
        assertThrows(RoundNotFoundException.class, () -> roundTrack.rmDice(d, roundNeg));
        assertThrows(RoundNotFoundException.class, () -> roundTrack.rmDice(d, roundOver));
        assertTrue(roundTrack.rmDice(d, round));
        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(id));
        assertThrows(IDNotFoundException.class, () -> roundTrack.rmDice(d, round));
    }
 }