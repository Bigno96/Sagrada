package it.polimi.ingsw.servertest.modeltest.toolcardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolFactory;
import it.polimi.ingsw.server.model.toolcard.ToolStrategy;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

public class ToolFactoryTest extends TestCase {

    private Game game = new Game();
    private DiceBag diceBag = new DiceBag();
    private Draft draft = new Draft(diceBag, 9);
    private RoundTrack roundTrack = new RoundTrack(draft);
    private ToolStrategy toolStrategy = new ToolStrategy(roundTrack, draft, diceBag);
    private ToolFactory toolFactory = new ToolFactory(toolStrategy, game);

    public ToolFactoryTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testMakeToolCard() throws FileNotFoundException, IDNotFoundException {
        ToolCard tool;
        game.startGame();

        tool = toolFactory.makeToolCard(1);
        assertSame(1, tool.getId());
        assertEquals("Grozing Pliers", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(2);
        assertSame(2, tool.getId());
        assertEquals("Eglomise Brush", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        tool = toolFactory.makeToolCard(3);
        assertSame(3, tool.getId());
        assertEquals("Copper Foil Burnisher", tool.getName());
        assertSame(Colors.RED, tool.getColor());

        tool = toolFactory.makeToolCard(4);
        assertSame(4, tool.getId());
        assertEquals("Lathekin", tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        tool = toolFactory.makeToolCard(5);
        assertSame(5, tool.getId());
        assertEquals("Lens Cutter", tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        tool = toolFactory.makeToolCard(6);
        assertSame(6, tool.getId());
        assertEquals("Flux Brush", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(7);
        assertSame(7, tool.getId());
        assertEquals("Glazing Hammer", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        tool = toolFactory.makeToolCard(8);
        assertSame(8, tool.getId());
        assertEquals("Running Pliers", tool.getName());
        assertSame(Colors.RED, tool.getColor());

        tool = toolFactory.makeToolCard(9);
        assertSame(9, tool.getId());
        assertEquals("Cork-backed Straightedge", tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        tool = toolFactory.makeToolCard(10);
        assertSame(10, tool.getId());
        assertEquals("Grinding Stone", tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        tool = toolFactory.makeToolCard(11);
        assertSame(11, tool.getId());
        assertEquals("Flux Remover", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(12);
        assertSame(12, tool.getId());
        assertEquals("Tap Wheel", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());
    }
}
