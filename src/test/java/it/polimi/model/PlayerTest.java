package it.polimi.model;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {

    private Player player1 = new Player(1);
    private Player player2 = new Player(2);

    public PlayerTest(String testName){
        super(testName);
    }

    public void testGetId(){
        assertEquals(1, player1.getID());
        assertEquals(2, player2.getID());
    }

    public void testFavorPoint(){
        player1.setFavorPoint(1);
        assertEquals(1, player1.getFavorPoint());
    }

}
