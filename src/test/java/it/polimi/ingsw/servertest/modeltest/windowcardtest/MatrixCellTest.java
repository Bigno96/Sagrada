package it.polimi.ingsw.servertest.modeltest.windowcardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.MatrixCell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixCellTest extends TestCase {

    private Cell[][] matrix;
    private int row;
    private int col;

    private static final Random random = new Random();
    private final int id = random.nextInt(20);
    private final int fp = random.nextInt(4)+3;
    private final int value = random.nextInt(6)+1;
    private final Colors color = Colors.random();

    private final GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private final int max_row = gameSettings.getWindowCardMaxRow();
    private final int max_col = gameSettings.getWindowCardMaxColumn();
    private static final int MIN = 0;

    public MatrixCellTest(String testName) {
        super (testName);
    }

    /**
     * Fills a list of cell with 20 random cells
     * @return List<Cell> list, list.size() = 20
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=MIN; i<max_row; i++)
            for (int j=MIN; j<max_col; j++)
                cellList.add(new Cell(random.nextInt(7), Colors.random(), i, j, max_row, max_col));

        return cellList;
    }

    /**
     * Testing getMaxRow, getMaxCol, getMatrix, getCell
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws IDNotFoundException thrown when building dice
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testGetter() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());

        matrix = card.getWindow().getMatrix();
        row = random.nextInt(max_row);
        col = random.nextInt(max_col);
        Cell c = card.getWindow().getCell(row, col);
        int wrong_row = random.nextInt(1)+max_row;
        int wrong_col = random.nextInt(1)+max_col;
        int neg_row = random.nextInt(1)-(random.nextInt()+1);
        int neg_col = random.nextInt(1)-(random.nextInt()+1);

        assertSame(max_row, card.getWindow().getMaxRow());
        assertSame(max_col, card.getWindow().getMaxCol());
        assertSame(matrix, card.getWindow().getMatrix());

        assertSame(c, card.getWindow().getCell(row, col));
        assertNull(card.getWindow().getCell(wrong_row, col));
        assertNull(card.getWindow().getCell(row, wrong_col));
        assertNull(card.getWindow().getCell(wrong_row, wrong_col));
        assertNull(card.getWindow().getCell(neg_row, col));
        assertNull(card.getWindow().getCell(row, neg_col));
        assertNull(card.getWindow().getCell(neg_row, neg_col));

        int diceId = random.nextInt(90);
        Dice d = new Dice(diceId, color, value);

        int wrongId = (random.nextInt(90)+diceId)%90;
        Dice wrongD = new Dice(wrongId, color, value);

        c.setDice(d);
        assertSame(c, card.getWindow().getCell(d));
        assertNull(card.getWindow().getCell(wrongD));
    }

    /**
     * Testing setting correctly matrix inside of window card
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testLoadMatrixCell() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        matrix = card.getWindow().getMatrix();
        MatrixCell window = new MatrixCell(max_row, max_col);

        assertNotNull(matrix);
        assertNotSame(card.getWindow().getMatrix(), window);
    }

    /**
     * Testing if the dice passed is in the matrix
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws IDNotFoundException thrown when building dice
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testContainsDice() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());

        Colors col = card.getWindow().getCell(0, 0).getColor();
        int val = card.getWindow().getCell(0, 0).getValue();
        Dice d = new Dice(id, col, val);
        card.getWindow().getCell(0,0).setDice(d);

        assertTrue(card.getWindow().containsDice(d));

        Dice d1 = new Dice(id+1, col, val);

        assertFalse(card.getWindow().containsDice(d1));
    }


    /**
     * Testing finding cells on border
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws ValueException thrown when passing wrong values to cell
     */
    public void testIsBorder() throws PositionException, ValueException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());

        row = MIN;
        col = random.nextInt(max_col-2)+1;
        Cell top = new Cell(value, color, row, col, max_row, max_col);

        row = max_row-1;
        col = random.nextInt(max_col-2)+1;
        Cell bot = new Cell(value, color, row, col, max_row, max_col);

        row = random.nextInt(max_row-2)+1;
        col = MIN;
        Cell left = new Cell(value, color, row, col, max_row, max_col);

        row = random.nextInt(max_row-2)+1;
        col = max_col-1;
        Cell right = new Cell(value, color, row, col, max_row, max_col);

        row = random.nextInt(max_row-2)+1;
        col = random.nextInt(max_col-2)+1;
        Cell notBorder = new Cell(value, color, row, col, max_row, max_col);

        assertTrue(card.getWindow().isBorder(top));
        assertTrue(card.getWindow().isBorder(bot));
        assertTrue(card.getWindow().isBorder(left));
        assertTrue(card.getWindow().isBorder(right));
        assertFalse(card.getWindow().isBorder(notBorder));
    }

    /**
     * Testing reaction when asking for nearby cells around an incorrect cell
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testPositionException() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        MatrixCell window = card.getWindow();

        row = random.nextInt(1)+max_row;
        col = random.nextInt(1)+max_col;
        int neg_row = random.nextInt(1)-(random.nextInt()+1);
        int neg_col = random.nextInt(1)-(random.nextInt()+1);

        assertThrows(PositionException.class, () -> window.retDiagonal(row, col));
        assertThrows(PositionException.class, () -> window.retOrthogonal(row, col));
        assertThrows(PositionException.class, () -> window.retNeighbors(row, col));

        assertThrows(PositionException.class, () -> window.retDiagonal(neg_row, neg_col));
        assertThrows(PositionException.class, () -> window.retOrthogonal(neg_row, neg_col));
        assertThrows(PositionException.class, () -> window.retNeighbors(neg_row, neg_col));
    }

    /**
     * Testing finding orthogonal neighbors of a cell
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testRetOrthogonal() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Cell> orthogonal = new ArrayList<>();
        matrix = card.getWindow().getMatrix();

        row = random.nextInt(max_row);
        col = random.nextInt(max_col);

        if (row != MIN)
            orthogonal.add(matrix[row-1][col]);

        if (col != MIN)
            orthogonal.add(matrix[row][col-1]);

        if (col != max_col-1)
            orthogonal.add(matrix[row][col+1]);

        if (row != max_row-1)
            orthogonal.add(matrix[row+1][col]);

        assertEquals(orthogonal, card.getWindow().retOrthogonal(row, col));
    }

    /**
     * Testing finding diagonal neighbors of a cell
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testRetDiagonal() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Cell> diagonal = new ArrayList<>();
        matrix = card.getWindow().getMatrix();

        row = random.nextInt(max_row);
        col = random.nextInt(max_col);

        if (row != MIN && col != MIN)
            diagonal.add(matrix[row-1][col-1]);

        if (row != MIN && col != max_col-1)
            diagonal.add(matrix[row-1][col+1]);

        if (row != max_row-1 && col != MIN)
            diagonal.add(matrix[row+1][col-1]);

        if (row != max_row-1 && col != max_col-1)
            diagonal.add(matrix[row+1][col+1]);

        assertEquals(diagonal, card.getWindow().retDiagonal(row, col));
    }

    /**
     * Testing finding all neighbors of a cell
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testRetNeighbors() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Cell> neighbors = new ArrayList<>();
        matrix = card.getWindow().getMatrix();

        row = random.nextInt(max_row);
        col = random.nextInt(max_col);

        if (row != MIN)
            neighbors.add(matrix[row-1][col]);

        if (col != MIN)
            neighbors.add(matrix[row][col-1]);

        if (col != max_col-1)
            neighbors.add(matrix[row][col+1]);

        if (row != max_row-1)
            neighbors.add(matrix[row+1][col]);

        if (row != MIN && col != MIN)
            neighbors.add(matrix[row-1][col-1]);

        if (row != MIN && col != max_col-1)
            neighbors.add(matrix[row-1][col+1]);

        if (row != max_row-1 && col != MIN)
            neighbors.add(matrix[row+1][col-1]);

        if (row != max_row-1 && col != max_col-1)
            neighbors.add(matrix[row+1][col+1]);

        assertEquals(neighbors, card.getWindow().retNeighbors(row, col));
    }

}
