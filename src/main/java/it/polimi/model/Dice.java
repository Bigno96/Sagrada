package it.polimi.model;

import java.util.Random;
import java.util.logging.Logger;

public class Dice {

    private int value;
    public enum colors {YELLOW, RED, BLUE, GREEN, VIOLET}
    private colors color;
    private int id;     // id between 0 and 89
    private static final Logger logger = Logger.getLogger(DiceBag.class.getName());

    public Dice( int id, colors color ) {
        this.id = id;
        this.color = color;
        this.value = 0;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("ID: " + getID() + " Col: " + getColor() + " Val: " + getValue());
    }

    public int getID()
    {
        return this.id;
    }

    public colors getColor()
    {
        return this.color;
    }

    public void rollDice() {
        Random rand = new Random();
        value = rand.nextInt(6 ) + 1;
    }

    public void changeValue( int newValue )
    {
        this.value = newValue;
    }

    public int getValue()
    {
        return this.value;
    }

}
