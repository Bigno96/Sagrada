package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class DiceBag {

    private List<Dice> dices;
    private static final Logger logger = Logger.getLogger(Dice.class.getName());

    /**
     * Constructor
     * @throws IDNotFoundException when invalid Dices are forced in the bag
     */
    public DiceBag() throws IDNotFoundException {     // dice enumerated from 0 to 89
        dices = new ArrayList<>();
        int n = 0;
        for (final Colors c : Colors.values()) {    // loop on Dice's enum color
            if (c != Colors.NULL)
                for (int i = n * 18; i < (n + 1) * 18; i++) {       // 18 dices assigned per color
                    Dice dice = new Dice(i, c);
                    dices.add(dice);
                }
                n++;
        }
    }

    /**
     * Constructor of an empty Dice Bag
     * @param empty == true
     */
    public DiceBag(boolean empty) {
        if (empty)
            dices = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following dices: ");
        for (Dice d : dices)
            d.dump();

    }

    /**
     * Return the dice searched by a passed id
     * @param id >= 0 && <= 89
     * @return null if not found,else return Dice
     */
    public Dice findDice(int id) {         // find and return Dice with passed id
        for (final Dice d : dices)
            if (d.getID() == id)
                return d;

        return null;
    }

    /**
     * Return random dice between the ones in the bag that doesn't have a value
     * @return return null if bag is empty,else ID of a random dice of the bag
     * @throws IDNotFoundException when try to take a Dice with invalid id
     */
    public Dice randDice() throws IDNotFoundException {
        if (dices.isEmpty())            // if bag is empty, return null
            return null;
        Dice d;
        do {
            Random rand = new Random();
            d = dices.get(rand.nextInt(dices.size()));
        } while (d == null || d.getValue() != 0);

        return d.copyDice();
    }

    public int diceRemaining() { return this.dices.size(); }

    /**
     * Remove Dice d if the bag is not empty
     * @param d dice to remove
     * @return true if remove is successful
     * @throws EmptyException when bag is empty
     * @throws IDNotFoundException when try to take a Dice with invalid id
     */
    public boolean rmDice(Dice d) throws EmptyException, IDNotFoundException {
        if (dices.isEmpty()) {
            throw new EmptyException("Draft is empty");
        } else {
            for (Dice itr : dices)
                if (d.getID() == itr.getID())
                    return dices.remove(itr);
        }

        throw new IDNotFoundException("Id not found");
    }

    /**
     * Add Dice d if d it's not already in the bag
     * @param d dice to add
     * @return true if add is successful
     * @throws SameDiceException when dice is already in Bag
     * @throws ValueException when changeValue try to put an incorrect value
     */
    public boolean addDice(Dice d) throws SameDiceException, ValueException {
        for (Dice itr : dices)
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already in Bag");
        d.changeValue(0);
        return dices.add(d);
    }

}