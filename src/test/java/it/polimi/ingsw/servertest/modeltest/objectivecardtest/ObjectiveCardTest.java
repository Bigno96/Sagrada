package it.polimi.ingsw.servertest.modeltest.objectivecardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectiveCardTest extends TestCase {

    private Random random = new Random();
    private List<Cell> cellList = myCellListFilled();
    private int idPubl = random.nextInt(10) + 1;
    private int idPriv = random.nextInt(5) + 1;
    private int fp = random.nextInt(4) + 3;
    private WindowCard winCard = new WindowCard(1, "Test", fp, cellList);

    private ObjectiveCard privObj = new PrivateObjective(idPriv, "test1");
    private ObjectiveCard publObj = new PublicObjective(idPubl, "test2", 3);

    public ObjectiveCardTest(String testName) throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
        super(testName);
    }

    // fills a list of cell with 20 random cells and corresponding Dices
    private List<Cell> myCellListFilled() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> cellList = new ArrayList<>();
        GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        int val;
        Colors col;
        Cell c;
        Dice d;

        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++) {
                val = random.nextInt(7);
                col = Colors.random();

                c = new Cell(val, col, i, j, gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());

                // cannot set a dice with val = 0 or null color
                val = random.nextInt(6)+1;

                while (col.equals(Colors.WHITE)) {
                    col = Colors.random();
                }
                d = new Dice(i+j, col, val);

                cellList.add(c);
                c.setDice(d);
            }

        return cellList;
    }

    public void testId() {
        assertSame(idPriv, privObj.getId());
        assertSame(idPubl, publObj.getId());
        assertNotSame(privObj.toString(), publObj.toString());
    }

    public void testPoint() {
        assertSame(3, publObj.getPoint());
    }

    public void testDescr() {
        assertSame("test1", privObj.getDescription());
        assertSame("test2", publObj.getDescription());
    }

}
