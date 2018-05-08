package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import exception.IDNotFoundException;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectiveFactoryTest  extends TestCase {

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveFactory objFact = new ObjectiveFactory(objStrat);
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(ObjectiveFactoryTest.class.getName());

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
        int idPriv = random.nextInt(1)+6;

        assertThrows(IDNotFoundException.class, () -> objFact.getPrivCard(idPriv));
    }

    public void testExceptionPubl() {
        int idPubl = random.nextInt(1)+11;

        assertThrows(IDNotFoundException.class, () -> objFact.getPrivCard(idPubl));
    }

}
