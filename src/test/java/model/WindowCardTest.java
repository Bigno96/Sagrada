package model;


import exception.*;
import junit.framework.TestCase;
import java.util.*;

public class WindowCardTest extends TestCase{

    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<Cell>();
        for (int i=0; i<20; i++)
            cellList.add(new Cell(0, Colors.NULL, i));
        return cellList;
    }

    public WindowCardTest(String testName) throws ValueException, PositionException {
        super(testName);
    }

    private WindowCard card1 = new WindowCard(1, "Try1", 3, myCellList());
    private WindowCard card2 = new WindowCard(2, "Try2", 4, myCellList());
    private WindowCard card3 = new WindowCard(3, "Try3", 5, myCellList());

    public void testGetId(){
        assertEquals(1, card1.getId());
        assertEquals(2, card2.getId());
        assertEquals(3, card3.getId());
    }

    public void testGetName(){
        assertEquals("Try1", card1.getName());
        assertEquals("Try2", card2.getName());
        assertEquals("Try3", card3.getName());
    }

    public void testGetNumFavPoint(){
        assertEquals(3, card1.getNumFavPoint());
        assertEquals(4, card2.getNumFavPoint());
        assertEquals(5, card3.getNumFavPoint());
    }
    
    public void testCheckFirstDice() throws EmptyException, WrongPositionException, IDNotFoundException, NotEmptyException {
        card1.cellList.get(0).setDice(new Dice(0, Colors.GREEN));
        assertTrue(card1.checkFirstDice());
    }

    public void testCheckOneDice() throws EmptyException, IDNotFoundException, NotEmptyException {
        card1.cellList.get(0).setDice(new Dice(0, Colors.GREEN));
        assertTrue(card1.checkOneDice());
    }

    public void testCheckPlaceCond() throws WrongPositionException, EmptyException, IDNotFoundException, ValueException, NotEmptyException {
        card1.cellList.get(0).setDice(new Dice(0, Colors.GREEN));
        card1.cellList.get(0).getDice().changeValue(1);
        card1.cellList.get(1).setDice(new Dice(1, Colors.RED));
        card1.cellList.get(1).getDice().changeValue(2);
        card1.cellList.get(6).setDice(new Dice(2, Colors.GREEN));
        card1.cellList.get(6).getDice().changeValue(3);
        card2.cellList.get(0).setDice(new Dice(4, Colors.GREEN));
        assertTrue(card1.checkPlaceCond());
        assertTrue(card2.checkPlaceCond());
    }
}