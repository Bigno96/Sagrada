package it.polimi.ingsw.servertest.parsertest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PrivateObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

public class ObjectiveParserTest extends TestCase {

    private static final String NULL = "null";
    private static final String COLOR = "color";
    private static final String SHADE = "shade";
    private static final String DIFF = "diff";
    private static final String GRAD = "grad";
    private static final String VAR = "var";
    private static final String DIAG = "diag";
    private static final String ROW = "row";
    private static final String COL = "col";
    private static final String LIGHT = "light";
    private static final String MEDIUM = "medium";
    private static final String DARK = "dark";

    private final PublicObjectiveCardParser publicParser = (PublicObjectiveCardParser) ParserManager.getPublicCardParser();

    public ObjectiveParserTest(String testName) {
        super(testName);
    }

    /**
     * Testing if parameter of all private cards are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPrivateCards() throws FileNotFoundException, IDNotFoundException {
        PrivateObjectiveCardParser privateParser = (PrivateObjectiveCardParser) ParserManager.getPrivateCardParser();

        ObjectiveCard private1 = privateParser.makeObjectiveCard(1);
        ObjectiveCard private2 = privateParser.makeObjectiveCard(2);
        ObjectiveCard private3 = privateParser.makeObjectiveCard(3);
        ObjectiveCard private4 = privateParser.makeObjectiveCard(4);
        ObjectiveCard private5 = privateParser.makeObjectiveCard(5);

        assertEquals(1, private1.getId());
        assertEquals("YELLOW", private1.getType());

        assertEquals(2, private2.getId());
        assertEquals("RED", private2.getType());

        assertEquals(3, private3.getId());
        assertEquals("BLUE", private3.getType());

        assertEquals(4, private4.getId());
        assertEquals("GREEN", private4.getType());

        assertEquals(5, private5.getId());
        assertEquals("MAGENTA", private5.getType());
    }

    /**
     * Testing if parameter of public card 1 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic1() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(1);

        assertEquals(1, card.getId());
        assertEquals(6, card.getPoint());
        assertEquals(COLOR, card.getType());
        assertEquals(DIFF, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(ROW, card.getDir());
    }

    /**
     * Testing if parameter of public card 2 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic2() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(2);

        assertEquals(2, card.getId());
        assertEquals(5, card.getPoint());
        assertEquals(COLOR, card.getType());
        assertEquals(DIFF, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(COL, card.getDir());
    }

    /**
     * Testing if parameter of public card 3 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic3() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(3);

        assertEquals(3, card.getId());
        assertEquals(5, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(DIFF, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(ROW, card.getDir());
    }

    /**
     * Testing if parameter of public card 4 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic4() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(4);

        assertEquals(4, card.getId());
        assertEquals(4, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(DIFF, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(COL, card.getDir());
    }

    /**
     * Testing if parameter of public card 5 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic5() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(5);

        assertEquals(5, card.getId());
        assertEquals(2, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(GRAD, card.getScope());
        assertEquals(LIGHT, card.getGrad());
        assertEquals(NULL, card.getDir());
    }

    /**
     * Testing if parameter of public card 6 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic6() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(6);

        assertEquals(6, card.getId());
        assertEquals(2, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(GRAD, card.getScope());
        assertEquals(MEDIUM, card.getGrad());
        assertEquals(NULL, card.getDir());
    }

    /**
     * Testing if parameter of public card 7 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic7() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(7);

        assertEquals(7, card.getId());
        assertEquals(2, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(GRAD, card.getScope());
        assertEquals(DARK, card.getGrad());
        assertEquals(NULL, card.getDir());
    }

    /**
     * Testing if parameter of public card 8 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic8() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(8);

        assertEquals(8, card.getId());
        assertEquals(5, card.getPoint());
        assertEquals(SHADE, card.getType());
        assertEquals(VAR, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(NULL, card.getDir());
    }

    /**
     * Testing if parameter of public card 9 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic9() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(9);

        assertEquals(9, card.getId());
        assertEquals(0, card.getPoint());
        assertEquals(COLOR, card.getType());
        assertEquals(DIAG, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(NULL, card.getDir());
    }

    /**
     * Testing if parameter of public card 10 are built correctly
     * @throws FileNotFoundException thrown by the parser
     * @throws IDNotFoundException when not finding id on the parser
     */
    public void testPublic10() throws FileNotFoundException, IDNotFoundException {
        ObjectiveCard card = publicParser.makeObjectiveCard(10);

        assertEquals(10, card.getId());
        assertEquals(4, card.getPoint());
        assertEquals(COLOR, card.getType());
        assertEquals(VAR, card.getScope());
        assertEquals(NULL, card.getGrad());
        assertEquals(NULL, card.getDir());
    }
}
