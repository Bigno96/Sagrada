package it.polimi.model;


import junit.framework.TestCase;

public class CellTest extends TestCase{

    private Cell cell1 = new Cell(2, Cell.color.NULL);
    private Cell cell2 = new Cell(0, Cell.color.NULL);
    private Cell cell3 = new Cell(0, Cell.color.GREEN);
    private Cell cell4 = new Cell(0, Cell.color.NULL);
    private Cell cell5 = new Cell(0, Cell.color.NULL);

    private Dice dice = new Dice( 1, Dice.color.YELLOW );
    private Dice dice2 = new Dice( 2, Dice.color.BLU );

    public CellTest(String testName){
        super (testName);
    }

    public void testChangeColor()
    {
        cell4.changeColor( Cell.color.RED);
        assertEquals( Cell.color.RED, cell4.getColor() );
    }

    public void testGetColor() {
        assertEquals(Cell.color.GREEN, cell3.getColor());
    }

    public void testChangeValue()
    {
        cell5.changeValue( 6);
        assertEquals( 6, cell5.getValue() );
    }

    public void testGetValue() {
        assertEquals(2, cell1.getValue());
    }

    public void testGetIsOccupied(){
        cell1.setDice(dice2);
        assertTrue(cell1.getIsOccupied());
    }

    public void testSetDice(){
        cell2.setDice(dice);
        assertEquals(dice, cell2.getDice());
    }

    public void testGetDice(){
        cell2.setDice(dice);
        assertEquals(dice, cell2.getDice());
    }

}