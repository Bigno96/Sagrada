package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class ObjectiveFactoryTest  extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);
    private static final Logger logger = Logger.getLogger(ObjectiveFactoryTest.class.getName());

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

        } catch (IDNotFoundException | FileNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    public void testNegativePriv() {
        try {
            ObjectiveCard privObj = objFact.getPrivCard(19);

            assertSame(19, privObj.getId());
            assertEquals("Shades of Yellow", privObj.getDescr());
            
        } catch (IDNotFoundException | FileNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    public void testNegativePubl() {
        try {
            ObjectiveCard publObj = objFact.getPublCard(31);

            assertSame(31, publObj.getId());
            assertEquals("Row Shade Variety", publObj.getDescr());
            assertSame(0, publObj.getPoint());

        } catch (IDNotFoundException | FileNotFoundException e) {
            logger.info(e.getMessage());
        }
    }


}
