package it.polimi.ingsw.servertest.modeltest.objectivecardtest;

import junit.framework.TestCase;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;

import java.util.Random;

public class ObjectiveCardTest extends TestCase {

    private Random random = new Random();
    private int idPublic = random.nextInt(10) + 1;
    private int idPrivate = random.nextInt(5) + 1;

    private ObjectiveCard privateObj = new PrivateObjective(idPrivate, "test1");
    private ObjectiveCard publicObj = new PublicObjective(idPublic, "test2", 3);

    public ObjectiveCardTest(String testName) {
        super(testName);
    }

    public void testId() {
        assertSame(idPrivate, privateObj.getId());
        assertSame(idPublic, publicObj.getId());
        assertNotSame(privateObj.toString(), publicObj.toString());
    }

    public void testPoint() {
        assertSame(3, publicObj.getPoint());
        assertSame(0, privateObj.getPoint());
    }

    public void testDescription() {
        assertSame("test1", privateObj.getDescription());
        assertSame("test2", publicObj.getDescription());
    }

    public void testSetParameter() {
        String nullString = "null";
        String type = "t";
        String scope = "s";
        String grad = "g";
        String dir = "d";

        privateObj.setParameter(type, scope, grad, dir);
        assertSame(type, privateObj.getType());
        assertSame(nullString, privateObj.getScope());
        assertSame(nullString, privateObj.getGrad());
        assertSame(nullString, privateObj.getDir());

        publicObj.setParameter(type, scope, grad, dir);
        assertSame(type, publicObj.getType());
        assertSame(scope, publicObj.getScope());
        assertSame(grad, publicObj.getGrad());
        assertSame(dir, publicObj.getDir());
    }

    public void testCopy() {
        assertNotSame(privateObj, privateObj.copy());
        assertSame(privateObj.getId(), privateObj.copy().getId());

        assertNotSame(publicObj, publicObj.copy());
        assertSame(publicObj.getId(), publicObj.copy().getId());
    }

}
