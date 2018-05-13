package model.windowcard;

import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;
import model.Colors;

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

    public MatrixCellTest(String testName) {
        super (testName);
    }

    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++)
                cellList.add(new Cell(random.nextInt(7), Colors.random(), i, j));
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


    public void testIsBorder() throws PositionException, ValueException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);

        row = 0;
        col = random.nextInt(5);
        Cell cTop = new Cell(value, color, row, col);
        row = 3;
        col = random.nextInt(5);
        Cell cBot = new Cell(value, color, row, col);
        row = 0;
        col = 1;
        Cell cL1 = new Cell(value, color, row, col);
        row = 0;
        col = 2;
        Cell cL2 = new Cell(value, color, row, col);
        row = 3;
        col = 1;
        Cell cR1 = new Cell(value, color, row, col);
        row = 3;
        col = 2;
        Cell cR2 = new Cell(value, color, row, col);

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
