package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolEffectRealization;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardTest extends TestCase {

    private int nPlayer = 2;
    private static final Random random = new Random();
    private int idPubl = random.nextInt(10)+1;
    private int idTool = random.nextInt(12)+1;

    public BoardTest(String testName){
        super(testName);
    }

    public void testGetter() throws IDNotFoundException {
        Board board = new Board(nPlayer);

        assertNotNull(board.getPublObj());
        assertEquals(nPlayer, board.getnPlayer());
        assertNotNull(board.getToolCard());
        assertNotNull(board.getDiceBag());
        assertNotNull(board.getDraft());
        assertNotNull(board.getRoundTrack());

    }

    public void testSetPublObj() throws IDNotFoundException, FileNotFoundException {
        Board board = new Board(nPlayer);
        List<ObjectiveCard> list = new ArrayList<>();
        PublicObjectiveCardParser publicParser = (PublicObjectiveCardParser) ParserManager.getPublicCardParser();
        ObjectiveCard obj1 = publicParser.makeObjectiveCard(idPubl);
        ObjectiveCard obj2 = publicParser.makeObjectiveCard((idPubl+1)%10+1);
        ObjectiveCard obj3 = publicParser.makeObjectiveCard((idPubl+2)%10+1);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        board.setPublObj(obj1, obj2, obj3);

        assertEquals(list, board.getPublObj());
    }

    public void testSetToolCard() throws IDNotFoundException{
        Board board = new Board(nPlayer);
        List<ToolCard> list = new ArrayList<>();
        ToolEffectRealization objStrat = new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());
        ToolCard obj1 = new ToolCard(idTool, "Obj1", Colors.random(), objStrat);
        ToolCard obj2 = new ToolCard((idTool+1)%12+1, "Obj2", Colors.random(), objStrat);
        ToolCard obj3 = new ToolCard((idTool+2)%12+1, "Obj3", Colors.random(), objStrat);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        board.setToolCard(obj1, obj2, obj3);

        assertEquals(list, board.getToolCard());
    }

}
