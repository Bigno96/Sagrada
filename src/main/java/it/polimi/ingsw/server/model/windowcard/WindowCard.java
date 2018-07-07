package it.polimi.ingsw.server.model.windowcard;

import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.WrongPositionException;
import it.polimi.ingsw.exception.EmptyException;

import static java.lang.System.*;

public class WindowCard extends Observable implements Serializable {

    private static final String COLOR_ERROR_MSG = "Restrizione di colore non rispettata nella cella: ";
    private static final String VALUE_ERROR_MSG = "Restrizione di valore non rispettata nella cella: ";
    private static final String FIRST_DICE_POSITION_ERROR_MSG = "Il primo dado deve essere posizionato nel bordo dello schema";
    private static final String MORE_DICE_POSITION_ERROR_MSG = "Più di un dado è stato posizionato";
    private static final String NO_DICE_POSITION_ERROR_MSG = "Nessun dado è stato posizionato";
    private static final String POSITION_ERROR_MSG = "Non si sono rispettate le restrizioni di adiacenza con gli altri dadi nella cella: ";
    private static final String NO_DICE_AROUND_POSITION_ERROR_MSG = "Nessun altro dado è presente nelle vicinanze della cella: ";

    private static final String DUMP_ID_MSG = "ID: ";
    private static final String DUMP_NAME_MSG = " Name: ";
    private static final String DUMP_NUM_FAVOR_POINT_MSG = " NumFavPoints: ";

    private MatrixCell window;
    private int id;                 // id it's the same for 2 window card that represents front and behind of a real Window Card
    private int numFavPoint;
    private String name;

    /**
     * Constructor
     * @param id != null
     * @param name != null
     * @param numFavPoint !=null
     * @param cellList != null
     */
    public WindowCard (int id, String name, int numFavPoint, List<Cell> cellList, int maxRow, int maxCol) {
        this.id = id;
        this.name = name;
        this.numFavPoint = numFavPoint;

        window = new MatrixCell(maxRow, maxCol);
        this.window.loadMatrixCell(cellList);
    }

    /**
     * @return id of window card
     */
    public int getId() {
        return id;
    }

    /**
     * @return name of window card
     */
    public String getName() {
        return name;
    }

    /**
     * @return number of favor point of this window card
     */
    public int getNumFavPoint() {
        return numFavPoint;
    }

    /**
     * @return MatrixCell representing list of Cells of this window card
     */
    public MatrixCell getWindow() {
        return window;
    }

    @Override
    public String toString() {
    return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        out.println(DUMP_ID_MSG + getId() + DUMP_NAME_MSG + getName() + DUMP_NUM_FAVOR_POINT_MSG + getNumFavPoint());
        window.itrHorizontal().forEachRemaining(Cell::dump);
    }

    /**
     * @return iterator over cells matrix that iterates horizontally
     */
    public Iterator<Cell> getHorizontalItr() {
        return window.itrHorizontal();
    }

    /**
     * Checks if the first dice position is correct
     * @return true if dice position is correct, else false
     * @throws WrongPositionException when Dice position is wrong
     * @throws EmptyException when cells have invalid positions
     */
    public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        if (checkOneDice()) {
            for (Iterator<Cell> itr = window.itrHorizontal(); itr.hasNext();) {
                Cell c = itr.next();
                if (c.isOccupied() && !window.isBorder(c))
                    throw new WrongPositionException(FIRST_DICE_POSITION_ERROR_MSG);
            }

        } else
            throw new WrongPositionException(MORE_DICE_POSITION_ERROR_MSG);

        return true;
    }

    /**
     * Checks if the dice position is correct
     * @return true if dice position is correct, else false
     * @throws WrongPositionException when Dice position is wrong
     * @throws EmptyException when cells have invalid positions
     */
    public boolean checkOneDice() throws EmptyException, WrongPositionException {
        Boolean first = true;

        for (Iterator<Cell> itr = window.itrHorizontal(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.isOccupied() && first) {
                first = false;
                if (!c.checkColor())
                    throw new WrongPositionException(COLOR_ERROR_MSG + cellErrorStringBuilder(c));
                if (!c.checkValue())
                    throw new WrongPositionException(VALUE_ERROR_MSG + cellErrorStringBuilder(c));
            }
            else if (c.isOccupied() && !first)
                return false;
        }

        if (first)
            throw new EmptyException(NO_DICE_POSITION_ERROR_MSG);

        return true;
    }

    /**
     * Check if a dice on a cell has valid color adjacency
     * @param c cell to check
     * @param cellList list of cell where to check
     * @return true if the cell has valid color adjacency, false else
     */
    public boolean checkOrtCol(Cell c, List<Cell> cellList) {
        Optional<Cell> ret = cellList.stream()
                .filter(cell -> cell.getDice() != null)
                .filter(cell -> !cell.isIgnoreColor() && c.getDice().getColor().equals(cell.getDice().getColor()))
                .findAny();

        return !ret.isPresent();
    }

    /**
     * Check if a dice on a cell has valid value adjacency
     * @param c cell to check
     * @param cellList list of cell where to check
     * @return true if the cell has valid value adjacency, false else
     */
    public boolean checkOrtVal(Cell c, List<Cell> cellList) {
        Optional<Cell> ret = cellList.stream()
                .filter(cell -> cell.getDice() != null)
                .filter(cell -> !cell.isIgnoreValue() && c.getDice().getValue() == cell.getDice().getValue())
                .findAny();

        return !ret.isPresent();
    }

    /**
     * Check if a dice on a cell satisfy restriction of color and value adjacency
     * @param c cell to check
     * @return true if the cell satisfy restriction, false else
     * @throws PositionException when trying to pass wrong coordinates
     */
    public boolean checkOrtPos(Cell c) throws PositionException {
        List<Cell> orthogonal = window.retOrthogonal(c.getRow(), c.getCol());

        Optional<Cell> ret = orthogonal.stream()
                .filter(cell -> cell.getDice() != null)
                .filter(cell ->
                        (!c.isIgnoreValue() && !checkOrtVal(c, orthogonal)) ||
                        (!c.isIgnoreColor() && !checkOrtCol(c, orthogonal)))
                .findAny();

        return !ret.isPresent();
    }

    /**
     * Check if the cell occupied has some neighbors
     * @param c cell to check
     * @return true if the dice has some neighbors, false else
     * @throws PositionException when trying to pass wrong coordinates
     */
    public boolean checkNeighbors(Cell c) throws PositionException {
        if (c.isIgnoreNearby())
            return true;

        List<Cell> neighbors = window.retNeighbors(c.getRow(), c.getCol());

        Optional<Cell> ret = neighbors.stream()
                .filter(Cell::isOccupied)
                .findAny();

        return ret.isPresent();
    }

    /**
     * Check if all the restriction are satisfied on the window card
     * @return true if restriction are satisfied, false else
     * @throws WrongPositionException thrown when place condition are not satisfied
     * @throws PositionException when trying to pass wrong coordinates
     */
    public boolean checkPlaceCond() throws WrongPositionException, PositionException {
        for (Iterator<Cell> itr = window.itrHorizontal(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.isOccupied()) {
                if (!c.checkColor())
                    throw new WrongPositionException(COLOR_ERROR_MSG + cellErrorStringBuilder(c));
                else if (!c.checkValue())
                    throw new WrongPositionException(VALUE_ERROR_MSG + cellErrorStringBuilder(c));
                else if (!checkOrtPos(c))
                    throw new WrongPositionException(POSITION_ERROR_MSG + cellErrorStringBuilder(c));
                else if (!checkNeighbors(c))
                    throw new WrongPositionException(NO_DICE_AROUND_POSITION_ERROR_MSG + cellErrorStringBuilder(c));
            }
        }

        return true;
    }

    /**
     * @return number of empty cell in window card
     */
    public int numEmptyCells() {
        int count = 0;
        for (Iterator<Cell> itr = window.itrHorizontal(); itr.hasNext();)
            if (!itr.next().isOccupied())
                count++;

        return count;
    }

    /**
     * @param c cell to build message
     * @return coordinates of cell into an error message
     */
    private String cellErrorStringBuilder(Cell c) {
        return "(" + c.getRow() + "," + c.getCol() + ")";
    }

    /**
     * Set changed and notify observer
     */
    public void setPlacement(Cell c) {
        setChanged();
        notifyObservers(c);
    }

}
