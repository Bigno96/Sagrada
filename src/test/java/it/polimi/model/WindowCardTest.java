package it.polimi.model;


import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class WindowCardTest extends TestCase{

    private List<Cell> myList() {
        List<Cell> cellList = new ArrayList<Cell>();
        for (int i=0; i<20; i++)
            cellList.add(new Cell(0, Cell.color.VUOTO));
        return cellList;
    }

    public WindowCardTest(String testName){
        super(testName);
    }

    WindowCard card1 = new WindowCard(1, "Try1", 3, myList());
    WindowCard card2 = new WindowCard(2, "Try2", 4, myList());
    WindowCard card3 = new WindowCard(3, "Try3", 5, myList());

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

    public void testToString() {
        assertEquals("it.polimi.model.WindowCard@ ID: 1 Name: Try1 NumFavPoints: 3", card1.toString());
    }

    public void testDump()
    {
        assertEquals( "ID: 2 Name: Try2 NumFavPoints: 4", card2.dump() );
    }
}