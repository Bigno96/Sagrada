package it.polimi.ingsw.server.model.windowcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.util.logging.Logger;

public class Cell {
    private int value;
    private Colors color;
    private Dice dice;
    private boolean isOccupied;
    private boolean ignoreNearby;
    private boolean ignoreValue;
    private boolean ignoreColor;
    private int row;            // position from 0 to 3
    private int col;            // position from 0 to 4

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

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

    public void freeCell() {
        if (isOccupied) {
            this.dice = null;
            isOccupied = false;
        }
    }

    public void setDice(Dice dice) throws NotEmptyException {
        if (isOccupied)
            throw new NotEmptyException("Cell not empty");
        this.dice = dice;
        isOccupied = true;
    }

    public boolean checkColor(){
        if (ignoreColor || color.equals(Colors.NULL))
            return true;
        return this.color.equals(dice.getColor());
    }

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
