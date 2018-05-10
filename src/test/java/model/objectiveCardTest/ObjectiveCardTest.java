package model.objectiveCardTest;

import junit.framework.TestCase;
import model.objectiveCard.ObjectiveCard;
import model.objectiveCard.ObjectiveStrategy;
import model.objectiveCard.PrivateObjective;
import model.objectiveCard.PublicObjective;

public class ObjectiveCardTest extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveCard privObj = new PrivateObjective(1, "test1", objStrat);
    private ObjectiveCard publObj = new PublicObjective(2, "test2", 3, objStrat);

    public ObjectiveCardTest(String testName){
        super(testName);
    }

    public void testId() {
        assertSame(1, privObj.getId());
        assertSame(2, publObj.getId());
    }

    public void testPoint() {
        assertSame(3, publObj.getPoint());
    }

    public void testDescr() {
        assertSame("test1", privObj.getDescr());
        assertSame("test2", publObj.getDescr());
    }

    public void testFP() {
        assertSame(0, privObj.getFP());
        assertSame(0, publObj.getFP());
        privObj.setFP(1);
        publObj.setFP(3);
        assertSame(1, privObj.getFP());
        assertSame(3, publObj.getFP());
    }

}
