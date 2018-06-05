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
import it.polimi.ingsw.server.model.windowcard.MatrixCell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixCellTest extends TestCase {

    private static final Random random = new Random();
    private Cell[][] matrix;
    private int rows = 4;
    private int cols = 5;
    private int row;
    private int col;
    private int id = random.nextInt(20);
    private int fp = random.nextInt(4)+3;
    private int value = random.nextInt(6)+1;
    private Colors color = Colors.random();
    private GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private int max_row = gameSettings.getWindowCardMaxRow();
    private int max_col = gameSettings.getWindowCardMaxColumn();

    public MatrixCellTest(String testName) {
        super (testName);
    }

    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++)
                cellList.add(new Cell(random.nextInt(7), Colors.random(), i, j, max_row, max_col));
        return cellList;
    }

    public void testGetter() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        matrix = card.getWindow().getMatrix();
        row = random.nextInt(4);
        col = random.nextInt(5);
        Cell c = card.getWindow().getCell(row, col);

        assertSame(rows, card.getWindow().getRows());
        assertSame(cols, card.getWindow().getCols());
        assertSame(matrix, card.getWindow().getMatrix());
        assertSame(c, card.getWindow().getCell(row, col));
    }

    public void testLoadMatrixCell() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        matrix = card.getWindow().getMatrix();

        assertNotNull(matrix);
        assertNotSame(card.getWindow().toString(), list.toString());
    }

    public void testContainsDice() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);

        Colors col = card.getWindow().getCell(0, 0).getColor();
        int val = card.getWindow().getCell(0, 0).getValue();
        Dice d = new Dice(id, col, val);
        card.getWindow().getCell(0,0).setDice(d);

        assertTrue(card.getWindow().containsDice(d));

        Dice d1 = new Dice(id+1, col, val);

        assertFalse(card.getWindow().containsDice(d1));
    }


    public void testIsBorder() throws PositionException, ValueException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);

        row = 0;
        col = random.nextInt(5);
        Cell cTop = new Cell(value, color, row, col, max_row, max_col);
        row = 3;
        col = random.nextInt(5);
        Cell cBot = new Cell(value, color, row, col, max_row, max_col);
        row = 0;
        col = 1;
        Cell cL1 = new Cell(value, color, row, col, max_row, max_col);
        row = 0;
        col = 2;
        Cell cL2 = new Cell(value, color, row, col, max_row, max_col);
        row = 3;
        col = 1;
        Cell cR1 = new Cell(value, color, row, col, max_row, max_col);
        row = 3;
        col = 2;
        Cell cR2 = new Cell(value, color, row, col, max_row, max_col);

        assertTrue(card.getWindow().isBorder(cTop));
        assertTrue(card.getWindow().isBorder(cBot));
        assertTrue(card.getWindow().isBorder(cL1));
        assertTrue(card.getWindow().isBorder(cL2));
        assertTrue(card.getWindow().isBorder(cR1));
        assertTrue(card.getWindow().isBorder(cR2));
    }

    public void testPositionException() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        MatrixCell window = card.getWindow();

        row = random.nextInt(6)+4;
        col = random.nextInt(5);

        assertThrows(PositionException.class, () -> window.retDiagonal(row, col));
        assertThrows(PositionException.class, () -> window.retOrtogonal(row, col));
        assertThrows(PositionException.class, () -> window.retNeighbors(row, col));
    }

    public void testRetOrtogonal() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        List<Cell> ort;

        row = random.nextInt(4);
        col = random.nextInt(5);

        ort = card.getWindow().retOrtogonal(row, col);

        assertEquals(ort, card.getWindow().retOrtogonal(row, col));
    }

    public void testRetDiagonal() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        List<Cell> ort;

        row = random.nextInt(4);
        col = random.nextInt(5);

        ort = card.getWindow().retDiagonal(row, col);

        assertEquals(ort, card.getWindow().retDiagonal(row, col));
    }

    public void testRetNeighbors() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        List<Cell> ort;

        row = random.nextInt(4);
        col = random.nextInt(5);

        ort = card.getWindow().retNeighbors(row, col);

        assertEquals(ort, card.getWindow().retNeighbors(row, col));
    }

}
