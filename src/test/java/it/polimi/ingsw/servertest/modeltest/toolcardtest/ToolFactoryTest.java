package it.polimi.ingsw.servertest.modeltest.toolcardtest;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.ToolCardParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

public class ToolFactoryTest extends TestCase {

    private Game game = new Game();
    private ToolCardParser toolFactory = (ToolCardParser) ParserManager.getToolCardParser();

    public ToolFactoryTest(String testName) {
        super(testName);
    }

    public void testMakeToolCard() throws FileNotFoundException {
        ToolCard tool;
        game.startGame();

        tool = toolFactory.makeToolCard(1, game);
        assertSame(1, tool.getId());
        assertEquals("Grozing Pliers", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(2, game);
        assertSame(2, tool.getId());
        assertEquals("Eglomise Brush", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        tool = toolFactory.makeToolCard(3, game);
        assertSame(3, tool.getId());
        assertEquals("Copper Foil Burnisher", tool.getName());
        assertSame(Colors.RED, tool.getColor());

        tool = toolFactory.makeToolCard(4, game);
        assertSame(4, tool.getId());
        assertEquals("Lathekin", tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        tool = toolFactory.makeToolCard(5, game);
        assertSame(5, tool.getId());
        assertEquals("Lens Cutter", tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        tool = toolFactory.makeToolCard(6, game);
        assertSame(6, tool.getId());
        assertEquals("Flux Brush", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(7, game);
        assertSame(7, tool.getId());
        assertEquals("Glazing Hammer", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        tool = toolFactory.makeToolCard(8, game);
        assertSame(8, tool.getId());
        assertEquals("Running Pliers", tool.getName());
        assertSame(Colors.RED, tool.getColor());

        tool = toolFactory.makeToolCard(9, game);
        assertSame(9, tool.getId());
        assertEquals("Cork-backed Straightedge", tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        tool = toolFactory.makeToolCard(10, game);
        assertSame(10, tool.getId());
        assertEquals("Grinding Stone", tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        tool = toolFactory.makeToolCard(11, game);
        assertSame(11, tool.getId());
        assertEquals("Flux Remover", tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        tool = toolFactory.makeToolCard(12, game);
        assertSame(12, tool.getId());
        assertEquals("Tap Wheel", tool.getName());
        assertSame(Colors.BLUE, tool.getColor());
    }
}
