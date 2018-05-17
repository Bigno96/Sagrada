package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;

import java.util.Random;
import java.util.logging.Logger;

public class Dice {

    private int value;
    private Colors color;
    private int id;     // id between 0 and 89
    private static final Logger logger = Logger.getLogger(DiceBag.class.getName());

    public Dice(int id, Colors color) throws IDNotFoundException {
        if (id > 89 || id < 0)
            throw new IDNotFoundException("ID not allowed");
        this.id = id;
        this.color = color;
        this.value = 0;
    }

    public Dice(int id, Colors color, int value) throws IDNotFoundException {
        if (id > 89 || id < 0)
            throw new IDNotFoundException("ID not allowed");
        this.id = id;
        this.color = color;
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("ID: " + getID() + " Col: " + getColor() + " Val: " + getValue());
    }

    public int getID() {
        return this.id;
    }

    public Colors getColor() {
        return this.color;
    }

    public void rollDice() {
        Random rand = new Random();
        value = rand.nextInt(6) + 1;
    }

    public void changeValue(int newValue) throws ValueException {
        if (newValue < 0 || newValue > 6)
            throw new ValueException("Illegal Value");
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    public Dice copyDice() throws IDNotFoundException {
        return new Dice(this.id, this.color, this.value);
    }

    public boolean isEqual(Dice d) {
        return d.getID() == this.id && d.getValue() == this.value && d.getColor() == this.color;
    }

}
