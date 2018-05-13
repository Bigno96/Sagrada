package model.objectivecard;

import exception.IDNotFoundException;
import exception.NotEmptyException;
import exception.PositionException;
import exception.ValueException;
import junit.framework.TestCase;
import model.Colors;
import model.dicebag.Dice;
import model.windowcard.Cell;
import model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectiveCardTest extends TestCase {

    private Random random = new Random();
    private List<Cell> cellList = myCellListFilled();
    private int idPubl = random.nextInt(10)+1;
    private int idPriv = random.nextInt(5)+1;
    private int fp = random.nextInt(4)+3;
    private WindowCard winCard = new WindowCard(1, "Test", fp, cellList);

    private ObjectiveStrategy objStrat = new ObjectiveStrategy();
    private ObjectiveCard privObj = new PrivateObjective(idPriv, "test1", objStrat);
    private ObjectiveCard publObj = new PublicObjective(idPubl, "test2", 3, objStrat);

    public ObjectiveCardTest(String testName) throws IDNotFoundException, PositionException, NotEmptyException, ValueException {
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
                while (val == 0) {
                    val = random.nextInt(6)+1;
                }
                while (col.equals(Colors.NULL)) {
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
        assertSame("test1", privObj.getDescr());
        assertSame("test2", publObj.getDescr());
    }

    public void testCalcPointPrivate() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        ObjectiveCard obj1 = new PrivateObjective(1, "Shades of Yellow", objStrat);
        ObjectiveCard obj2 = new PrivateObjective(2, "Shades of Red", objStrat);
        ObjectiveCard obj3 = new PrivateObjective(3, "Shades of Blue", objStrat);
        ObjectiveCard obj4 = new PrivateObjective(4, "Shades of Green", objStrat);
        ObjectiveCard obj5 = new PrivateObjective(5, "Shades of Purple", objStrat);

        assertSame(obj1.calcPoint(winCard), objStrat.calcPoint(obj1, winCard));
        assertSame(obj2.calcPoint(winCard), objStrat.calcPoint(obj2, winCard));
        assertSame(obj3.calcPoint(winCard), objStrat.calcPoint(obj3, winCard));
        assertSame(obj4.calcPoint(winCard), objStrat.calcPoint(obj4, winCard));
        assertSame(obj5.calcPoint(winCard), objStrat.calcPoint(obj5, winCard));
    }

    public void testCalcPointPublic() throws IDNotFoundException, FileNotFoundException, PositionException, ValueException {
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

        assertSame(obj1.calcPoint(winCard), objStrat.calcPoint(obj1, winCard));
        assertSame(obj2.calcPoint(winCard), objStrat.calcPoint(obj2, winCard));
        assertSame(obj3.calcPoint(winCard), objStrat.calcPoint(obj3, winCard));
        assertSame(obj4.calcPoint(winCard), objStrat.calcPoint(obj4, winCard));
        assertSame(obj5.calcPoint(winCard), objStrat.calcPoint(obj5, winCard));
        assertSame(obj6.calcPoint(winCard), objStrat.calcPoint(obj6, winCard));
        assertSame(obj7.calcPoint(winCard), objStrat.calcPoint(obj7, winCard));
        assertSame(obj8.calcPoint(winCard), objStrat.calcPoint(obj8, winCard));
        assertSame(obj9.calcPoint(winCard), objStrat.calcPoint(obj9, winCard));
        assertSame(obj10.calcPoint(winCard), objStrat.calcPoint(obj10, winCard));
    }
}
