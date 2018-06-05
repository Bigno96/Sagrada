package it.polimi.ingsw.servertest.modeltest.windowcardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest extends TestCase{

    private static final Random random = new Random();
    private int id = random.nextInt(90);
    private int value = random.nextInt(6)+1;
    private Colors color = Colors.random();
    private int row = random.nextInt(4);
    private int col = random.nextInt(5);
    private GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private int max_row = gameSettings.getWindowCardMaxRow();
    private int max_col = gameSettings.getWindowCardMaxColumn();

    public CellTest(String testName) {
        super (testName);
    }

    public void testGetter() throws PositionException, ValueException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);

        assertSame(value, c.getValue());
        assertSame(color, c.getColor());
        assertSame(row, c.getRow());
        assertSame(col, c.getCol());
    }

    public void testChangeDiceValue() throws PositionException, ValueException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);
        int rand = random.nextInt(7);
        c.setDice(new Dice(id, color));
        c.changeDiceValue(rand);

        assertEquals(rand, c.getDice().getValue());
    }

    public void testIsOccupied() throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);
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
        Cell c = new Cell(value, color, row, col, max_row, max_col);
        int wrongVal;
        Colors wrongCol;


        wrongVal = (value+1)%5;

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(color));

        c.setDice(new Dice(id, color));
        assertTrue(c.checkColor());

        c.changeDiceValue(value);
        assertTrue(c.checkValue());

        c.changeDiceValue(wrongVal);
        if (c.getValue() != 0)
            assertFalse(c.checkValue());
        c.setIgnoreValue(true);
        assertTrue(c.checkValue());

        c.freeCell();
        c.setDice(new Dice(id, wrongCol));
        if (!c.getColor().equals(Colors.WHITE))
            assertFalse(c.checkColor());
        c.setIgnoreColor(true);
        assertTrue(c.checkColor());
    }

    public void testValueException() throws PositionException, ValueException, IDNotFoundException, NotEmptyException {
        final int valueNeg = random.nextInt(7) - (random.nextInt()+7);
        final int valuePos = random.nextInt() + 7;
        Cell c = new Cell(value, color, row, col, max_row, max_col);
        c.setDice(new Dice(id, color));

        assertThrows(ValueException.class, () -> new Cell(valueNeg, color, row, col, max_row, max_col));
        assertThrows(ValueException.class, () -> new Cell(valuePos, color, row, col, max_row, max_col));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valueNeg));
        assertThrows(ValueException.class, () -> c.changeDiceValue(valuePos));
    }

    public void testNotEmptyException() throws PositionException, IDNotFoundException, NotEmptyException, ValueException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);
        Dice d = new Dice(id, color);

        c.setDice(d);
        assertThrows(NotEmptyException.class, () -> c.setDice(d));
    }

    public void testFreeCell() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);
        Dice d = new Dice(id, color);

        c.setDice(d);
        assertTrue(c.isOccupied());
        c.freeCell();
        assertFalse(c.isOccupied());
    }

    public void testIgnoreRestriction() throws ValueException, PositionException {
        Cell c = new Cell(value, color, row, col, max_row, max_col);

        assertFalse(c.isIgnoreNearby());
        c.setIgnoreNearby(true);
        assertTrue(c.isIgnoreNearby());
        c.setIgnoreNearby(false);
        assertFalse(c.isIgnoreNearby());

        assertFalse(c.isIgnoreValue());
        c.setIgnoreValue(true);
        assertTrue(c.isIgnoreValue());
        c.setIgnoreValue(false);
        assertFalse(c.isIgnoreValue());

        assertFalse(c.isIgnoreColor());
        c.setIgnoreColor(true);
        assertTrue(c.isIgnoreColor());
        c.setIgnoreColor(false);
        assertFalse(c.isIgnoreColor());
    }

}