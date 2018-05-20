package it.polimi.ingsw.server.model.windowcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.util.logging.Logger;

public class Cell {
    private int value;  // value from 0 to 6, 0 when when there aren't value restrictions
    private Colors color;
    private Dice dice;
    private boolean isOccupied;
    private boolean ignoreNearby;
    private boolean ignoreValue;
    private boolean ignoreColor;
    private int row;            // position from 0 to 3
    private int col;            // position from 0 to 4

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    /**
     * Constructor
     * @param value >= 0 && value <= 6
     * @param color color restriction
     * @param row != null
     * @param col != null
     * @throws ValueException when invalid value
     * @throws PositionException when invalid position ( row || col)
     */
    public Cell(int value, Colors color, int row, int col) throws ValueException, PositionException {
        if (value < 0 || value > 6)
            throw new ValueException("Illegal Value");
        this.value = value;
        this.color = color;
        isOccupied = false;
        ignoreNearby = false;
        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException("Illegal Position");
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Color: " + getColor() + " Val: " + getValue() + " Row: " + getRow() + " Col: " + getCol());
    }

    public Colors getColor() {
        return this.color;
    }

    /**
     * Change value of Dice of the Cell
     * @param newValue >= 0 && newValue <= 6
     * @throws ValueException when invalid value
     */
    public void changeDiceValue(int newValue) throws ValueException {
        this.dice.changeValue(newValue);
    }

    public int getValue()
    {
        return this.value;
    }

    public boolean isOccupied() {
        return this.isOccupied;
    }

    /**
     * If the cell is occupied, free the cell
     */
    public void freeCell() {
        if (isOccupied) {
            this.dice = null;
            isOccupied = false;
        }
    }

    /**
     * Change value of Dice of the Cell
     * @param dice != null
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public void setDice(Dice dice) throws NotEmptyException {
        if (isOccupied)
            throw new NotEmptyException("Cell not empty");
        this.dice = dice;
        isOccupied = true;
    }

    /**
     * Check the Dice color of the Cell
     * @return true if the Dice color is permitted, else false
     */
    public boolean checkColor(){
        if (ignoreColor || color.equals(Colors.NULL))
            return true;
        return this.color.equals(dice.getColor());
    }

    /**
     * Check the Dice value of the Cell
     * @return true if the Dice value is permitted, else false
     */
    public boolean checkValue(){
        if (ignoreValue || value == 0)
            return true;
        return this.getValue() == dice.getValue();
    }

    public Dice getDice() throws IDNotFoundException {
        if (dice == null)
            return null;

        return dice.copyDice();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isIgnoreNearby() {
        return ignoreNearby;
    }

    public void setIgnoreNearby() {
        this.ignoreNearby = true;
    }

    public void resetIgnoreNearby() {
        this.ignoreNearby = false;
    }

    public boolean isIgnoreColor() {
        return ignoreColor;
    }

    public void setIgnoreColor() {
        this.ignoreColor = true;
    }

    public void resetIgnoreColor() {
        this.ignoreColor = false;
    }

    public boolean isIgnoreValue() {
        return ignoreValue;
    }

    public void setIgnoreValue() {
        this.ignoreValue = true;
    }

    public void resetIgnoreValue() {
        this.ignoreValue = false;
    }
}
