package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WindowFactoryTest extends TestCase {

    private WindowFactory winFact = new WindowFactory();
    private List<WindowCard> winCards = new ArrayList<WindowCard>();
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public WindowFactoryTest(String testName) {
        super(testName);
    }

    public void testGetWindow() {
        int x = 10;
        int y = 3;

        try {
            winCards = winFact.getWindow(x, y);
            assertSame(x, winCards.get(0).getId());
            assertSame(x, winCards.get(1).getId());
            assertSame(y, winCards.get(2).getId());
            assertSame(y, winCards.get(3).getId());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
    }
}
