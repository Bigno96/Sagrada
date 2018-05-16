package it.polimi.ingsw.servertest.modeltest;

import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;

public class ColorsTest extends TestCase {

    public ColorsTest(String testName) {
        super(testName);
    }

    public void testRandomColors() {
        assertNotNull(Colors.random());
    }

    public void testParseColor() {
        assertSame(Colors.GREEN, Colors.parseColor("GREEN"));
        assertSame(Colors.BLUE, Colors.parseColor("BLUE"));
        assertSame(Colors.RED, Colors.parseColor("RED"));
        assertSame(Colors.VIOLET, Colors.parseColor("VIOLET"));
        assertSame(Colors.YELLOW, Colors.parseColor("YELLOW"));
        assertSame(Colors.NULL, Colors.parseColor("NULL"));
    }
}
