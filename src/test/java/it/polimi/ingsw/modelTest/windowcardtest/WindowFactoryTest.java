package it.polimi.ingsw.modelTest.windowcardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import junit.framework.TestCase;
import it.polimi.ingsw.model.windowcard.WindowCard;
import it.polimi.ingsw.model.windowcard.WindowFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowFactoryTest extends TestCase {

    private WindowFactory winFact = new WindowFactory();
    private List<WindowCard> winCards = new ArrayList<>();
    private static final Random random = new Random();

    public WindowFactoryTest(String testName) {
        super(testName);
    }

    public void testGetWindow() throws ValueException, PositionException, FileNotFoundException, IDNotFoundException {
        int x = random.nextInt(12)+1;
        int y = random.nextInt(12)+1;

        winCards = winFact.getWindow(x, y);
        assertSame(x, winCards.get(0).getId());
        assertSame(x, winCards.get(1).getId());
        assertSame(y, winCards.get(2).getId());
        assertSame(y, winCards.get(3).getId());
    }

    public void testException() {
        int x = random.nextInt()+13;
        int y = random.nextInt()+13;

        assertThrows(IDNotFoundException.class, () -> winCards = winFact.getWindow(x, y));
    }
}