package it.polimi.ingsw.server.model.roundtrack;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ListDiceRound {

    private List<Dice> listDice;
    private static final Logger logger = Logger.getLogger(ListDiceRound.class.getName());

    /**
     * Constructor
     */
    public ListDiceRound() {
        listDice = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following dices: ");
        for (Dice d : listDice)
            d.dump();
    }

    /**
     * Add Dice into ListDiceRound
     * @param d != null
     * @return true if is possible add Dice, else false
     * @throws SameDiceException when there is another Dice with the same ID
     */
    public boolean addDice(Dice d) throws SameDiceException {
        for (Dice itr : listDice)
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already on Round Track");

        return listDice.add(d);
    }

    public boolean addDice(List<Dice> d) throws SameDiceException {
        if (listDice.containsAll(d))
            throw new SameDiceException("Dices are already on Round Track");

        return listDice.addAll(d);
    }

    /**
     * Remove Dice from ListDiceRound
     * @param d != null
     * @return true if is removed, else false
     * @throws EmptyException when trying to get a dice from an empty ListDiceRound
     * @throws IDNotFoundException when non existent Dices are selected
     */
    public boolean rmDice(Dice d) throws EmptyException, IDNotFoundException {
        if (listDice.isEmpty())
            throw new EmptyException(this.toString() + "is empty");
        else {
            for (Dice itr : listDice)
                if (d.getID() == itr.getID())
                    return listDice.remove(itr);
        }

        throw new IDNotFoundException("Id not found");
    }

    public Iterator<Dice> itr() {
        return listDice.iterator();
    }

    public Dice getDice(int id) throws IDNotFoundException {
        for (Dice dice : listDice)
            if (dice.getID() == id)
                return dice.copyDice();

        throw new IDNotFoundException("Id not found");
    }

    public boolean contains(Dice d) {
        return listDice.contains(d);
    }
}