package model;

import junit.framework.TestCase;

public class ObjectiveFactoryTest  extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);

    public ObjectiveFactoryTest(String testName) {
        super(testName);
    }

    public void testGetObjCard() {
        ObjectiveCard privObj = objFact.getPrivCard(1);
        ObjectiveCard publObj = objFact.getPublCard(3);

        assertSame(1, privObj.getId());
        assertEquals("Shades of Yellow", privObj.getDescr());

        assertSame(3, publObj.getId());
        assertEquals("Row Shade Variety", publObj.getDescr());
        assertSame(5, publObj.getPoint());
    }
}
