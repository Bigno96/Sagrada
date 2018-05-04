package it.polimi.model;

import java.util.logging.Logger;

public class Cell {
    private int value;
    public enum colors {YELLOW, RED, BLUE, GREEN, VIOLET, NULL}
    private colors color;
    private Dice dice;
    private boolean isOccupied;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public Cell( int value, colors color )
    {
        this.value = value;
        this.color = color;
        isOccupied = false;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Col: " + getColor() + " Val: " + getValue());
    }

    public void changeColor(colors newColor) {
        this.color = newColor;
    }

    public colors getColor()
    {
        return this.color;
    }

    public void changeValue( int newValue )
    {
        this.value = newValue;
    }

    public int getValue()
    {
        return this.value;
    }

    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        isOccupied = true;
    }

    public Dice getDice() {
        return dice;
    }

}
