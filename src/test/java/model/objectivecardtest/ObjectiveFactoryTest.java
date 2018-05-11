package model.objectivecardtest;

import exception.IDNotFoundException;
import junit.framework.TestCase;
import model.objectivecard.ObjectiveCard;
import model.objectivecard.ObjectiveFactory;
import model.objectivecard.ObjectiveStrategy;

import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectiveFactoryTest  extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);
    private static final Random random = new Random();

    public ObjectiveFactoryTest(String testName) {
        super(testName);
    }

    public void testGetPrivCard() throws FileNotFoundException, IDNotFoundException {
        int idPriv = random.nextInt(5)+1;
        ObjectiveCard privObj = objFact.getPrivCard(idPriv);

        assertSame(idPriv, privObj.getId());
    }

    public void testGetPublCard() throws FileNotFoundException, IDNotFoundException {
        int idPubl = random.nextInt(10)+1;
        ObjectiveCard publObj = objFact.getPublCard(idPubl);

        assertSame(idPubl, publObj.getId());
    }

    public void testExceptionPriv() {
        int idPriv = random.nextInt()+6;

        assertThrows(IDNotFoundException.class, () -> objFact.getPrivCard(idPriv));
    }

    public void testExceptionPubl() {
        int idPubl = random.nextInt()+11;

        assertThrows(IDNotFoundException.class, () -> objFact.getPublCard(idPubl));
    }

}
