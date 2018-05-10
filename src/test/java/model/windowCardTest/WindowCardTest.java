package model.windowCardTest;

import exception.*;
import junit.framework.TestCase;
import model.Colors;
import model.diceBag.Dice;
import model.windowCard.Cell;
import model.windowCard.MatrixCell;
import model.windowCard.WindowCard;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowCardTest extends TestCase{


    private static final Random random = new Random();
    private int id = random.nextInt(20);
    private int fp = random.nextInt(4)+3;
    private MatrixCell matrix = new MatrixCell(4,5);


    public WindowCardTest(String testName) {
        super(testName);
    }

    private List<Cell> myCellList() throws ValueException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<20; i++)
            cellList.add(new Cell(random.nextInt(7), Colors.random()));
        return cellList;
    }

    public void testGetter() throws ValueException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        matrix = card.getWindow();

        assertEquals(id, card.getId());
        assertEquals("Test", card.getName());
        assertEquals(fp, card.getNumFavPoint());
        assertEquals(matrix, card.getWindow());
    }

    public void testArrayIndexOutOfBoundsException() throws ValueException {
        int rowNeg = random.nextInt(1) - (random.nextInt()+1);
        int rowPos = random.nextInt(1)+4;
        int colNeg = random.nextInt(1) - (random.nextInt()+1);
        int colPos = random.nextInt(1)+5;
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.getWindow().getCell(rowNeg, colNeg));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.getWindow().getCell(rowPos, colPos));
    }

    /*public void testCheckFirstDice() throws EmptyException, WrongPositionException, ValueException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int pos = 3;

        Colors color = list.get(pos).getColor();
        int value = list.get(pos).getValue();

        card.getWindow().getCell(0,3).setDice(new Dice(id, color));

        card.getWindow().getCell(1,4).setDice(new Dice(id, color));

        assertTrue(card.checkFirstDice());
    }
    
    /*public void testCheckOneDice() throws EmptyException, WrongPositionException, IDNotFoundException, NotEmptyException, ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        Colors color = list.get(id).getColor();
        int value = list.get(id).getValue();

        card.getCell(id).setDice(new Dice(id, color));

        Dice d = card.getCell(id).getDice();
        d.changeValue(value);
        card.getCell(id).freeCell();
        card.getCell(id).setDice(d);

        assertTrue(card.checkOneDice());
    }*/



    /*public void testWrongPositionException() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int pos = 4;
        Colors color = list.get(pos).getColor();
        int value = list.get(pos).getValue();
        Colors wrongCol;
        int wrongVal;
        int wrongPos = 12;

        do {
            wrongCol = Colors.random();
        } while (wrongCol.equals(color));

        do {
            wrongVal = random.nextInt(7);
        } while (wrongVal == value);

        card.getCell(pos).setDice(new Dice(id, wrongCol));

        assertThrows(WrongPositionException.class, card::checkOneDice);
        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getCell(pos).freeCell();
        card.getCell(pos).setDice(new Dice(id, color));
        Dice d = card.getCell(pos).getDice();
        d.changeValue(wrongVal);
        card.getCell(pos).freeCell();
        card.getCell(pos).setDice(d);

        assertThrows(WrongPositionException.class, card::checkOneDice);
        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getCell(pos).freeCell();
        color = list.get(wrongPos).getColor();
        card.getCell(wrongPos).setDice(new Dice(id, color));

        assertThrows(WrongPositionException.class, card::checkFirstDice);
    }*/

    /*public void testEmptyException() throws ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);

        assertThrows(EmptyException.class, card::checkOneDice);
        assertThrows(EmptyException.class, card::checkFirstDice);
    }*/

    /*public void testCheckPlaceCond() throws WrongPositionException, EmptyException, IDNotFoundException, ValueException, NotEmptyException, PositionException {
        List<Cell> list = new ArrayList<>();
        list.add(new Cell(0, Colors.GREEN, 0));
        list.add(new Cell(0, Colors.BLUE, 1));
        for (int i=2; i<5; i++)
            list.add(new Cell(0, Colors.NULL, i));
        list.add(new Cell(1, Colors.NULL, 5));
        for (int i=6; i<20; i++)
            list.add(new Cell(0, Colors.NULL, i));

        WindowCard card = new WindowCard(id, "Test", fp, list);
        Dice d;

        card.getCell(1).setDice(new Dice(8, Colors.BLUE));

        d = card.getCell(1).getDice();
        d.changeValue(6);
        card.getCell(1).freeCell();
        card.getCell(1).setDice(d);

        assertTrue(card.checkPlaceCond());

        card.getCell(0).setDice(new Dice(0, Colors.GREEN));

        d = card.getCell(0).getDice();
        d.changeValue(3);
        card.getCell(0).freeCell();
        card.getCell(0).setDice(d);

        assertTrue(card.checkPlaceCond());

        card.getCell(5).setDice(new Dice(3, Colors.NULL));

        d = card.getCell(5).getDice();
        d.changeValue(1);
        card.getCell(5).freeCell();
        card.getCell(5).setDice(d);

        assertTrue(card.checkPlaceCond());

        d = card.getCell(0).getDice();
        d.changeValue(1);
        card.getCell(0).freeCell();
        card.getCell(0).setDice(d);

        assertFalse(card.checkPlaceCond());

        card.getCell(5).freeCell();
        card.getCell(5).setDice(new Dice(2, Colors.GREEN));

        assertFalse(card.checkPlaceCond());
    }*/

}