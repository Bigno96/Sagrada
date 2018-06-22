package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.ToolCardParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolEffectRealization;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BoardTest extends TestCase {

    private int nPlayer = 2;
    private static final Random random = new Random();
    private int idPublic = random.nextInt(10)+1;
    private int idTool = random.nextInt(12)+1;

    public BoardTest(String testName){
        super(testName);
    }

    /**
     * Testing getter of board class
     * @throws IDNotFoundException thrown by dice bag constructor inside the board constructor
     */
    public void testGetter() throws IDNotFoundException {
        Board board = new Board(nPlayer);

        assertNotNull(board.getPublicObj());
        assertEquals(nPlayer, board.getNumberPlayer());
        assertNotNull(board.getToolCard());
        assertNotNull(board.getDiceBag());
        assertNotNull(board.getDraft());
        assertNotNull(board.getRoundTrack());
    }

    /**
     * Testing setting public objective
     * @throws IDNotFoundException thrown by dice bag constructor inside the board constructor and by makeObjectiveCArd
     * @throws FileNotFoundException thrown by the parser
     */
    public void testSetPublicObj() throws IDNotFoundException, FileNotFoundException {
        Board board = new Board(nPlayer);
        List<ObjectiveCard> list = new ArrayList<>();
        PublicObjectiveCardParser publicParser = (PublicObjectiveCardParser) ParserManager.getPublicCardParser();

        ObjectiveCard obj1 = publicParser.makeObjectiveCard(idPublic);
        ObjectiveCard obj2 = publicParser.makeObjectiveCard((idPublic +1)%10+1);
        ObjectiveCard obj3 = publicParser.makeObjectiveCard((idPublic +2)%10+1);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        board.setPublicObj(obj1, obj2, obj3);

        IntStream.range(0, 3).forEach(i -> assertSame(board.getPublicObj().get(i).getId(), list.get(i).getId()));
    }

    /**
     * Testing setting tool card
     * @throws IDNotFoundException thrown by dice bag constructor inside the board constructor
     * @throws FileNotFoundException thrown by the parser
     */
    public void testSetToolCard() throws IDNotFoundException, FileNotFoundException {
        Board board = new Board(nPlayer);
        Game game = new Game();
        game.startGame();
        List<ToolCard> list = new ArrayList<>();
        ToolCardParser toolParser = (ToolCardParser) ParserManager.getToolCardParser();

        ToolCard tool1 = toolParser.makeToolCard(idTool, game);
        ToolCard tool2 = toolParser.makeToolCard((idTool+1)%12+1, game);
        ToolCard tool3 = toolParser.makeToolCard((idTool+2)%12+1, game);

        list.add(tool1);
        list.add(tool2);
        list.add(tool3);
        board.setToolCard(tool1, tool2, tool3);

        IntStream.range(0, 3).forEach(i -> assertSame(board.getToolCard().get(i).getId(), list.get(i).getId()));
    }

}
