package model;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

public class PlayerTest extends TestCase {

    private static final Random random = new Random();
    private int nPlayer = random.nextInt(3)+2;
    private int id = random.nextInt(100);
    private Board board = new Board(nPlayer);
    private WindowFactory winFact = new WindowFactory();
    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);

    public PlayerTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testWindowCard() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        Player p = new Player(id, board);
        int pick = random.nextInt(4);
        int idCard1 = random.nextInt(12)+1;
        int idCard2;

        if (idCard1+1 > 12)
            idCard2 = 1;
        else
            idCard2 = idCard1 +1;

        List<WindowCard> windows = winFact.getWindow(idCard1, idCard2);
        WindowCard winCard = windows.get(pick);

        p.setWindowCard(winCard);
        assertSame(winCard, p.getWindowCard());
    }

    public void testGetID() {
        Player p = new Player(id, board);

        assertSame(id, p.getId());
    }

    public void testGetBoard() {
        Player p = new Player(id, board);

        assertSame(board, p.getBoard());
    }

    public void testTurn() {
        Player p = new Player(id, board);

        assertTrue(p.isTurn());
        p.endTurn();
        assertFalse(p.isTurn());
        p.resetTurn();
        assertTrue(p.isTurn());
    }

    public void testPrivObj() throws FileNotFoundException, IDNotFoundException {
        Player p = new Player(id, board);
        int n = random.nextInt(5)+1;

        ObjectiveCard obj = objFact.getPrivCard(n);

        p.setPrivObj(obj);
        assertSame(obj, p.getPrivObj());
    }

    public void testFavorPoint() {
        Player p = new Player(id, board);
        int fp = random.nextInt(7);

        p.setFavorPoint(fp);
        assertSame(fp, p.getFavorPoint());
    }

}