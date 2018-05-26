package it.polimi.ingsw.servertest.modeltest.objectivecardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.objectivecard.*;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectiveStrategyTest extends TestCase {

    private Random random = new Random();
    private List<Cell> cellList = myCellListFilled();
    private int id = random.nextInt(100);
    private int fp = random.nextInt(4)+3;
    private WindowCard winCard = new WindowCard(id, "Test", fp, cellList);
    private ObjectiveCalculator pointCalc = new ObjectiveCalculator();
    private ObjectiveStrategy objStrat = new ObjectiveStrategy();

    public ObjectiveStrategyTest(String testName) throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
        super(testName);
    }

    // fills a list of cell with 20 random cells and corresponding Dices
    private List<Cell> myCellListFilled() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> cellList = new ArrayList<>();
        int val;
        Colors col;
        Cell c;
        Dice d;

        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++) {
                val = random.nextInt(7);
                col = Colors.random();

                c = new Cell(val, col, i, j);

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

    public void testPrivateObjectiveCard() throws IDNotFoundException, FileNotFoundException, PositionException, ValueException {
        ObjectiveCard obj1 = new PrivateObjective(1, "Shades of Yellow", objStrat);
        ObjectiveCard obj2 = new PrivateObjective(2, "Shades of Red", objStrat);
        ObjectiveCard obj3 = new PrivateObjective(3, "Shades of Blue", objStrat);
        ObjectiveCard obj4 = new PrivateObjective(4, "Shades of Green", objStrat);
        ObjectiveCard obj5 = new PrivateObjective(5, "Shades of Purple", objStrat);

        assertSame(pointCalc.calcPointPriv(Colors.YELLOW, winCard), objStrat.calcPoint(obj1, winCard));
        assertSame(pointCalc.calcPointPriv(Colors.RED, winCard), objStrat.calcPoint(obj2, winCard));
        assertSame(pointCalc.calcPointPriv(Colors.BLUE, winCard), objStrat.calcPoint(obj3, winCard));
        assertSame(pointCalc.calcPointPriv(Colors.GREEN, winCard), objStrat.calcPoint(obj4, winCard));
        assertSame(pointCalc.calcPointPriv(Colors.MAGENTA, winCard), objStrat.calcPoint(obj5, winCard));
    }

    public void testPublicObjectiveCard() throws IDNotFoundException, FileNotFoundException, PositionException, ValueException {
        ObjectiveCard obj1 = new PublicObjective(1, "Row Color Variety", 6, objStrat);
        ObjectiveCard obj2 = new PublicObjective(2, "Column Color Variety", 5, objStrat);
        ObjectiveCard obj3 = new PublicObjective(3, "Row Shade Variety", 5, objStrat);
        ObjectiveCard obj4 = new PublicObjective(4, "Column Shade Variety", 4, objStrat);
        ObjectiveCard obj5 = new PublicObjective(5, "Light Shade", 2, objStrat);
        ObjectiveCard obj6 = new PublicObjective(6, "Medium Shade", 2, objStrat);
        ObjectiveCard obj7 = new PublicObjective(7, "Dark Shade", 2, objStrat);
        ObjectiveCard obj8 = new PublicObjective(8, "Shade Variety", 5, objStrat);
        ObjectiveCard obj9 = new PublicObjective(9, "Colored Diagonal", 0, objStrat);
        ObjectiveCard obj10 = new PublicObjective(10, "Color Variety", 4, objStrat);

        assertSame(pointCalc.calcDifferentRowColor(winCard, obj1), objStrat.calcPoint(obj1, winCard));
        assertSame(pointCalc.calcDifferentColumnColor(winCard, obj2), objStrat.calcPoint(obj2, winCard));
        assertSame(pointCalc.calcDifferentRowShade(winCard, obj3), objStrat.calcPoint(obj3, winCard));
        assertSame(pointCalc.calcDifferentColumnShade(winCard, obj4), objStrat.calcPoint(obj4, winCard));
        assertSame(pointCalc.calcGradationShade(1, 2, winCard, obj5), objStrat.calcPoint(obj5, winCard));
        assertSame(pointCalc.calcGradationShade(3, 4, winCard, obj6), objStrat.calcPoint(obj6, winCard));
        assertSame(pointCalc.calcGradationShade(5, 6, winCard, obj7), objStrat.calcPoint(obj7, winCard));
        assertSame(pointCalc.calcVarietyShade(winCard, obj8), objStrat.calcPoint(obj8, winCard));
        assertSame(pointCalc.calcDiagonalColor(winCard), objStrat.calcPoint(obj9, winCard));
        assertSame(pointCalc.calcVarietyColor(winCard, obj10), objStrat.calcPoint(obj10, winCard));
    }

    public void testException() {
        ObjectiveCard obj = new ObjectiveCard(random.nextInt(100), "test", 3, objStrat);
        ObjectiveCard parsePrivObj = new PrivateObjective(random.nextInt()+6, "test", objStrat);

        assertThrows(ValueException.class, () -> objStrat.calcPoint(obj, winCard));
        assertThrows(IDNotFoundException.class, () -> objStrat.calcPoint(parsePrivObj, winCard));
    }
}
