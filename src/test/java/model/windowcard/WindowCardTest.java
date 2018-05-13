package model.windowcard;

import exception.*;
import junit.framework.TestCase;
import model.Colors;
import model.dicebag.Dice;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowCardTest extends TestCase{


    private static final Random random = new Random();
    private int id = random.nextInt(20);
    private int fp = random.nextInt(4)+3;

    public WindowCardTest(String testName) {
        super(testName);
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
        MatrixCell matrix = card.getWindow();

        assertEquals(id, card.getId());
        assertEquals("Test", card.getName());
        assertEquals(fp, card.getNumFavPoint());
        assertEquals(matrix, card.getWindow());
        assertNotSame(card.toString(), card.getWindow().toString());
    }

    public void testCheckFirstDice() throws EmptyException, WrongPositionException, ValueException, IDNotFoundException, NotEmptyException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int row, col;
        Colors color;
        int value;

        row = 0;
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        row = 3;
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        col = 0;
        row = random.nextInt(4);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();

        col = 4;
        row = random.nextInt(4);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkFirstDice());

        card.getWindow().getCell(row,col).freeCell();
    }

    public void testCheckFirstDiceException() throws ValueException, IDNotFoundException, PositionException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int row, col;
        Colors color;
        int value;

        assertThrows(EmptyException.class, card::checkFirstDice);

        row = random.nextInt(2)+1;
        col = random.nextInt(3)+1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = 0;
        col = random.nextInt(5);
        do {
            color = Colors.random();
        } while (color.equals(card.getWindow().getCell(row,col).getColor()));
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = 0;
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

        row = 0;
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        row = random.nextInt(2)+1;
        col = random.nextInt(3)+1;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkFirstDice);

        card.getWindow().getCell(row,col).freeCell();

    }
    
    public void testCheckOneDice() throws EmptyException, WrongPositionException, IDNotFoundException, NotEmptyException, ValueException, PositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int row, col;
        Colors color;
        int value;

        row = random.nextInt(4);
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertTrue(card.checkOneDice());

        card.getWindow().getCell(row,col).freeCell();
    }

    public void testCheckOneDiceException() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        int row, col;
        Colors color;
        int value;

        assertThrows(EmptyException.class, card::checkOneDice);

        row = random.nextInt(4);
        col = random.nextInt(5);
        do {
            color = Colors.random();
        } while (color.equals(card.getWindow().getCell(row,col).getColor()));
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkOneDice);

        card.getWindow().getCell(row,col).freeCell();

        row = random.nextInt(4);
        col = random.nextInt(5);
        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkOneDice);

        card.getWindow().getCell(row,col).freeCell();
    }


    public void testCheckPlaceCond() throws IDNotFoundException, NotEmptyException, ValueException, PositionException, EmptyException, WrongPositionException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
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

    public void testCheckPlaceCondException() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
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
        } while (color.equals(card.getWindow().getCell(row,col).getColor()));
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();

        color = card.getWindow().getCell(row,col).getColor();
        do {
            value = random.nextInt(6)+1;
        } while (value == card.getWindow().getCell(row,col).getValue());
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();

        row=1;
        col=0;
        color = card.getWindow().getCell(0,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);

        card.getWindow().getCell(row,col).freeCell();


        row = random.nextInt(2)+1;
        col = random.nextInt(2)+2;
        color = card.getWindow().getCell(row,col).getColor();
        value = card.getWindow().getCell(row,col).getValue();
        card.getWindow().getCell(row,col).setDice(new Dice(id,color,value));

        assertThrows(WrongPositionException.class, card::checkPlaceCond);
    }

}