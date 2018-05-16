package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Draft {

    private List<Dice> draftList;
    private DiceBag diceBag;
    private int nDice;
    private static final Logger logger = Logger.getLogger(Draft.class.getName());

    public Draft(DiceBag diceBag, int nDice) {
        draftList = new ArrayList<>();
        this.diceBag = diceBag;
        this.nDice = nDice;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following dices: ");
        for (Dice d : draftList)
            d.dump();
    }

    public boolean fillDraft() throws EmptyException, IDNotFoundException {                   // filling draft with nDice Dices from DiceBag removing dices taken from it
        for (int i = 0; i < nDice; i++) {
            final Dice d = diceBag.randDice();
            if (d == null)
                throw new EmptyException("No Dice in Bag");
            if (diceBag.diceRemaining() < nDice)
                throw new EmptyException("Not Enough Dices in Bag");
            diceBag.rmDice(d);
            draftList.add(d);
        }

        return true;
    }

    public void rollDraft() {                   // rollling all dices in the draft
        for (final Dice d : draftList) {
            d.rollDice();
        }
    }

    public int diceRemaining() { return this.draftList.size(); }

    public Dice findDice(int id) throws IDNotFoundException {         // find and return Dice with passed id
        for (Dice d : draftList)
            if (d.getID() == id)
                return d.copyDice();

        return null;
    }

    public Iterator<Dice> itrDraft() {
        return draftList.iterator();
    }

    public List<Dice> copyDraft() {
        return new ArrayList<>(draftList);
    }

    public void freeDraft() {
        draftList.clear();
    }

    public boolean rmDice(Dice d) throws IDNotFoundException, EmptyException {
        if (draftList.isEmpty()) {
            throw new EmptyException("Draft is empty");
        } else {
            for (Dice dice : draftList)
                if (d.getID() == dice.getID())
                    return draftList.remove(dice);
        }

        throw new IDNotFoundException("Id not found");
    }

    public boolean addDice(Dice d) throws SameDiceException {
        for (Dice itr : draftList)
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already in Draft");

        return draftList.add(d);
    }

    public void setnDice(int n) { this.nDice = n; }

    public int getnDice() { return this.nDice; }

}
