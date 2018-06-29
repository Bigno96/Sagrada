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

    private static final String SINGLE_DICE_ALREADY_IN_TRACK_MSG = "Dice is already on Round Track";
    private static final String MULTIPLE_DICE_ALREADY_IN_TRACK_MSG = "Dices are already on Round Track";
    private static final String EMPTY_MSG = "is empty";
    private static final String ID_NOT_FOUND_MSG = "Id not found";

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
     * @return true if is possible to add Dice, else false
     * @throws SameDiceException when there is another Dice with the same ID
     */
    public boolean addDice(Dice d) throws SameDiceException {
        for (Dice itr : listDice)
            if (itr.getID() == d.getID())
                throw new SameDiceException(SINGLE_DICE_ALREADY_IN_TRACK_MSG);

        return listDice.add(d);
    }

    /**
     * Add list of Dices into ListDiceRound
     * @param list != null
     * @return true if is possible to add Dices, else false
     * @throws SameDiceException when there is another Dice with the same ID
     */
    public boolean addDice(List<Dice> list) throws SameDiceException {
        for (Dice itr : list)
            if (listDice.contains(itr))
                throw new SameDiceException(MULTIPLE_DICE_ALREADY_IN_TRACK_MSG);

        return listDice.addAll(list);
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
            throw new EmptyException(this.toString() + EMPTY_MSG);
        else
            for (Dice itr : listDice)
                if (d.getID() == itr.getID())
                    return listDice.remove(itr);

        throw new IDNotFoundException(ID_NOT_FOUND_MSG);
    }

    /**
     * @return Iterator<Dice> over the list of dice
     */
    public Iterator<Dice> itr() {
        return listDice.iterator();
    }

    /**
     * Used to find a dice with passed id
     * @param id of dice to find
     * @return dice searched
     * @throws IDNotFoundException when non existent Dices are selected
     */
    public Dice getDice(int id) throws IDNotFoundException {
        for (Dice dice : listDice)
            if (dice.getID() == id)
                return dice.copyDice();

        throw new IDNotFoundException(ID_NOT_FOUND_MSG);
    }

    /**
     * @param d dice being searched
     * @return true if list dice contains passed dice
     */
    public boolean contains(Dice d) {
        return listDice.contains(d);
    }
}