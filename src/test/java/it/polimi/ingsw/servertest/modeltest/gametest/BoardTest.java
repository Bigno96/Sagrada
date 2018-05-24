package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolStrategy;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveStrategy;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardTest extends TestCase {

    private int nPlayer = 2;
    private static final Random random = new Random();
    private int id = random.nextInt(5);
    private int point = random.nextInt(3)+3;

    public BoardTest(String testName){
        super(testName);
    }

    public void testGetter() throws IDNotFoundException {
        Board board = new Board(nPlayer);

        assertNotNull(board.getObjectiveStrategy());
        assertNotNull(board.getObjectiveFactory());
        assertNotNull(board.getPublObj());
        assertEquals(nPlayer, board.getnPlayer());
        assertNotNull(board.getToolCard());
        assertNotNull(board.getDiceBag());
        assertNotNull(board.getDraft());
        assertNotNull(board.getRoundTrack());
        assertNotNull(board.getWindowFactory());

    }

    public void testSetPublObj() throws IDNotFoundException{
        Board board = new Board(nPlayer);
        List<PublicObjective> list = new ArrayList<>();
        ObjectiveStrategy objStrat = new ObjectiveStrategy();
        PublicObjective obj1 = new PublicObjective(id, "Obj1", point, objStrat);
        PublicObjective obj2 = new PublicObjective(id+1, "Obj2", point+1, objStrat);
        PublicObjective obj3 = new PublicObjective(id+2, "Obj3", point-1, objStrat);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        board.setPublObj(obj1, obj2, obj3);

        assertEquals(list, board.getPublObj());
    }

    public void testSetToolCard() throws IDNotFoundException{
        Board board = new Board(nPlayer);
        List<ToolCard> list = new ArrayList<>();
        ToolStrategy objStrat = new ToolStrategy(board.getRoundTrack(), board.getDraft(), board.getDiceBag());
        ToolCard obj1 = new ToolCard(id, "Obj1", Colors.random(), objStrat);
        ToolCard obj2 = new ToolCard(id+1, "Obj2", Colors.random(), objStrat);
        ToolCard obj3 = new ToolCard(id+2, "Obj3", Colors.random(), objStrat);

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        board.setToolCard(obj1, obj2, obj3);

        assertEquals(list, board.getToolCard());
    }

}
