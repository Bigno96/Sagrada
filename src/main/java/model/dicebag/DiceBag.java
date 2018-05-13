package model.dicebag;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import exception.ValueException;
import model.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class DiceBag {

    private List<Dice> dices;
    private static final Logger logger = Logger.getLogger(Dice.class.getName());

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

    public Dice findDice(int id) {         // find and return Dice with passed id
        for (final Dice d : dices)
            if (d.getID() == id)
                return d;

        return null;
    }

    public Dice randDice() throws IDNotFoundException {                // return random dice between the ones in the bag that doesn't have a value
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

    public boolean rmDice(Dice d) throws EmptyException, IDNotFoundException {         // remove Dice d if the bag is not empty
        if (dices.isEmpty()) {
            throw new EmptyException("Draft is empty");
        } else {
            for (Dice itr : dices)
                if (d.getID() == itr.getID())
                    return dices.remove(itr);
        }

        throw new IDNotFoundException("Id not found");
    }

    public boolean addDice(Dice d) throws SameDiceException, ValueException {        // add Dice d if d it's not already in the bag
        for (Dice itr : dices)
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already in Draft");
        d.changeValue(0);
        return dices.add(d);
    }

}