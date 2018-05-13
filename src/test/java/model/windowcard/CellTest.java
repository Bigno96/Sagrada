package model.windowcard;

import exception.IDNotFoundException;
import exception.NotEmptyException;
import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;
import model.Colors;
import model.dicebag.Dice;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest extends TestCase{

    private static final Random random = new Random();
    private int id = random.nextInt(90);
    private int value = random.nextInt(6)+1;
    private Colors color = Colors.random();
    private int row = random.nextInt(4);
    private int col = random.nextInt(5);

    public CellTest(String testName) {
        super (testName);
    }

    public void testGetter() throws PositionException, ValueException {
        Cell c = new Cell(value, color, row, col);

        assertSame(value, c.getValue());
        assertSame(color, c.getColor());
        assertSame(row, c.getRow());
        assertSame(col, c.getCol());
    }

    public void testChangeDiceValue() throws PositionException, ValueException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, color, row, col);
        int rand = random.nextInt(7);
        c.setDice(new Dice(id, color));
        c.changeDiceValue(rand);

        assertEquals(rand, c.getDice().getValue());
    }

    public void testIsOccupied() throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
        Cell c = new Cell(value, color, row, col);
        Dice d = new Dice(id, color);

        assertFalse(c.isOccupied());
        assertNull(c.getDice());

        c.setDice(d);

        assertTrue(c.isOccupied());
        assertTrue(c.getDice().isEqual(d));

        c.freeCell();

        assertFalse(c.isOccupied());
        assertNull(c.getDice());
    }

    public void testCheck() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, color, row, col);
        int wrongVal;
        Colors wrongCol;

        do {
            wrongVal = random.nextInt(6)+1;
        } while (wrongVal == value);

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(color));

        c.setDice(new Dice(id, color));
        assertTrue(c.checkColor());

        c.changeDiceValue(value);
        assertTrue(c.checkValue());

        c.changeDiceValue(wrongVal);
        assertFalse(c.checkValue());

        c.freeCell();
        c.setDice(new Dice(id, wrongCol));
        assertFalse(c.checkColor());
    }

    public void testValueException() throws PositionException, ValueException, IDNotFoundException, NotEmptyException {
        final int valueNeg = random.nextInt(7) - (random.nextInt()+7);
        final int valuePos = random.nextInt() + 7;
        Cell c = new Cell(value, color, row, col);
        c.setDice(new Dice(id, color));

        assertThrows(ValueException.class, () -> new Cell(valueNeg, color, row, col));
        assertThrows(ValueException.class, () -> new Cell(valuePos, color, row, col));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valueNeg));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valuePos));
    }

    public void testNotEmptyException() throws PositionException, IDNotFoundException, NotEmptyException, ValueException {
        Cell c = new Cell(value, color, row, col);
        Dice d = new Dice(id, color);

        c.setDice(d);
        assertThrows(NotEmptyException.class, () -> c.setDice(d));
    }

    public void testFreeCell() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, color, row, col);
        Dice d = new Dice(id, color);

        c.setDice(d);
        assertTrue(c.isOccupied());
        c.freeCell();
        assertFalse(c.isOccupied());
    }

}