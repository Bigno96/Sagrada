package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;

import java.io.Serializable;
import java.util.Random;
import java.util.logging.Logger;

public class Dice implements Serializable {

    private static final String DUMP_ID_MSG = "ID: ";
    private static final String DUMP_COL_MSG = " Col: ";
    private static final String DUMP_VAL_MSG = " Val: ";

    private static final String WRONG_ID = "ID not allowed";
    private static final String WRONG_VALUE = "Illegal Value";
    private static final int MAX_DICE_VALUE = 6;
    private static final int MIN_DICE_VALUE = 0;
    private static final int DICE_NUMBER = 90;

    private int value;  //value = 0, when it has not yet been rolled
    private Colors color;
    private int id;     // id between 0 and 89

    private static final Logger logger = Logger.getLogger(Dice.class.getName());

    /**
     * Constructor without value
     * @param id != null && id >= 0 && id <= 89
     * @param color != null && Colors.contain(color)
     * @throws IDNotFoundException when id is < 0 || > 89
     */
    public Dice(int id, Colors color) throws IDNotFoundException {
        if (id > DICE_NUMBER-1 || id < 0)
            throw new IDNotFoundException(WRONG_ID);
        this.id = id;
        this.color = color;
        this.value = 0;
    }

    /**
     * Constructor with value
     * @param id != null && id >= 0 && id <= 89
     * @param color != null && Colors.contain(color)
     * @param value != null && value >= 0 && value =< 6
     * @throws IDNotFoundException when id is < 0 || > 89
     */
    public Dice(int id, Colors color, int value) throws IDNotFoundException {
        if (id > DICE_NUMBER-1 || id < 0)
            throw new IDNotFoundException(WRONG_ID);
        this.id = id;
        this.color = color;
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_ID_MSG + getID() + DUMP_COL_MSG + getColor() + DUMP_VAL_MSG + getValue());
    }

    public int getID() {
        return this.id;
    }

    public Colors getColor() {
        return this.color;
    }

    /**
     * Roll dice randomly
     */
    public void rollDice() {
        Random rand = new Random();
        value = rand.nextInt(MAX_DICE_VALUE) + 1;
    }

    /**
     * Change value of dice
     * @param newValue >= 0 && < 7
     * @throws ValueException when new Value < 0 || > 6
     */
    public void changeValue(int newValue) throws ValueException {
        if (newValue < MIN_DICE_VALUE || newValue > MAX_DICE_VALUE)
            throw new ValueException(WRONG_VALUE);
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    /**
     * Return a copy of the dice
     * @return copy of the dice
     */
    public Dice copyDice() {
        try {
            return new Dice(this.id, this.color, this.value);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    /**
     * Equality of Dice
     * @param d dice to control
     * @return true d is equal
     */
    public boolean isEqual(Dice d) {
        return d.getID() == this.id && d.getValue() == this.value && d.getColor() == this.color;
    }

}
