package it.polimi.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class PlayerTest extends TestCase {

    private Player player1 = new Player(1);
    private Player player2 = new Player(2);
    private WindowFactory winFact = new WindowFactory();


    public PlayerTest(String testName){
        super(testName);
    }

    public void testGetId() {
        assertEquals(1, player1.getID());
        assertEquals(2, player2.getID());
    }

    public void testFavorPoint() {
        player1.setFavorPoint(1);
        assertEquals(1, player1.getFavorPoint());
    }

    public void testWindCard() {
        int x = 2;
        int y = 4;
        List<WindowCard> winCardList = winFact.getWindow(x,y);
        WindowCard windCard =  winCardList.get(0);
        assertSame(null, player1.getWind());
        player1.setWind(windCard);
        assertSame(windCard, player1.getWind());
    }
}
