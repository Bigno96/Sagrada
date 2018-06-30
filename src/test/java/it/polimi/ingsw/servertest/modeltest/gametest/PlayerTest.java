package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PrivateObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.parser.gamedataparser.WindowParser;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayerTest extends TestCase {

    private static final Random random = new Random();
    private final int nPlayer = random.nextInt(3)+2;
    private final String id = "Test";
    private final Board board = new Board(nPlayer);

    private final PrivateObjectiveCardParser privateParser = (PrivateObjectiveCardParser) ParserManager.getPrivateCardParser();
    private final PublicObjectiveCardParser publicParser = (PublicObjectiveCardParser) ParserManager.getPublicCardParser();
    private final WindowParser winFact = (WindowParser) ParserManager.getWindowCardParser();

    public PlayerTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    /**
     * Test the correct pick of the window card by a player
     * @throws FileNotFoundException thrown by parser
     * @throws IDNotFoundException thrown by getWindow of parser
     * @throws PositionException thrown by getWindow of parser
     * @throws ValueException thrown by getWindow of parser
     */
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
    }

    /**
     * Testing getting id
     */
    public void testGetID() {
        Player p = new Player(id);
        p.setBoard(board);

        assertSame(id, p.getId());
    }

    /**
     * Testing getting board
     */
    public void testGetBoard() {
        Player p = new Player(id);
        p.setBoard(board);

        assertSame(board, p.getBoard());
    }

    /**
     * Testing the correct setting and resetting of turn indicator in player
     */
    public void testTurn() {
        Player p = new Player(id);
        p.setBoard(board);

        assertTrue(p.isFirstTurn());
        p.setFirstTurn(false);
        assertFalse(p.isFirstTurn());
        p.setFirstTurn(true);
        assertTrue(p.isFirstTurn());

        assertTrue(p.isSecondTurn());
        p.setSecondTurn(false);
        assertFalse(p.isSecondTurn());
        p.setSecondTurn(true);
        assertTrue(p.isSecondTurn());
    }

    /**
     * Testing the correct setting and resetting of placing dice indicator in player
     */
    public void testPlayDice() {
        Player p = new Player(id);
        p.setBoard(board);

        assertFalse(p.isPlayedDice());
        p.setPlayedDice(true);
        assertTrue(p.isPlayedDice());
        p.setPlayedDice(false);
        assertFalse(p.isPlayedDice());
    }

    /**
     * Testing the correct setting and resetting of using tool indicator in player
     */
    public void testUseTool() {
        Player p = new Player(id);
        p.setBoard(board);

        assertFalse(p.isUsedTool());
        p.setUsedTool(true);
        assertTrue(p.isUsedTool());
        p.setUsedTool(false);
        assertFalse(p.isUsedTool());
    }

    /**
     * Testing the correct setting of the private objective
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException thrown  by the parser
     */
    public void testPrivateObj() throws FileNotFoundException, IDNotFoundException {
        Player p = new Player(id);
        p.setBoard(board);
        int idCard = random.nextInt(5)+1;

        ObjectiveCard obj = privateParser.makeObjectiveCard(idCard);

        p.setPrivateObj(obj);
        assertSame(obj.getId(), p.getPrivateObj().getId());
    }

    /**
     * Testing the correct number of favor point
     */
    public void testFavorPoint() {
        Player p = new Player(id);
        p.setBoard(board);
        int fp = random.nextInt(7);

        p.setFavorPoint(fp);
        assertSame(fp, p.getFavorPoint());
    }

    /**
     * Testing the calculation of the score of the player
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException thrown by the parser
     * @throws ValueException thrown by the parser
     * @throws PositionException thrown by the parser
     * @throws NotEmptyException thrown by set Dice
     */
    public void testRateScore() throws FileNotFoundException, IDNotFoundException, ValueException, PositionException, NotEmptyException {
        Player p = new Player(id);
        p.setBoard(board);

        ObjectiveCard publicObj1 = publicParser.makeObjectiveCard(1);
        ObjectiveCard publicObj5 = publicParser.makeObjectiveCard(5);
        ObjectiveCard publicObj10 = publicParser.makeObjectiveCard(10);

        board.setPublicObj(publicObj1, publicObj5, publicObj10);

        ObjectiveCard privateObj = privateParser.makeObjectiveCard(4);

        p.setPrivateObj(privateObj);

        WindowCard windowCard = winFact.getWindow("Virtus");

        p.setWindowCard(windowCard);

        for (Iterator<Cell> itr = windowCard.getHorizontalItr(); itr.hasNext();) {
            Cell cell = itr.next();
            cell.setDice(new Dice(cell.getCol() + cell.getRow(), cell.getColor(), cell.getValue()));
        }

        assertSame(7, p.rateScore());
    }

    /**
     * Test setter and getter for disconnected state
     */
    public void testDisconnection() {
        Player p = new Player(id);

        p.setDisconnected(true);
        assertTrue(p.isDisconnected());

        p.setDisconnected(false);
        assertFalse(p.isDisconnected());
    }

    /**
     * Testing preparation of player for playing another turn
     */
    public void testNextTurn() {
        Player p = new Player(id);

        p.nextTurn();
        assertFalse(p.isPlayedDice());
        assertFalse(p.isUsedTool());
    }
}