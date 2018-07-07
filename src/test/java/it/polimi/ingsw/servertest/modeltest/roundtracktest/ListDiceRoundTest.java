package it.polimi.ingsw.servertest.modeltest.roundtracktest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.roundtrack.ListDiceRound;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListDiceRoundTest extends TestCase {

    private static final Random random = new Random();
    private Colors col = Colors.random();
    private int id = random.nextInt(90);

    public ListDiceRoundTest(String testName) {
        super(testName);
    }

    /**
     * Testing adding Dice to a round of RoundTrack
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
    public void testAddDice() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        List<Dice> dices = new ArrayList<>();
        int n = random.nextInt(7)+3;

        for (int i=0; i<n; i++) {
            dices.add(new Dice(i, col));
        }

        Dice d = new Dice(n, col);

        assertNotSame(dices.toString(), list.toString());
        assertTrue(list.addDice(d));
        assertThrows(SameDiceException.class, () -> list.addDice(d));
        assertTrue(list.addDice(dices));
        assertThrows(SameDiceException.class, () -> list.addDice(dices));
    }

    /**
     * Testing removing Dice from a round of RoundTrack
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     * @throws EmptyException when trying to remove a dice from an empty round
     */
    public void testRemoveDice() throws IDNotFoundException, SameDiceException, EmptyException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);

        list.addDice(d);
        assertTrue(list.rmDice(d));
    }

    /**
     * testing reaction when removing from an empty ListDiceRound
     * @throws IDNotFoundException when dice is not in the specified round
     */
    public void testEmptyException() throws IDNotFoundException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);

        assertThrows(EmptyException.class, () -> list.rmDice(d));
    }

    /**
     * Testing reaction when asking for a Dice not in ListDiceRound
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
    public void testIDNotFoundException() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        int idDiff = (id+1)%90;

        Dice dDiff = new Dice(idDiff, col);
        list.addDice(d);

        assertThrows(IDNotFoundException.class, () -> list.rmDice(dDiff));
        assertThrows(IDNotFoundException.class, () -> list.getDice(dDiff.getID()));
    }

    /**
     * Testing if a Dice can be found
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
    public void testGetDice() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        list.addDice(d);

        assertNotSame(d, list.getDice(id));
        assertSame(id, list.getDice(id).getID());
        assertSame(col, list.getDice(id).getColor());
    }

    /**
     * testing the presence of a Dice
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws SameDiceException when trying to add the same Dice twice
     */
    public void testContains() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        list.addDice(d);

        assertTrue(list.contains(d));
        assertFalse(list.contains(d.copyDice()));
    }

}
