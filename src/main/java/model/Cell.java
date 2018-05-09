package model;

import exception.IDNotFoundException;
import exception.NotEmptyException;
import exception.PositionException;
import exception.ValueException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Cell {
    private int value;
    private Colors color;
    private Dice dice;
    private boolean isOccupied;
    private int pos;            // position from 0 to 19
    private Set<Integer> borderPos = new HashSet<>(Arrays.asList(0,1,2,3,4,5,9,10,14,15,16,17,18,19));

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public Cell(int value, Colors color, int pos) throws ValueException, PositionException {
        if (value < 0 || value > 6)
            throw new ValueException("Illegal Value");
        this.value = value;
        this.color = color;
        isOccupied = false;
        if (pos < 0 || pos > 19)
            throw new PositionException("Illegal Position");
        this.pos = pos;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Col: " + getColor() + " Val: " + getValue());
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
        return this.color.equals(dice.getColor());
    }

    public boolean checkValue(){
        return this.getValue() == dice.getValue();
    }

    public Dice getDice() throws IDNotFoundException {
        if (dice == null)
            return null;
        return dice.copyDice();
    }

    public int getPos() {
        return this.pos;
    }

    public boolean isBorder() {
        return borderPos.contains(pos);
    }

}
