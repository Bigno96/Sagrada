package model.roundtracktest;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import junit.framework.TestCase;
import model.Colors;
import model.dicebag.Dice;
import model.roundtrack.ListDiceRound;

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

    public void testAddDice() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        List<Dice> dices = new ArrayList<>();
        int n = random.nextInt(7)+3;

        for (int i=0; i<n; i++) {
            dices.add(new Dice(i, col));
        }

        Dice d = new Dice(n, col);

        assertTrue(list.addDice(d));
        assertThrows(SameDiceException.class, () -> list.addDice(d));
        assertTrue(list.addDice(dices));
        assertThrows(SameDiceException.class, () -> list.addDice(dices));
    }

    public void testRemoveDice() throws IDNotFoundException, SameDiceException, EmptyException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);

        list.addDice(d);
        assertTrue(list.rmDice(d));
    }

    public void testEmptyException() throws IDNotFoundException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);

        assertThrows(EmptyException.class, () -> list.rmDice(d));
    }

    public void testIDNotFoundException() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        int idDiff;

        do {
            idDiff = random.nextInt(90);
        } while (id == idDiff);

        Dice dDiff = new Dice(idDiff, col);
        list.addDice(d);

        assertThrows(IDNotFoundException.class, () -> list.rmDice(dDiff));
        assertThrows(IDNotFoundException.class, () -> list.getDice(dDiff.getID()));
    }

    public void testGetDice() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        list.addDice(d);

        assertNotSame(d, list.getDice(id));
        assertSame(id, list.getDice(id).getID());
        assertSame(col, list.getDice(id).getColor());
    }

    public void testContains() throws IDNotFoundException, SameDiceException {
        ListDiceRound list = new ListDiceRound();
        Dice d = new Dice(id, col);
        list.addDice(d);

        assertTrue(list.contains(d));
        assertFalse(list.contains(d.copyDice()));
    }

}
