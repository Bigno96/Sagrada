package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class ObjectiveFactoryTest  extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public ObjectiveFactoryTest(String testName) {
        super(testName);
    }

    public void testGetObjCard() {
        try {
            ObjectiveCard privObj = objFact.getPrivCard(1);
            ObjectiveCard publObj = objFact.getPublCard(3);

            assertSame(1, privObj.getId());
            assertEquals("Shades of Yellow", privObj.getDescr());

            assertSame(3, publObj.getId());
            assertEquals("Row Shade Variety", publObj.getDescr());
            assertSame(5, publObj.getPoint());

        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        }

    }
}
