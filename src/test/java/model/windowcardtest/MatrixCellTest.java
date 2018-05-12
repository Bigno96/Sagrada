package model.windowcardtest;

import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;
import model.Colors;
import model.windowcard.Cell;
import model.windowcard.WindowCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
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
        assertTrue(matrix!=null);
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

}
