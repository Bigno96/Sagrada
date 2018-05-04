import exception.IDNotFoundException;
import it.polimi.model.WindowFactory;
import junit.framework.TestCase;

public class WindowFactoryTest extends TestCase {

    private WindowFactory winFact = new WindowFactory();

    public WindowFactoryTest(String testName) {
        super(testName);
    }

    public void testGetWindow() {
        try {
            winFact.getWindow(1,2);
        } catch (IDNotFoundException e) {
            e.printStackTrace();
        }
    }
}
