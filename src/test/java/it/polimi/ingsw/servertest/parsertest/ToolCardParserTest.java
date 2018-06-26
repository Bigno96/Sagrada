package it.polimi.ingsw.servertest.parsertest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.ToolCardParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ToolCardParserTest extends TestCase {
    private Game game = new Game();
    private ToolCardParser toolFactory = (ToolCardParser) ParserManager.getToolCardParser();

    private static final String SYSTEM_DIR = System.getProperty("user.dir");
    private final String infoPath = SYSTEM_DIR + "/src/main/resources/Json/ToolCard.json";
    private static final String ID = "ID";
    private static final String NAME = "NAME";

    public ToolCardParserTest(String testName) {
        super(testName);
    }

    /**
     * Testing the making of all the tool cards
     * @throws FileNotFoundException thrown by reader
     */
    public void testMakeToolCard() throws FileNotFoundException {
        ToolCard tool;
        game.startGame();

        int id = 1;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        id = 2;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        id = 3;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.RED, tool.getColor());

        id = 4;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        id = 5;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        id = 6;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        id = 7;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.BLUE, tool.getColor());

        id = 8;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.RED, tool.getColor());

        id = 9;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.YELLOW, tool.getColor());

        id = 10;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.GREEN, tool.getColor());

        id = 11;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.MAGENTA, tool.getColor());

        id = 12;
        tool = toolFactory.makeToolCard(id, game);
        assertSame(id, tool.getId());
        assertEquals(findName(id), tool.getName());
        assertSame(Colors.BLUE, tool.getColor());
    }

    /**
     * Find name of tool card based on passed id
     * @param id of tool card
     * @return String name
     * @throws FileNotFoundException thrown by reader
     */
    private String findName(int id) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;

            if (Integer.parseInt(obj.get(ID).toString()) == id)
                return obj.get(NAME).getAsString();
        }

        return null;
    }
}
