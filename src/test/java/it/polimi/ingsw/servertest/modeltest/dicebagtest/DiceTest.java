package it.polimi.ingsw.servertest.modeltest.dicebagtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import junit.framework.TestCase;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiceTest extends TestCase {

    private static final Random random = new Random();
    private static int id = random.nextInt(90);
    private static Colors col = Colors.random();

    public DiceTest(String testName) {
        super(testName);
    }

    /**
     * Basic testing of getter methods for dice.id and dice.color
     * @throws IDNotFoundException thrown by dice constructor
     */
    public void testGetter() throws IDNotFoundException{
        Dice d = new Dice(id, col);
        Dice d1 = new Dice((id+1)%90, col);

        assertEquals(id, d.getID());
        assertEquals(col, d.getColor());

        assertEquals((id+1)%90, d1.getID());
        assertNotSame(id, d1.getID());
        assertEquals(col, d1.getColor());
    }

    /**
     * Testing throwing of IDNotFoundException when using both dice constructor.
     * Test case for id values over 90 and id values negative
     */
    public void testIDNotFoundException() {
        int randNeg = random.nextInt(1) - (random.nextInt() +1);
        int randPos = random.nextInt() + 90;
        int value = random.nextInt(6)+1;

        assertThrows(IDNotFoundException.class, () -> new Dice(randNeg, Colors.random()));
        assertThrows(IDNotFoundException.class, () -> new Dice(randPos, Colors.random()));
        assertThrows(IDNotFoundException.class, () -> new Dice(randNeg, Colors.random(), value));
        assertThrows(IDNotFoundException.class, () -> new Dice(randPos, Colors.random(), value));
    }

    /**
     * Testing rolling Dice that must produce value x | 0 < x < 7
     * @throws IDNotFoundException thrown by dice constructor
     */
    public void testRollDice() throws IDNotFoundException {
        Dice d = new Dice(id, col);
        d.rollDice();

        assertTrue(0 < d.getValue() && 7 > d.getValue());
    }

    /**
     * Testing Dice's value changing
     * @throws IDNotFoundException thrown by dice constructor
     * @throws ValueException thrown by change Value
     */
    public void testChangeValue() throws IDNotFoundException, ValueException {
        int value = random.nextInt(6)+1;
        Dice d = new Dice(id, col);
        d.rollDice();
        d.changeValue(value);

        assertEquals(value, d.getValue());
    }

    /**
     * Testing throwing ValueException when value x | x < 0 || x > 6
     * @throws IDNotFoundException thrown by change Value
     */
    public void testValueException() throws IDNotFoundException {
        int valueNeg = random.nextInt(1)-(random.nextInt()+1);
        int valuePos = random.nextInt() + 7;

        Dice d = new Dice(id, col);
        d.rollDice();

        assertThrows(ValueException.class, () -> d.changeValue(valuePos));
        assertThrows(ValueException.class, () -> d.changeValue(valueNeg));
    }

    /**
     * Testing copying dice and testing is equal method
     * @throws IDNotFoundException thrown by change Value
     */
    public void testCopyDice() throws IDNotFoundException {
        Dice d = new Dice(id, col);

        assertTrue(d.copyDice().isEqual(d));
        assertNotSame(d, d.copyDice());
        assertNotSame(d.toString(), d.copyDice().toString());
    }

}
