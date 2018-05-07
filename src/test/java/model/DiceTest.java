package model;

import exception.IDNotFoundException;
import exception.ValueException;
import junit.framework.TestCase;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiceTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(90);
    private Colors col = Colors.random();

    public DiceTest(String testName) {
        super(testName);
    }

    public void testGetter() throws IDNotFoundException{
        assertEquals(id, new Dice(id, col).getID());
        assertEquals(col, new Dice(id, col).getColor());

    }

    public void testIDNotFoundException() {
        final int randNeg = random.nextInt(1) - (random.nextInt() +1);
        final int randPos = random.nextInt(1) + 90;

        assertThrows(IDNotFoundException.class, () -> new Dice(randNeg, Colors.random()));
        assertThrows(IDNotFoundException.class, () -> new Dice(randPos, Colors.random()));
    }

    public void testRollDice() throws IDNotFoundException {            // roll Dice must produce value x | 0 < x < 7
        Dice d = new Dice(id, col);
        d.rollDice();

        assertTrue(0 < d.getValue() && 7 > d.getValue());
    }

    public void testChangeValue() throws IDNotFoundException, ValueException {         // testing Dice's value changing
        int value = random.nextInt(6)+1;
        Dice d = new Dice(id, col);
        d.rollDice();
        d.changeValue(value);

        assertEquals(value, d.getValue());
    }

    public void testValueException() throws IDNotFoundException {
        int valueNeg = random.nextInt(1)-(random.nextInt()+1);
        int valuePos = random.nextInt() + 7;

        Dice d = new Dice(id, col);
        d.rollDice();

        assertThrows(ValueException.class, () -> d.changeValue(valuePos));
        assertThrows(ValueException.class, () -> d.changeValue(valueNeg));
    }

    public void testCopyDice() throws IDNotFoundException {
        Dice d = new Dice(id, col);

        assertTrue(d.copyDice().isEqual(d));
    }

}
