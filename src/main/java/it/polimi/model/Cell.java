package it.polimi.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Cell {
    private int value;
    public enum colors {YELLOW, RED, BLUE, GREEN, VIOLET, NULL}
    private colors color;
    private Dice dice;
    private boolean isOccupied;
    private int pos;            // position from 0 to 19
    private Set<Integer> borderPos = new HashSet<Integer>(Arrays.asList(0,1,2,3,4,5,9,10,14,15,16,17,18,19));

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public Cell( int value, colors color, int pos )
    {
        this.value = value;
        this.color = color;
        isOccupied = false;
        this.pos = pos;
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

    public boolean isOccupied() {
        return this.isOccupied;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        isOccupied = true;
    }

    public Dice getDice() {
        return dice;
    }

    public int getPos() {
        return this.pos;
    }

    public boolean isBorder() {
        return borderPos.contains(pos);
    }

}
