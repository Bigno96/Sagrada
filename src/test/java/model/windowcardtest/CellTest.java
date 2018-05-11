package model.windowcardtest;

import junit.framework.TestCase;
import model.Colors;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest extends TestCase{

    private static final Random random = new Random();
    private int id = random.nextInt(90);
    private int pos = random.nextInt(20);
    private int value = random.nextInt(6)+1;
    private Colors col = Colors.random();

    public CellTest(String testName) {
        super (testName);
    }
    /*
    public void testGetter() throws PositionException, ValueException {
        Cell c = new Cell(value, col, pos);

        assertSame(value, c.getValue());
        assertSame(col, c.getColor());
        assertSame(pos, c.getPos());
    }

    public void testChangeDiceValue() throws PositionException, ValueException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, col, pos);
        int rand = random.nextInt(7);
        c.setDice(new Dice(id, col));
        c.changeDiceValue(rand);

        assertEquals(rand, c.getDice().getValue());
    }

    public void testIsOccupied() throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
        Cell c = new Cell(value, col, pos);
        Dice d = new Dice(id, col);

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
        Cell c = new Cell(value, col, pos);
        int wrongVal;
        Colors wrongCol;

        do {
            wrongVal = random.nextInt(6)+1;
        } while (wrongVal == value);

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(col));

        c.setDice(new Dice(id, col));
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
        Cell c = new Cell(value, col, pos);
        c.setDice(new Dice(id, col));

        assertThrows(ValueException.class, () -> new Cell(valueNeg, col, pos));
        assertThrows(ValueException.class, () -> new Cell(valuePos, col, pos));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valueNeg));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valuePos));
    }

    public void testPositionException() {
        final int posNeg = random.nextInt(20) - (random.nextInt()+20);
        final int posPos = random.nextInt() + 20;

        assertThrows(PositionException.class, () -> new Cell(value, col, posNeg));
        assertThrows(PositionException.class, () -> new Cell(value, col, posPos));
    }

    public void testNotEmptyException() throws PositionException, IDNotFoundException, NotEmptyException, ValueException {
        Cell c = new Cell(value, col, pos);
        Dice d = new Dice(id, col);

        c.setDice(d);
        assertThrows(NotEmptyException.class, () -> c.setDice(d));
    }

    public void testFreeCell() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, col, pos);
        Dice d = new Dice(id, col);

        c.setDice(d);
        assertTrue(c.isOccupied());
        c.freeCell();
        assertFalse(c.isOccupied());
    }

    */

}