package it.polimi.ingsw.servertest.modeltest.windowcardtest;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.MatrixCell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowCardTest extends TestCase{

    private static final Random random = new Random();
    private int id = random.nextInt(20);
    private int fp = random.nextInt(4)+3;

    private final GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private final int max_row = gameSettings.getWindowCardMaxRow();
    private final int max_col = gameSettings.getWindowCardMaxColumn();
    private static final int MIN = 0;

    public WindowCardTest(String testName) {
        super(testName);
    }

    /**
     * Fills a list of cell with 20 random cells
     * @return List<Cell> list, list.size() = 20
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        Colors color;
        for (int i=MIN; i<max_row; i++)
            for (int j=MIN; j<max_col; j++) {
                do {
                    color = Colors.random();
                } while (color == Colors.WHITE);
                cellList.add(new Cell(random.nextInt(6)+1, color, i, j,
                    gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn()));
            }

        return cellList;
    }

    /**
     * Fills a list of cell with 20 random cells with value = 0 and colors = white
     * @return List<Cell> list, list.size() = 20
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    private List<Cell> myCleanCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=MIN; i<max_row; i++)
            for (int j=MIN; j<max_col; j++) {
                cellList.add(new Cell(0, Colors.WHITE, i, j,
                    gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn()));
            }

        return cellList;
    }

    /**
     * Testing getId, getName, getNumFavPoint and getWindow
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testGetter() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        MatrixCell matrix = card.getWindow();

        assertEquals(id, card.getId());
        assertEquals("Test", card.getName());
        assertEquals(fp, card.getNumFavPoint());
        assertEquals(matrix, card.getWindow());
    }

    /**
     * Testing if first Dice is correctly positioned
     * @throws EmptyException thrown when checking an empty window card
     * @throws WrongPositionException thrown when a dice is found in an incorrect position
     * @throws ValueException thrown when passing wrong values to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testCheckFirstDice() throws EmptyException, WrongPositionException, ValueException, IDNotFoundException, NotEmptyException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        row = MIN;
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        row = max_row-1;
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        col = MIN;
        row = random.nextInt(max_row);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        col = max_col-1;
        row = random.nextInt(max_row);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();
    }

    /**
     * Testing cases as first dice placement of no dices positioned, more than one dice positioned, not in border and color and/or value restriction not respected
     * @throws ValueException thrown when passing wrong values to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testCheckFirstDiceException() throws ValueException, IDNotFoundException, PositionException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        assertThrows(EmptyException.class, card::checkFirstDice);

        row = random.nextInt(max_row-2)+1;
        col = random.nextInt(max_col-2)+1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = MIN;
        col = random.nextInt(max_col);
        do {
            color = Colors.random();
        } while (color.equals(Colors.WHITE) || color.equals(card.getWindow().getCell(row,col).getColor()));
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = MIN;
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = MIN;
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        row = random.nextInt(max_row-2)+1;
        col = random.nextInt(max_col-2)+1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

    }

    /**
     * Testing if only one dice is positioned
     * @throws EmptyException thrown when checking an empty window card
     * @throws WrongPositionException thrown when a dice is found in an incorrect position
     * @throws ValueException thrown when passing wrong values to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testCheckOneDice() throws EmptyException, WrongPositionException, IDNotFoundException, NotEmptyException, ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        row = random.nextInt(max_row);
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkOneDice());

        card.getWindow().getCell(row,col).freeCell();
    }

    /**
     * Testing cases no dice are positioned and color and/or value restriction not respected
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testCheckOneDiceException() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        assertThrows(EmptyException.class, card::checkOneDice);

        row = random.nextInt(max_row-2)+1;
        col = random.nextInt(max_col-2)+1;
        do {
            color = Colors.random();
        } while (color.equals(Colors.WHITE) || color.equals(card.getWindow().getCell(row,col).getColor()));
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkOneDice);

        card.getWindow().getCell(row,col).freeCell();

        row = random.nextInt(max_row);
        col = random.nextInt(max_col);
        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkOneDice);

        card.getWindow().getCell(row,col).freeCell();
    }

    /**
     * Testing the correct condition of the orthogonally positioned cells
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testCheckOrtPos() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCleanCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color = Colors.random();
        int value = random.nextInt(6)+1;
        Colors diffColor;
        int diffValue;

        do {
            diffColor = Colors.random();
        } while (diffColor.equals(Colors.WHITE) || color.equals(diffColor));

        do {
            diffValue = random.nextInt(6)+1;
        } while (value == diffValue);

        row = 0;
        col = 0;
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));
        List<Cell> orthogonal = card.getWindow().retOrthogonal(row,col);
        Cell cell = card.getWindow().getCell(row, col);

        assertTrue(card.checkOrtCol(cell, orthogonal));
        assertTrue(card.checkOrtVal(cell, orthogonal));
        assertTrue(card.checkOrtPos(cell));

        row = 0;
        col = 1;
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,diffValue));

        assertFalse(card.checkOrtCol(cell, orthogonal));
        assertTrue(card.checkOrtVal(cell, orthogonal));
        assertFalse(card.checkOrtPos(cell));

        card.getWindow().getCell(row, col).setIgnoreColor(true);
        assertTrue(card.checkOrtCol(cell, orthogonal));

        card.getWindow().getCell(row, col).setIgnoreColor(false);
        cell.setIgnoreColor(true);
        assertTrue(card.checkOrtPos(cell));

        cell.setIgnoreColor(false);
        card.getWindow().getCell(row,col).freeCell();

        card.getWindow().getCell(row,col).setDice(new Dice(id,diffColor,value));

        assertTrue(card.checkOrtCol(cell, orthogonal));
        assertFalse(card.checkOrtVal(cell, orthogonal));
        assertFalse(card.checkOrtPos(cell));

        card.getWindow().getCell(row, col).setIgnoreValue(true);
        assertTrue(card.checkOrtVal(cell, orthogonal));

        card.getWindow().getCell(row, col).setIgnoreValue(false);
        cell.setIgnoreValue(true);
        assertTrue(card.checkOrtPos(cell));

        cell.setIgnoreValue(false);

        card.getWindow().getCell(row,col).freeCell();

        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertFalse(card.checkOrtCol(cell, orthogonal));
        assertFalse(card.checkOrtVal(cell, orthogonal));
        assertFalse(card.checkOrtPos(cell));

        card.getWindow().getCell(row,col).freeCell();

        card.getWindow().getCell(row,col).setDice(new Dice(id,diffColor,diffValue));

        assertTrue(card.checkOrtCol(cell, orthogonal));
        assertTrue(card.checkOrtCol(cell, orthogonal));
        assertTrue(card.checkOrtPos(cell));

    }

    /**
     * Testing the correct condition of the cells nearby
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     */
    public void testCheckNeighbors() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        row = 0;
        col = 0;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        Cell cell = card.getWindow().getCell(row,col);
        cell.setDice(new Dice(id,color,value));

        assertFalse(card.checkNeighbors(cell));

        cell.setIgnoreNearby(true);
        assertTrue(card.checkNeighbors(card.getWindow().getCell(row,col)));
        cell.setIgnoreNearby(false);

        row = 1;
        col = 1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkNeighbors(cell));
    }

    /**
     * Testing if all dices are correctly positioned
     * @throws WrongPositionException thrown when a dice is found in an incorrect position
     * @throws ValueException thrown when passing wrong values to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testCheckPlaceCond() throws IDNotFoundException, NotEmptyException, ValueException, PositionException, WrongPositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        row = 0;
        col = 0;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        row = 1;
        col = 1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkPlaceCond());
    }

    /**
     * Testing all bad cases of placement
     * @throws ValueException thrown when passing wrong values to cell
     * @throws IDNotFoundException thrown when passing wrong id to dice constructor
     * @throws NotEmptyException thrown when trying to set a dice on an already occupied cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testCheckPlaceCondException() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row, col;
        Colors color;
        int value;

        row = 0;
        col = 0;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        row = 1;
        col = 1;
        do {
            color = Colors.random();
        } while (color.equals(card.getWindow().getCell(row,col).getColor()) || color == Colors.WHITE);
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        if (!card.getWindow().getCell(row, col).getColor().equals(Colors.WHITE))
            assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();

        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        if (card.getWindow().getCell(row, col).getValue() != 0)
            assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();

        row=1;
        col=0;
        color = card.getWindow().getCell(0,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();

        row = random.nextInt(max_row-2)+1;
        col = random.nextInt(max_col-3)+2;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);
    }

    /**
     * Testing number of empty cells
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testNumEmptyCells() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list,
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int numTotCell = 20;

        assertEquals(numTotCell, card.numEmptyCells());

        Arrays.stream(card.getWindow().getMatrix())
                .forEach(cellArr -> Arrays.stream(cellArr)
                .forEach(cell -> {
                    Colors color = cell.getColor();
                    int value = cell.getValue();

                    try {
                        cell.setDice(new Dice(id, color, value));
                    } catch (NotEmptyException | IDNotFoundException e) {
                        out.println(e.getMessage());
                    }

                    assertEquals(numTotCell - (cell.getRow()*5+cell.getCol() + 1), card.numEmptyCells());
                }));
    }

}