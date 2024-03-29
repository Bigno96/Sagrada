package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ObjectiveCalculator implements Serializable {

    private static final GameSettingsParser settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private static final int MIN = 0;
    private static final int MAX_ROW = settings.getWindowCardMaxRow();
    private static final int MAX_COL = settings.getWindowCardMaxColumn();

    private static ObjectiveCalculator instance = null;


    public static ObjectiveCalculator getInstance() {
        if (instance == null)
            instance = new ObjectiveCalculator();
        
        return instance;
    }

    private ObjectiveCalculator() {
    }

    /**
     * Calculating points from Private Objective
     * @param col != null && col >= 0 && col <= 4
     * @param winCard != null
     * @return sum points of PrivateObjective
     */
    public int calcPointPrivate(Colors col, WindowCard winCard) {
        int sum = 0;
        List<Cell> occupiedCellList = new ArrayList<>();

        winCard.getHorizontalItr().forEachRemaining(cell -> {
            if (cell.isOccupied())
                occupiedCellList.add(cell);
        });

        for (Cell c : occupiedCellList) {
            if (c.getDice().getColor() == col) {         // if dice on cell has same color of the one of the Private Objective
                sum += c.getDice().getValue();          // sum the value of the dice
            }
        }

        return sum;
    }

    /**
     * Calculating points from Row without colors repetition
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 1
     */
    public int calcDifferentRowColor(WindowCard winCard, ObjectiveCard objective) {
        Cell c;
        int sum = 0;
        HashSet<Colors> colorFound = new HashSet<>();           // set of color found on the row

        for (int i = MIN; i < MAX_ROW; i++) {                             // iterates on rows of Window Card
            for (int j = MIN; j < MAX_COL; j++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied())                                          // if the cell is occupied
                    if (colorFound.contains(c.getDice().getColor()))         // and it's not a new color in the row
                        break;                                               // exit from the row
                    else
                        colorFound.add(c.getDice().getColor());              // if it's a new color, add it to colorFound on the row
            }

            if (colorFound.size() == 5)                     // if 5 different colors are found on a row
                sum += objective.getPoint();                // add points

            colorFound.clear();                             // reset color Found on row
        }

        return sum;
    }
    /**
     * Calculating points from Column without colors repetition
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 2
     */
    public int calcDifferentColumnColor(WindowCard winCard, ObjectiveCard objective) {
        Cell c;
        int sum = 0;
        HashSet<Colors> colorFound = new HashSet<>();           // set of color found on the column

        for (int j = MIN; j < MAX_COL; j++) {                             // iterates on columns of Window Card
            for (int i = MIN; i < MAX_ROW; i++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied())                                             // if the cell is occupied
                    if (colorFound.contains(c.getDice().getColor()))            // and it's not new color in the column
                        break;                                                  // exit from the column
                else
                    colorFound.add(c.getDice().getColor()); // if it's a new color, add it to colorFound on the column
            }

            if (colorFound.size() == 4)                     // if 4 different colors are found on a column
                sum += objective.getPoint();                // add points

            colorFound.clear();                 // reset color Found on column
        }

        return sum;
    }

    /**
     * Calculating points from Row without shades repetition
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 3
     */
    public int calcDifferentRowShade(WindowCard winCard, ObjectiveCard objective) {
        Cell c;
        int sum = 0;
        HashSet<Integer> valueFound = new HashSet<>();              // set of shade found on the row

        for (int i = MIN; i < MAX_ROW; i++) {                                 // iterates on rows of Window Card
            for (int j = MIN; j < MAX_COL; j++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied())                                         // if the cell is occupied
                    if (valueFound.contains(c.getDice().getValue()))        // and it's not a new shade in the row
                        break;                                              // exit from the row
                    else
                        valueFound.add(c.getDice().getValue()); // if it's a new shade, add it to valueFound on the row
            }

            if (valueFound.size() == 5)                     // if 5 different shades are found on a row
                sum += objective.getPoint();                // add points

            valueFound.clear();                             // reset shade Found on row
        }

        return sum;
    }

    /**
     * Calculating points from Column without shades repetition
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 4
     */
    public int calcDifferentColumnShade(WindowCard winCard, ObjectiveCard objective) {
        Cell c;
        int sum = 0;
        HashSet<Integer> valueFound = new HashSet<>();              // set of shade found on the column

        for (int j = MIN; j < MAX_COL; j++) {                                 // iterates on columns of Window Card
            for (int i = MIN; i < MAX_ROW; i++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied())                                         // if the cell is occupied
                    if (valueFound.contains(c.getDice().getValue()))        // and it's not a new shade in the column
                        break;                                              // exit from the column
                    else
                        valueFound.add(c.getDice().getValue()); // if it's a new shade, add it to valueFound on the column
            }

            if (valueFound.size() == 4)                     // if 4 different shades are found on a column
                sum += objective.getPoint();                // add points

            valueFound.clear();                             // reset shade Found on column
        }

        return sum;
    }

    /**
     * Calculating points from different couple of shades, passed as parameters
     * @param val1 if PublicCard 5 val1 = 1, if PublicCard 6 val1 = 3, PublicCard 7 val1 = 5
     * @param val2 if PublicCard 5 val1 = 2, if PublicCard 6 val1 = 4, PublicCard 7 val1 = 6
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCards 5, 6, 7
     */
    public int calcGradationShade(int val1, int val2, WindowCard winCard, ObjectiveCard objective) {
        int num1 = 0;
        int num2 = 0;

        for (Iterator<Cell> itr = winCard.getHorizontalItr(); itr.hasNext();) {          // iterates on cells of window Card
            Cell c = itr.next();
            if (c.isOccupied()) {                                   // if cell is occupied
                if (c.getDice().getValue() == val1)                 // count every shade equals to the first
                    num1++;
                if (c.getDice().getValue() == val2)                 // count every shade equals to the second
                    num2++;
            }
        }

        return Math.min(num1, num2)*objective.getPoint();           // return the minimum of the two counts, multiplied for points
    }

    /**
     * Calculating points from different sets of 5 Shades
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 8
     */
    public int calcVarietyShade(WindowCard winCard, ObjectiveCard objective) {
        int sum = 0;
        HashSet<Integer> var1 = new HashSet<>();                // we can found max 3 sets of different shades
        HashSet<Integer> var2 = new HashSet<>();
        HashSet<Integer> var3 = new HashSet<>();

        for (Iterator<Cell> itr = winCard.getHorizontalItr(); itr.hasNext();) {      // iterates on cells of window Card
            Cell c = itr.next();
            if (c.isOccupied()) {                           // if cell is occupied
                int val = c.getDice().getValue();           // get shade of the Dice in the cell
                // add it to the set that doesn't contain it alreadY
                if (!var1.contains(val))
                    var1.add(val);
                else if (!var2.contains(val))
                    var2.add(val);
                else if (!var3.contains(val))
                    var3.add(val);
            }
        }

        // for each set of 6 shades, add points
        if (var1.size()==6)
            sum += objective.getPoint();
        if (var2.size()==6)
            sum += objective.getPoint();
        if (var3.size()==6)
            sum += objective.getPoint();

        return sum;
    }

    /**
     * Calculating points from Dices with the same colors diagonally
     * @param winCard != null
     * @return sum of points of PublicCard 9
     * @throws PositionException when retDiagonal throw exception
     */
    public int calcDiagonalColor(WindowCard winCard) throws PositionException {
        int sum = 0;

        for (Iterator<Cell> itr = winCard.getHorizontalItr(); itr.hasNext();) {      // iterates on cells of window Card
            Cell c = itr.next();
            List<Cell> diagonal = winCard.getWindow().retDiagonal(c.getRow(), c.getCol());      // get cells around diagonally the given cell
            for (Cell cell : diagonal) {                                // for all of these cells
                if (cell.isOccupied() && cell.getDice().getColor().equals(c.getDice().getColor())) {         // if a diagonal dice has the same color
                    sum++;                                                      // add 1 point
                    break;                                                      // exit from diagonal cells
                }
            }
        }

        return sum;
    }

    /**
     * Calculating points from different sets of 5 Colors
     * @param winCard != null
     * @param objective != null
     * @return sum of points of PublicCard 10
     */
    public int calcVarietyColor(WindowCard winCard, ObjectiveCard objective) {
        int sum = 0;
        HashSet<Colors> var1 = new HashSet<>();                 // we can found 4 max sets of different colors
        HashSet<Colors> var2 = new HashSet<>();
        HashSet<Colors> var3 = new HashSet<>();
        HashSet<Colors> var4 = new HashSet<>();

        for (Iterator<Cell> itr = winCard.getHorizontalItr(); itr.hasNext();) {    // iterates on cells of window Card
            Cell c = itr.next();
            if (c.isOccupied()) {                           // if cell is occupied
                Colors col = c.getDice().getColor();        // get color of the Dice in the cell
                // add it to the set that doesn't contain it already
                if (!var1.contains(col))
                    var1.add(col);
                else if (!var2.contains(col))
                    var2.add(col);
                else if (!var3.contains(col))
                    var3.add(col);
                else if (!var4.contains(col))
                    var4.add(col);
            }
        }

        // for each set of 5 colors, add points
        if (var1.size()==5)
            sum += objective.getPoint();
        if (var2.size()==5)
            sum += objective.getPoint();
        if (var3.size()==5)
            sum += objective.getPoint();
        if (var4.size()==5)
            sum += objective.getPoint();

        return sum;
    }
 }
