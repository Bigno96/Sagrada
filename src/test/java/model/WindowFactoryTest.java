package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class WindowFactoryTest extends TestCase {

    private WindowFactory winFact = new WindowFactory();
    private List<WindowCard> winCards = new ArrayList<WindowCard>();

    public WindowFactoryTest(String testName) {
        super(testName);
    }

    public void testGetWindow() {
        int x = 10;
        int y = 3;
        winCards = winFact.getWindow(x,y);
        assertSame(x, winCards.get(0).getId());
        assertSame(x, winCards.get(1).getId());
        assertSame(y, winCards.get(2).getId());
        assertSame(y, winCards.get(3).getId());
    }
}
