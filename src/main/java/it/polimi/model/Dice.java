package it.polimi.model;

import java.util.Random;

public class Dice {

    private int value = 0;
    public enum color {GIALLO, ROSSO, BLU, VERDE, VIOLA}
    private color color;
    private int id;     // id compreso tra 1 e 90

    public Dice( int id, color color )
    {
        this.id = id;
        this.color = color;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getID() + " Col: " + getColor() + " Val: " + getValue();
    }

    public String dump()
    {
        return "ID: " + getID() + " Col: " + getColor() + " Val: " + getValue();
    }

    public int getID()
    {
        return this.id;
    }

    public color getColor()
    {
        return this.color;
    }

    public void rollDice()
    {
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
