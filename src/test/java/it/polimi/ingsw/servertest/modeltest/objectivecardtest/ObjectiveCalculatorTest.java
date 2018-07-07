package it.polimi.ingsw.servertest.modeltest.objectivecardtest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.objectivecard.strategy.ObjectiveCalculator;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.MatrixCell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectiveCalculatorTest extends TestCase {

    private static final GameSettingsParser settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private static final int MIN = 0;
    private static final int MAX_ROW = settings.getWindowCardMaxRow();
    private static final int MAX_COL = settings.getWindowCardMaxColumn();

    private static final Random random = new Random();
    private final int id = random.nextInt(100);
    private final int fp = random.nextInt(4)+3;

    private final List<Cell> cellList = myCellListFilled();
    private final WindowCard winCard = new WindowCard(id, "Test", fp, cellList,
                            settings.getWindowCardMaxRow(), settings.getWindowCardMaxColumn());
    private static final ObjectiveCalculator pointCalc = ObjectiveCalculator.getInstance();

    // numbers of dices with that color
    private int numYellow = 0;
    private int numMagenta = 0;
    private int numRed = 0;
    private int numGreen= 0;
    private int numBlue = 0;

    // sums of values of dices with that color
    private int sumYellow = 0;
    private int sumMagenta = 0;
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

    public ObjectiveCalculatorTest(String testName) throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        super(testName);
    }

    /**
     * Fills a list of cell with 20 random cells and corresponding Dices
     * @return List<Cell> list, list.size() = 20
     * @throws ValueException thrown when creating dices
     * @throws PositionException thrown when creating cells
     * @throws IDNotFoundException when wrong id are passed
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    private List<Cell> myCellListFilled() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        List<Cell> cellList = new ArrayList<>();
        GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        int val;
        Colors col;
        Cell c;
        Dice d;

        for (int i=MIN; i<MAX_ROW; i++)
            for (int j=MIN; j<MAX_COL; j++) {
                val = random.nextInt(7);
                col = Colors.random();

                c = new Cell(val, col, i, j,  gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());

                // cannot set a dice with val = 0 or null color
                val = random.nextInt(6)+1;

                while (col.equals(Colors.WHITE))
                    col = Colors.random();

                d = new Dice(i+j, col, val);

                cellList.add(c);
                c.setDice(d);
            }

        return cellList;
    }

    /**
     * Update count of dices per color
     * @param c cell whose color determine which counter increments
     */
    private void countCol(Cell c) {
        if (c.getDice().getColor().equals(Colors.YELLOW))
            numYellow++;
        if (c.getDice().getColor().equals(Colors.RED))
            numRed++;
        if (c.getDice().getColor().equals(Colors.GREEN))
            numGreen++;
        if (c.getDice().getColor().equals(Colors.BLUE))
            numBlue++;
        if (c.getDice().getColor().equals(Colors.MAGENTA))
            numMagenta++;
    }

    /**
     * Update sum of values of dices per color
     * @param c cell whose color determine which counter increments
     */
    private void countSumCol(Cell c) {
        if (c.getDice().getColor().equals(Colors.YELLOW))
            sumYellow += c.getDice().getValue();
        if (c.getDice().getColor().equals(Colors.RED))
            sumRed += c.getDice().getValue();
        if (c.getDice().getColor().equals(Colors.GREEN))
            sumGreen += c.getDice().getValue();
        if (c.getDice().getColor().equals(Colors.BLUE))
            sumBlue += c.getDice().getValue();
        if (c.getDice().getColor().equals(Colors.MAGENTA))
            sumMagenta += c.getDice().getValue();
    }

    /**
     * Update count of dices per shade
     * @param c cell whose shade determine which counter increments
     */
    private void countVal(Cell c) {
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

    /**
     * Testing calculating private objective point
     */
    public void testCalcPointPrivate() {
        cellList.forEach(this::countSumCol);

        assertSame(sumYellow, pointCalc.calcPointPrivate(Colors.YELLOW, winCard));
        assertSame(sumRed, pointCalc.calcPointPrivate(Colors.RED, winCard));
        assertSame(sumGreen, pointCalc.calcPointPrivate(Colors.GREEN, winCard));
        assertSame(sumBlue, pointCalc.calcPointPrivate(Colors.BLUE, winCard));
        assertSame(sumMagenta, pointCalc.calcPointPrivate(Colors.MAGENTA, winCard));
    }

    /**
     * Testing calculating point from different colors in columns
     */
    public void testCalcDifferentColumnColor() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);
        MatrixCell matrix = winCard.getWindow();
        List<Colors> colorsList = new ArrayList<>();
        int sum = 0;

        for (int j=MIN; j<MAX_COL; j++) {               // iterates on columns of window Card
            for (int i=MIN; i<MAX_ROW; i++) {
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

    /**
     * Testing calculating point from different colors in rows
     */
    public void testCalcDifferentRowColor() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 6);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);
        MatrixCell matrix = winCard.getWindow();
        List<Colors> colorsList = new ArrayList<>();
        int sum = 0;

        for (int i=MIN; i<MAX_ROW; i++) {               // iterates on rows of window Card
            for (int j=MIN; j<MAX_COL; j++) {
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

    /**
     * Testing calculating point from different sets of colors
     */
    public void testCalcVarietyColor() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 4);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);

        cellList.forEach(this::countCol);

        // find minimum of all values counted
        int min1 = Math.min(numBlue, numGreen);
        int min2 = Math.min(numRed, numMagenta);
        int min3 = Math.min(min1, min2);
        int min = Math.min(min3, numYellow);

        assertSame(min*4, pointCalc.calcVarietyColor(winCard, objCard));
        if (min != 0)
            assertNotSame(min*4, pointCalc.calcVarietyColor(winCard, wrongObjCard));
    }

    /**
     * Testing calculating point from diagonal adjacent colors
     * @throws PositionException thrown when creating cells
     */
    public void testCalcDiagonalColor() throws PositionException {
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

    /**
     * Testing calculating point from different shade in columns
     */
    public void testCalcDifferentColumnShade() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 4);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);
        MatrixCell matrix = winCard.getWindow();
        List<Integer> valuesList = new ArrayList<>();
        int sum = 0;

        for (int j=MIN; j<MAX_COL; j++) {                   // iterates on columns of window Card
            for (int i=MIN; i<MAX_ROW; i++) {
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

    /**
     * testing calculating point from different shade in rows
     */
    public void testCalcDifferentRowShade() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);
        MatrixCell matrix = winCard.getWindow();
        List<Integer> valuesList = new ArrayList<>();
        int sum = 0;

        for (int i=MIN; i<MAX_ROW; i++) {                   // iterates on rows of window Card
            for (int j=MIN; j<MAX_COL; j++) {
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

    /**
     * testing calculating point from different sets of shades
     */
    public void testCalcVarietyShade() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 5);
        ObjectiveCard wrongObjCard = new PublicObjective(2, "Test", 10);

        cellList.forEach(this::countVal);

        // find minimum of all values counted
        int min1 = Math.min(num1, num2);
        int min2 = Math.min(num3, num4);
        int min3 = Math.min(num5, num6);
        int min4 = Math.min(min1, min2);
        int min = Math.min(min3, min4);

        assertSame(min*5, pointCalc.calcVarietyShade(winCard, objCard));
        if (min != 0)
            assertNotSame(min*5, pointCalc.calcVarietyShade(winCard, wrongObjCard));
    }

    /**
     * Testing calculating point from couples of shades
     */
    public void testCalcGradationShade() {
        ObjectiveCard objCard = new PublicObjective(1, "Test", 2);

        cellList.forEach(this::countVal);

        assertSame(Math.min(num1, num2)*2, pointCalc.calcGradationShade(1, 2, winCard, objCard));
        assertSame(Math.min(num3, num4)*2, pointCalc.calcGradationShade(3, 4, winCard, objCard));
        assertSame(Math.min(num5, num6)*2, pointCalc.calcGradationShade(5, 6, winCard, objCard));
    }
}
