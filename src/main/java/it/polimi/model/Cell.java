package it.polimi.model;

public class Cell {
    private int value = 0;
    public enum color {GIALLO, ROSSO, BLU, VERDE, VIOLA, VUOTO}
    private color color;
    private Dice dice;
    private boolean isOccuped;

    public Cell( int value, color color )
    {
        this.value = value;
        this.color = color;
        isOccuped = false;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + " Col: " + getColor() + " Val: " + getValue();
    }

    public String dump()
    {
        return  "Col: " + getColor() + " Val: " + getValue();
    }

    public void changeColor(color newColor){
        this.color = newColor;
    }

    public color getColor()
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

    public boolean getIsOccuped() {
        return this.isOccuped;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        isOccuped = true;
    }

    public Dice getDice() {
        return dice;
    }

}
