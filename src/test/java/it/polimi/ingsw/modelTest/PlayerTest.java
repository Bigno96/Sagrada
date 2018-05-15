package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import junit.framework.TestCase;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.objectivecard.ObjectiveFactory;
import it.polimi.ingsw.model.objectivecard.ObjectiveStrategy;
import it.polimi.ingsw.model.windowcard.WindowCard;
import it.polimi.ingsw.model.windowcard.WindowFactory;

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

        assertNotSame(winCard.toString(), p.toString());
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
        Player p = new Player(id, board);

        assertFalse(p.isPlayedDice());
        p.playDice();
        assertTrue(p.isPlayedDice());
        p.resetPlayedDice();
        assertFalse(p.isPlayedDice());
    }

    public void testUseTool() {
        Player p = new Player(id, board);

        assertFalse(p.isUsedTool());
        p.useTool();
        assertTrue(p.isUsedTool());
        p.resetUsedTool();
        assertFalse(p.isUsedTool());
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