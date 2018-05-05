package it.polimi.model;


import junit.framework.TestCase;

public class CellTest extends TestCase{

    private Cell cell1 = new Cell(2, Cell.colors.NULL, 1);
    private Cell cell2 = new Cell(0, Cell.colors.NULL, 2);
    private Cell cell3 = new Cell(0, Cell.colors.GREEN, 3);
    private Cell cell4 = new Cell(0, Cell.colors.NULL, 4);
    private Cell cell5 = new Cell(0, Cell.colors.NULL, 5);

    private Dice dice = new Dice( 1, Dice.colors.YELLOW );
    private Dice dice2 = new Dice( 2, Dice.colors.BLUE );

    public CellTest(String testName){
        super (testName);
    }

    public void testChangeColor()
    {
        cell4.changeColor( Cell.colors.RED);
        assertEquals( Cell.colors.RED, cell4.getColor() );
    }

    public void testGetColor() {
        assertEquals(Cell.colors.GREEN, cell3.getColor());
    }

    public void testChangeValue()
    {
        cell5.changeValue( 6);
        assertEquals( 6, cell5.getValue() );
    }

    public void testGetValue() {
        assertEquals(2, cell1.getValue());
    }

    public void testIsOccupied(){
        cell1.setDice(dice2);
        assertTrue(cell1.isOccupied());
    }

    public void testSetDice(){
        cell2.setDice(dice);
        assertEquals(dice, cell2.getDice());
    }

    public void testGetDice(){
        cell2.setDice(dice);
        assertEquals(dice, cell2.getDice());
    }

    public void testGetPos() {
        assertSame(1, cell1.getPos());
    }

    public void testIsBorder() {
        assertTrue(cell1.isBorder());
    }

}