package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Draft implements Serializable {

    private static final String NO_DICE = "No Dice in Bag";
    private static final String NOT_ENOUGH_DICE = "Not Enough Dices in Bag";
    private static final String EMPTY_DRAFT = "Draft is empty";
    private static final String ID_NOT_FOUND = "Id not found";
    private static final String ALREADY_IN_DRAFT = "Dice is already in Draft";
    private static final String DUMP_MSG = "contains following dices: ";

    private List<Dice> draftList;
    private DiceBag diceBag;
    private int numberDice;

    private static final Logger logger = Logger.getLogger(Draft.class.getName());

    /**
     * Constructor
     * @param diceBag of the game
     * @param numberDice = nPlayer * 2 + 1
     */
    public Draft(DiceBag diceBag, int numberDice) {
        draftList = new ArrayList<>();
        this.diceBag = diceBag;
        this.numberDice = numberDice;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_MSG);
        draftList.forEach(Dice::dump);
    }

    /**
     * Filling draft with numberDice Dices from DiceBag removing dices taken from it
     * @return true if fillDraft is successful
     * @throws EmptyException if there isn't enough dice in the bag, diceBag.size() < (2*nPlayer + 1)
     * @throws IDNotFoundException when try to take a Dice with invalid id
     */
    public boolean fillDraft() throws EmptyException, IDNotFoundException {
        if (diceBag.diceRemaining() < numberDice)
            throw new EmptyException(NOT_ENOUGH_DICE);

        for (int i = 0; i < numberDice; i++) {
            Dice d = diceBag.randDice();

            if (d == null)
                throw new EmptyException(NO_DICE);

            diceBag.rmDice(d);
            draftList.add(d);
        }

        return true;
    }

    /**
     * Rolling all dices in the draft
     */
    public void rollDraft() {
        draftList.forEach(Dice::rollDice);
    }

    /**
     * Number of remaining dice
     * @return draftList.size
     */
    public int diceRemaining() { return this.draftList.size(); }

    /**
     * Find and return Dice with passed id
     * @param id != null && id >= 0 && id <=89
     * @return Dice searched by a passed id
     * @throws IDNotFoundException when copyDice throws IDNotFoundException
     */
    public Dice findDice(int id) throws IDNotFoundException {
        Optional<Dice> ret = draftList.stream()
                .filter(d -> d.getID() == id)
                .collect(Collectors.toList()).stream().findFirst();

        if (ret.isPresent())
            return ret.get().copyDice();
        else
            return null;
    }

    /**
     * @return iterator over dices list in the draft
     */
    public Iterator<Dice> itrDraft() {
        return draftList.iterator();
    }

    /**
     * Clear all Draft
     */
    public void freeDraft() {
        draftList.clear();
    }

    /**
     * Remove dice from the draft
     * @param d dice to be removed
     * @return true if success, false else
     * @throws IDNotFoundException when dice is not in the draft
     * @throws EmptyException when draft is empty
     */
    public boolean rmDice(Dice d) throws IDNotFoundException, EmptyException {
        if (draftList.isEmpty())
            throw new EmptyException(EMPTY_DRAFT);

        Optional<Dice> ret = draftList.stream()
                .filter(dice -> dice.getID() == d.getID())
                .collect(Collectors.toList()).stream().findFirst();

        if (ret.isPresent())
            return draftList.remove(ret.get());

        throw new IDNotFoundException(ID_NOT_FOUND);
    }

    /**
     * Add Dice d to Draft
     * @param d Dice to add
     * @return true if addDice is successful
     * @throws SameDiceException when Dice is already in Draft
     */
    public boolean addDice(Dice d) throws SameDiceException {
        if (draftList.stream().anyMatch(dice -> dice.getID() == d.getID()))
            throw new SameDiceException(ALREADY_IN_DRAFT);

        return draftList.add(d);
    }

    /**
     * @param n number of dices in the draft
     */
    public void setNumberDice(int n) {
        this.numberDice = n;
        }

    /**
     * @return numberDice in the draft
     */
    public int getNumberDice() {
        return this.numberDice;
    }

    /**
     * @return copy of the dices in the draft list
     */
    public List<Dice> getDraftList() {
        List<Dice> ret = new ArrayList<>();

        draftList.forEach(dice -> {
            try {
                ret.add(dice.copyDice());
            } catch (IDNotFoundException e) {
                logger.info(e.getMessage());
            }
        });

        return ret;
    }
}
