package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.objectivecard.*;
import it.polimi.ingsw.server.model.windowcard.Cell;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerTest extends TestCase {

    private static final Random random = new Random();
    private int nPlayer = random.nextInt(3)+2;
    private int id = random.nextInt(100);
    private int idCard = random.nextInt(5)+2;
    private Board board = new Board(nPlayer);
    private WindowFactory winFact = new WindowFactory();
    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);
    private int fp = random.nextInt(4)+3;
    private int point = random.nextInt(3)+3;

    public PlayerTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testWindowCard() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        Player p = new Player(id);
        p.setBoard(board);
        int pick = random.nextInt(4);
        int idCard1 = random.nextInt(12)+1;
        int idCard2 = (idCard1+1)%12 +1;

        List<WindowCard> windows = winFact.getWindow(idCard1, idCard2);
        WindowCard winCard = windows.get(pick);

        p.setWindowCard(winCard);
        assertSame(winCard, p.getWindowCard());

        assertNotSame(winCard.toString(), p.toString());
    }

    public void testGetID() {
        Player p = new Player(id);
        p.setBoard(board);

        assertSame(id, p.getId());
    }

    public void testGetBoard() {
        Player p = new Player(id);
        p.setBoard(board);

        assertSame(board, p.getBoard());
    }

    public void testTurn() {
        Player p = new Player(id);
        p.setBoard(board);

        assertTrue(p.isFirstTurn());
        p.endFirstTurn();
        assertFalse(p.isFirstTurn());
        p.resetFirstTurn();
        assertTrue(p.isFirstTurn());

        assertTrue(p.isSecondTurn());
        p.endSecondTurn();
        assertFalse(p.isSecondTurn());
        p.resetSecondTurn();
        assertTrue(p.isSecondTurn());
    }

    public void testPlayDice() {
        Player p = new Player(id);
        p.setBoard(board);

        assertFalse(p.isPlayedDice());
        p.playDice();
        assertTrue(p.isPlayedDice());
        p.resetPlayedDice();
        assertFalse(p.isPlayedDice());
    }

    public void testUseTool() {
        Player p = new Player(id);
        p.setBoard(board);

        assertFalse(p.isUsedTool());
        p.useTool();
        assertTrue(p.isUsedTool());
        p.resetUsedTool();
        assertFalse(p.isUsedTool());
    }

    public void testPrivObj() throws FileNotFoundException, IDNotFoundException {
        Player p = new Player(id);
        p.setBoard(board);
        int n = random.nextInt(5)+1;

        ObjectiveCard obj = objFact.getPrivCard(n);

        p.setPrivObj(obj);
        assertSame(obj, p.getPrivObj());
    }

    public void testFavorPoint() {
        Player p = new Player(id);
        p.setBoard(board);
        int fp = random.nextInt(7);

        p.setFavorPoint(fp);
        assertSame(fp, p.getFavorPoint());
    }

    private List<Cell> myCellList() throws ValueException, PositionException {
        Colors col;
        int val;
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++) {
                do {
                    col = Colors.random();
                } while (col == Colors.NULL);
                val = random.nextInt(6)+1;
                cellList.add(new Cell(val, col, i, j));
            }
        return cellList;
    }

    public void testRateScore() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        int score = -20;
        List<Cell> list = myCellList();
        WindowCard card = new WindowCard(id, "Test", fp, list);
        ObjectiveStrategy objStrat = new ObjectiveStrategy();
        ObjectiveFactory obj = new ObjectiveFactory(objStrat);
        ObjectiveCard objPriv = obj.getPrivCard(idCard-1);
        ObjectiveCard obj1 = obj.getPublCard(idCard);
        ObjectiveCard obj2 = obj.getPublCard(idCard+1);
        ObjectiveCard obj3 = obj.getPublCard(idCard+2);
        board.setPublObj(obj1, obj2, obj3);

        Player p = new Player(id);
        p.setWindowCard(card);
        p.setBoard(board);
        p.setPrivObj(objPriv);
        p.setFavorPoint(0);

        assertEquals(score, p.rateScore());
    }
}