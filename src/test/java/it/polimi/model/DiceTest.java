package it.polimi.model;

import junit.framework.TestCase;

public class DiceTest extends TestCase {

    private Dice dice1G = new Dice( 1, Dice.color.GIALLO );
    private Dice dice2B = new Dice( 2, Dice.color.BLU );
    private Dice dice3Ve = new Dice( 3, Dice.color.VERDE );
    private Dice dice4Vi = new Dice( 4, Dice.color.VIOLA );
    private Dice dice5R = new Dice( 5, Dice.color.ROSSO );

    public DiceTest( String testName )
    {
        super( testName );
    }

    public void testGetID()
    {
        assertEquals( 1, dice1G.getID() );
    }

    public void testGetColor()
    {
        assertEquals( Dice.color.GIALLO, dice1G.getColor() );
        assertEquals( Dice.color.BLU, dice2B.getColor() );
        assertEquals( Dice.color.VERDE, dice3Ve.getColor() );
        assertEquals( Dice.color.VIOLA, dice4Vi.getColor() );
        assertEquals( Dice.color.ROSSO, dice5R.getColor() );
    }

    public void testRollDice()
    {
        dice1G.rollDice();
        assertTrue( 0 < dice1G.getValue() && 7 > dice1G.getValue() );
    }

    public void testChangeValue()
    {
        dice2B.rollDice();
        dice2B.changeValue( 3 );
        assertEquals( 3, dice2B.getValue() );
    }

    public void testToString()
    {
        assertEquals( "it.polimi.model.Dice@ ID: 5 Col: " + Dice.color.ROSSO + " Val: 0", dice5R.toString() );
        dice5R.rollDice();
        assertEquals( "it.polimi.model.Dice@ ID: 5 Col: " + Dice.color.ROSSO + " Val: " + dice5R.getValue(), dice5R.toString() );
    }

    public void testDump()
    {
        assertEquals( "ID: 5 Col: " + Dice.color.ROSSO + " Val: 0", dice5R.dump() );
        dice5R.rollDice();
        assertEquals( "ID: 5 Col: " + Dice.color.ROSSO + " Val: " + dice5R.getValue(), dice5R.dump() );
    }
}
