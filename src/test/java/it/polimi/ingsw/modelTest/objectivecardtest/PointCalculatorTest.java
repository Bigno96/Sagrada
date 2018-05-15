package it.polimi.ingsw.modelTest.objectivecardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import junit.framework.TestCase;
import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.objectivecard.ObjectiveStrategy;
import it.polimi.ingsw.model.objectivecard.PointCalculator;
import it.polimi.ingsw.model.objectivecard.PublicObjective;
import it.polimi.ingsw.model.windowcard.Cell;
import it.polimi.ingsw.model.windowcard.MatrixCell;
import it.polimi.ingsw.model.windowcard.WindowCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointCalculatorTest extends TestCase {

    private Random random = new Random();
    private List<Cell> cellList = myCellListFilled();
    private int id = random.nextInt(100);
    private int fp = random.nextInt(4)+3;
    private WindowCard winCard = new WindowCard(id, "Test", fp, cellList);
    private PointCalculator pointCalc = new PointCalculator();
    private ObjectiveStrategy objStrat = new ObjectiveStrategy();

    // numbers of dices with that color
    private int numYellow = 0;
    private int numViolet = 0;
    private int numRed = 0;
    private int numGreen= 0;
    private int numBlue = 0;

    // sums of values of dices with that color
    private int sumYellow = 0;
    private int sumViolet = 0;
    private int sumRed = 0;
    private int sumGreen= 0;
    private int sumBlue = 0;

    // numbers of dices with that shade
    private int num1 = 0;
    private int num2 = 0;
    private int num3 = 0;
    private int num4 = 0;
    private int num5 = 0;
    private int num6 = 0;

    public PointCalculatorTest(String testName) throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
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

    // update count of dices per color
    private void countCol(Cell c) throws IDNotFoundException {
        if (c.getDice().getColor().equals(Colors.YELLOW)) {
            numYellow++;
        }
        if (c.getDice().getColor().equals(Colors.RED)) {
            numRed++;
        }
        if (c.getDice().getColor().equals(Colors.GREEN)) {
            numGreen++;
        }
        if (c.getDice().getColor().equals(Colors.BLUE)) {
            numBlue++;
        }
        if (c.getDice().getColor().equals(Colors.VIOLET)) {
            numViolet++;
        }
    }

    // update sum of values of dices per color
    private void countSumCol(Cell c) throws IDNotFoundException {
        if (c.getDice().getColor().equals(Colors.YELLOW)) {
            sumYellow += c.getDice().getValue();
        }
        if (c.getDice().getColor().equals(Colors.RED)) {
            sumRed += c.getDice().getValue();
        }
        if (c.getDice().getColor().equals(Colors.GREEN)) {
            sumGreen += c.getDice().getValue();
        }
        if (c.getDice().getColor().equals(Colors.BLUE)) {
            sumBlue += c.getDice().getValue();
        }
        if (c.getDice().getColor().equals(Colors.VIOLET)) {
            sumViolet += c.getDice().getValue();
        }
    }

    // update count of dices per shade
    private void countVal(Cell c) throws IDNotFoundException {
        if (c.getDice().getValue() == 1)
            num1++;
        if (c.getDice().getValue() == 2)
            num2++;
        if (c.getDice().getValue() == 3)
            num3++;
        if (c.getDice().getValue() == 4)
            num4++;
        if (c.getDice().getValue() == 5)
            num5++;
        if (c.getDice().getValue() == 6)
            num6++;
    }

    public void testCalcPointPriv() throws IDNotFoundException {
        for (Cell c : cellList) {               // update sums of Values per Color
            countSumCol(c);
        }

        assertSame(sumYellow, pointCalc.calcPointPriv(Colors.YELLOW, winCard));
        assertSame(sumRed, pointCalc.calcPointPriv(Colors.RED, winCard));
        assertSame(sumGreen, pointCalc.calcPointPriv(Colors.GREEN, winCard));
        assertSame(sumBlue, pointCalc.calcPointPriv(Colors.BLUE, winCard));
        assertSame(sumViolet, pointCalc.calcPointPriv(Colors.VIOLET, winCard));
    }

    public void testCalcDifferentColumnColor() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);
        MatrixCell matrix = winCard.getWindow();
        List<Colors> colorsList = new ArrayList<>();
        int sum = 0;

        for (int j=0; j<5; j++) {               // iterates on columns of window Card
            for (int i=0; i<4; i++) {
                Colors col = matrix.getCell(i, j).getDice().getColor();
                if (!colorsList.contains(col))              // add color to the colors found in the column
                    colorsList.add(col);
                else
                    break;
            }

            if (colorsList.size() == 4)         // if found 4 color in a column
                sum += 5;                       // add 5 point

            colorsList.clear();                 // clean list
        }

        assertSame(sum, pointCalc.calcDifferentColumnColor(winCard, objCard));
        if (sum != 0)
            assertNotSame(sum, pointCalc.calcDifferentColumnColor(winCard, wrongObjCard));
    }

    public void testCalcDifferentRowColor() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 6, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);
        MatrixCell matrix = winCard.getWindow();
        List<Colors> colorsList = new ArrayList<>();
        int sum = 0;

        for (int i=0; i<4; i++) {               // iterates on rows of window Card
            for (int j=0; j<5; j++) {
                Colors col = matrix.getCell(i, j).getDice().getColor();
                if (!colorsList.contains(col))              // add color to the colors found in the row
                    colorsList.add(col);
                else
                    break;
            }

            if (colorsList.size() == 5)         // if found 5 color in a row
                sum += 6;                       // add 6 point

            colorsList.clear();                 // clean list
        }

        assertSame(sum, pointCalc.calcDifferentRowColor(winCard, objCard));
        if (sum != 0)
            assertNotSame(sum, pointCalc.calcDifferentRowColor(winCard, wrongObjCard));
    }

    public void testCalcVarietyColor() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 4, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);

        for (Cell c : cellList) {           // count number of dices per color
            countCol(c);
        }

        // find minimun of all values counted
        int min1 = Math.min(numBlue, numGreen);
        int min2 = Math.min(numRed, numViolet);
        int min3 = Math.min(min1, min2);
        int min = Math.min(min3, numYellow);

        assertSame(min*4, pointCalc.calcVarietyColor(winCard, objCard));
        if (min != 0)
            assertNotSame(min*4, pointCalc.calcVarietyColor(winCard, wrongObjCard));
    }

    public void testCalcDiagonalColor() throws IDNotFoundException, PositionException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 4, objStrat);
        int sum = 0;

        for (Cell c : cellList) {               // count all dices with diagonally adjacent dice of the same color
            List<Cell> diagonal = winCard.getWindow().retDiagonal(c.getRow(), c.getCol());
            for (Cell cell : diagonal) {
                if (cell.getDice().getColor().equals(c.getDice().getColor())) {
                    sum++;
                    break;
                }
            }
        }

        assertSame(sum, pointCalc.calcDiagonalColor(winCard));
    }

    public void testCalcDifferentColumnShade() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 4, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);
        MatrixCell matrix = winCard.getWindow();
        List<Integer> valuesList = new ArrayList<>();
        int sum = 0;

        for (int j=0; j<5; j++) {                   // iterates on columns of window Card
            for (int i=0; i<4; i++) {
                int val = matrix.getCell(i, j).getDice().getValue();
                if (!valuesList.contains(val))              // add shade to the values found in the column
                    valuesList.add(val);
                else
                    break;
            }

            if (valuesList.size() == 4)             // if found 4 shades in a column
                sum += 4;                           // add 4 points

            valuesList.clear();                     // clear list
        }

        assertSame(sum, pointCalc.calcDifferentColumnShade(winCard, objCard));
        if (sum != 0)
            assertNotSame(sum, pointCalc.calcDifferentColumnShade(winCard, wrongObjCard));
    }

    public void testCalcDifferentRowShade() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);
        MatrixCell matrix = winCard.getWindow();
        List<Integer> valuesList = new ArrayList<>();
        int sum = 0;

        for (int i=0; i<4; i++) {                   // iterates on rows of window Card
            for (int j=0; j<5; j++) {
                int val = matrix.getCell(i, j).getDice().getValue();
                if (!valuesList.contains(val))      // add shade to the values found in the row
                    valuesList.add(val);
                else
                    break;
            }

            if (valuesList.size() == 5)             // if found 5 shades in a row
                sum += 5;                           // add 5 points

            valuesList.clear();                     // clear list
        }

        assertSame(sum, pointCalc.calcDifferentRowShade(winCard, objCard));
        if (sum != 0)
            assertNotSame(sum, pointCalc.calcDifferentRowShade(winCard, wrongObjCard));
    }

    public void testCalcVarietyShade() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5, objStrat);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10, objStrat);

        for (Cell c : cellList) {           // count number of dices per value
            countVal(c);
        }

        // find minimun of all values counted
        int min1 = Math.min(num1, num2);
        int min2 = Math.min(num3, num4);
        int min3 = Math.min(num5, num6);
        int min4 = Math.min(min1, min2);
        int min = Math.min(min3, min4);

        assertSame(min*5, pointCalc.calcVarietyShade(winCard, objCard));
        if (min != 0)
            assertNotSame(min*5, pointCalc.calcVarietyShade(winCard, wrongObjCard));
    }

    public void testCalcGradationShade() throws IDNotFoundException {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 2, objStrat);

        for (Cell c : cellList) {           // count number of dices per value
            countVal(c);
        }

        assertSame(Math.min(num1, num2)*2, pointCalc.calcGradationShade(1, 2, winCard, objCard));
        assertSame(Math.min(num3, num4)*2, pointCalc.calcGradationShade(3, 4, winCard, objCard));
        assertSame(Math.min(num5, num6)*2, pointCalc.calcGradationShade(5, 6, winCard, objCard));
    }
}
