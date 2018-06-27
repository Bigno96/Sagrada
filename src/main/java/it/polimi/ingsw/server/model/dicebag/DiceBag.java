package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DiceBag implements Serializable {

    private static final String EMPTY_DRAFT = "Draft is empty";
    private static final String ID_NOT_FOUND = "Id not found";
    private static final String ALREADY_IN_BAG = "Dice is already in Bag";
    private static final String DUMP_MSG = "contains following dices: ";

    private static final int NUM_DICE = 90;
    private static final int NUM_COLOR = 5;

    private List<Dice> dices;
    private static final Logger logger = Logger.getLogger(Dice.class.getName());

    /**
     * Constructor
     * @throws IDNotFoundException when invalid Dices are forced in the bag
     */
    public DiceBag() throws IDNotFoundException {     // dice enumerated from 0 to 89
        dices = new ArrayList<>();
        int n = 0;

        for (Colors c : Colors.values()) {    // loop on Dice's enum color
            if (c != Colors.WHITE)
                for (int i = n * NUM_DICE/NUM_COLOR; i < (n + 1) * NUM_DICE/NUM_COLOR; i++) {       // 18 dices assigned per color
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
        logger.info(DUMP_MSG);
        dices.forEach(Dice::dump);
    }

    /**
     * Return the dice searched by a passed id
     * @param id >= 0 && <= 89
     * @return null if not found,else return Dice
     */
    public Dice findDice(int id) {
        Optional<Dice> ret = dices.stream()
                .filter(d -> d.getID() == id)
                .collect(Collectors.toList()).stream().findFirst();

        return ret.orElse(null);
    }

    /**
     * Return random dice between the ones in the bag that doesn't have a value
     * @return ID of a random dice of the bag
     */
    public Dice randDice() {
        if (dices.isEmpty())            // if bag is empty, return null
            return null;

        Dice d;
        do {
            Random rand = new Random();
            d = dices.get(rand.nextInt(dices.size()));
        } while (d == null || d.getValue() != 0);

        return d.copyDice();
    }

    /**
     * @return number of dices remaining in the bag
     */
    public int diceRemaining() { return this.dices.size(); }

    /**
     * Remove Dice d if the bag is not empty
     * @param d dice to remove
     * @return true if remove is successful
     * @throws EmptyException when bag is empty
     * @throws IDNotFoundException when try to take a Dice with invalid id
     */
    public boolean rmDice(Dice d) throws EmptyException, IDNotFoundException {
        if (dices.isEmpty())
            throw new EmptyException(EMPTY_DRAFT);

        Optional<Dice> rm = dices.stream().filter(dice -> dice.getID() == d.getID()).findFirst();
        if (rm.isPresent())
            return dices.remove(rm.get());

        throw new IDNotFoundException(ID_NOT_FOUND);
    }

    /**
     * Add Dice d if d it's not already in the bag
     * @param d dice to add
     * @return true if add is successful
     * @throws SameDiceException when dice is already in Bag
     * @throws ValueException when changeValue try to put an incorrect value
     */
    public boolean addDice(Dice d) throws SameDiceException, ValueException {
        if (dices.stream().anyMatch(dice -> dice.getID() == d.getID()))
            throw new SameDiceException(ALREADY_IN_BAG);

        d.changeValue(0);
        return dices.add(d);
    }

}