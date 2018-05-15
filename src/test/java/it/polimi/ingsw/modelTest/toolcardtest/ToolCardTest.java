package it.polimi.ingsw.modelTest.toolcardtest;

import junit.framework.TestCase;
import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.toolcard.ToolCard;

import java.util.Random;

public class ToolCardTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(12)+1;
    private Colors col = Colors.random();

    public ToolCardTest(String testName) {
        super(testName);
    }

    public void testGetter() {
        ToolCard tool = new ToolCard(id, "Test", col);

        assertSame(id, tool.getId());
        assertSame("Test", tool.getName());
        assertSame(col, tool.getDiceColor());
    }

}
