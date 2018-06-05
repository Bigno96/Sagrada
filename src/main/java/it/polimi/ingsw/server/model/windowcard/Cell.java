package it.polimi.ingsw.server.model.windowcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cell {

    public enum CellState { OCCUPIED, IGNORE_NEARBY, IGNORE_VALUE, IGNORE_COLOR }
    private List<CellState> states;

    private static final String WRONG_VALUE = "Illegal Value";
    private static final String WRONG_POSITION = "Illegal Position";
    private static final String NOT_EMPTY_CELL = "Cell not empty";

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 6;

    private int value;  // value from 0 to 6, 0 when when there aren't value restrictions
    private Colors color;
    private Dice dice;
    private int row;
    private int col;

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    /**
     * Constructor
     * @param value >= 0 && value <= 6
     * @param color color restriction
     * @param row >= 0 && row <= 3
     * @param col >= 0 && row <= 4
     * @throws ValueException when invalid value
     * @throws PositionException when invalid position ( row || col)
     */
    public Cell(int value, Colors color, int row, int col, int maxRow, int maxCol) throws ValueException, PositionException {
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new ValueException(WRONG_VALUE);

        if (row < MIN_VALUE || row > maxRow || col < MIN_VALUE || col > maxCol)
            throw new PositionException(WRONG_POSITION);

        this.value = value;
        this.color = color;
        this.row = row;
        this.col = col;
        this.states = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Color: " + getColor() + " Val: " + getValue() + " Row: " + getRow() + " Col: " + getCol());
    }

    /**
     * @return Color of the cell
     */
    public Colors getColor() {
        return this.color;
    }

    /**
     * @return value of the cell
     */
    public int getValue()
    {
        return this.value;
    }

    /**
     * Change value of Dice of the Cell
     * @param newValue >= 0 && newValue <= 6
     * @throws ValueException when invalid value
     */
    public void changeDiceValue(int newValue) throws ValueException {
        this.dice.changeValue(newValue);
    }

    /**
     * @return true if the cell is occupied, false else
     */
    public boolean isOccupied() {
        return states.contains(CellState.OCCUPIED);
    }

    /**
     * If the cell is occupied, free the cell
     */
    public void freeCell() {
        if (states.contains(CellState.OCCUPIED)) {
            this.dice = null;
            states.remove(CellState.OCCUPIED);
        }
    }

    /**
     * Change value of Dice of the Cell
     * @param dice != null
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public void setDice(Dice dice) throws NotEmptyException {
        if (states.contains(CellState.OCCUPIED))
            throw new NotEmptyException(NOT_EMPTY_CELL);
        this.dice = dice;
        states.add(CellState.OCCUPIED);
    }

    /**
     * Check the Dice color of the Cell
     * @return true if the Dice color is permitted, else false
     */
    public boolean checkColor() {
        if (states.contains(CellState.IGNORE_COLOR) || color.equals(Colors.WHITE))
            return true;
        return this.color.equals(dice.getColor());
    }

    /**
     * Check the Dice value of the Cell
     * @return true if the Dice value is permitted, else false
     */
    public boolean checkValue() {
        if (states.contains(CellState.IGNORE_VALUE) || value == 0)
            return true;
        return this.getValue() == dice.getValue();
    }

    /**
     * Get the dice on the cell
     * @return copy of the dice on the cell, null if not occupied
     * @throws IDNotFoundException if copying a wrong dice
     */
    public Dice getDice() throws IDNotFoundException {
        if (dice == null)
            return null;
        return dice.copyDice();
    }

    /**
     * @return row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * @return col of the cell
     */
    public int getCol() {
        return col;
    }

    /**
     * @return true if the Nearby restriction is ignored on this cell, false else
     */
    public boolean isIgnoreNearby() {
        return states.contains(CellState.IGNORE_NEARBY);
    }

    /**
     * @param ignoreNearby value of the ignoring Nearby restriction
     */
    public void setIgnoreNearby(Boolean ignoreNearby) {
        if (ignoreNearby)
            states.add(CellState.IGNORE_NEARBY);
        else
            states.remove(CellState.IGNORE_NEARBY);
    }

    /**
     * @return true if the Color restriction is ignored on this cell, false else
     */
    public boolean isIgnoreColor() {
        return states.contains(CellState.IGNORE_COLOR);
    }

    /**
     * @param ignoreColor value of the ignoring Color restriction
     */
    public void setIgnoreColor(Boolean ignoreColor) {
        if (ignoreColor)
            states.add(CellState.IGNORE_COLOR);
        else
            states.remove(CellState.IGNORE_COLOR);
    }

    /**
     * @return true if the Value restriction is ignored on this cell, false else
     */
    public boolean isIgnoreValue() {
        return states.contains(CellState.IGNORE_VALUE);
    }

    /**
     * @param ignoreValue value of the ignoring Value restriction
     */
    public void setIgnoreValue(Boolean ignoreValue) {
        if (ignoreValue)
            states.add(CellState.IGNORE_VALUE);
        else
            states.remove(CellState.IGNORE_VALUE);
    }
}
