package model;

import exception.IDNotFoundException;
import exception.SameDiceException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        assertTrue(list.addDice(dices));
    }
}
