package model;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WindowFactoryTest extends TestCase {

    private WindowFactory winFact = new WindowFactory();
    private List<WindowCard> winCards = new ArrayList<WindowCard>();
    private int x,y;
    private static final Logger logger = Logger.getLogger(WindowFactoryTest.class.getName());

    public WindowFactoryTest(String testName) {
        super(testName);
    }

    public void testPositiveGetWindow() throws ValueException, PositionException {
        x = 10;
        y = 3;

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

    public void testNegativeGetWindow() throws ValueException, PositionException {
        x = 100;
        y= 7;

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
