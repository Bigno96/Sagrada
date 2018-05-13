package model.dicebag;

import exception.IDNotFoundException;
import exception.ValueException;
import junit.framework.TestCase;
import model.Colors;

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
        Dice d = new Dice(id, col);
        Dice d1 = new Dice(id, col);
        assertEquals(id, d.getID());
        assertEquals(col, d.getColor());
        assertEquals(id, d1.getID());
        assertEquals(col, d1.getColor());
        assertNotSame(d.toString(), d1.toString());
    }

    public void testIDNotFoundException() {
        int randNeg = random.nextInt(1) - (random.nextInt() +1);
        int randPos = random.nextInt() + 90;
        int value = random.nextInt(6)+1;

        assertThrows(IDNotFoundException.class, () -> new Dice(randNeg, Colors.random()));
        assertThrows(IDNotFoundException.class, () -> new Dice(randPos, Colors.random()));
        assertThrows(IDNotFoundException.class, () -> new Dice(randNeg, Colors.random(), value));
        assertThrows(IDNotFoundException.class, () -> new Dice(randPos, Colors.random(), value));
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
        assertNotSame(d, d.copyDice());
    }

}
