package model;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

public class PlayerTest extends TestCase {

    private Board board;

    {
        try {
            board = new Board(4);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    private Player player1 = new Player(1, board);
    private Player player2 = new Player(2, board);
    private WindowFactory winFact = new WindowFactory();
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public PlayerTest(String testName){
        super(testName);
    }


    public void testIsFirstTurn(){
        assertTrue(player1.isFirstTurn());
    }

    public void testEndFirstTurn(){
        player1.endFirstTurn();
        assertFalse(player1.isFirstTurn());
    }

    public void testResetFirstTurn(){
        player1.resetFirstTurn();
        assertTrue(player1.isFirstTurn());
    }


    public void testSetFavor1(){
        assertEquals(0, player1.getFavorPoint());
    }

    public void testSetFavor2(){
        player1.setFavorPoint(1);
        assertEquals(1, player1.getFavorPoint());
    }

    public void testGetId(){
        assertEquals(1, player1.getId());
        assertEquals(2, player2.getId());
    }

    public void testWindCard() throws ValueException, PositionException {
        int x = 2;
        int y = 4;
        try {
            List<WindowCard> winCardList = winFact.getWindow(x, y);
            WindowCard windCard = winCardList.get(0);
            assertSame(null, player1.getWind());
            player1.setWind(windCard);
            assertSame(windCard, player1.getWind());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

}