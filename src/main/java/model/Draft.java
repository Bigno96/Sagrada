package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Draft {

    private List<Dice> draft;
    private DiceBag diceBag;
    private int nDice;
    private static final Logger logger = Logger.getLogger(Draft.class.getName());

    public Draft(DiceBag diceBag, int nDice) {
        draft = new ArrayList<>();
        this.diceBag = diceBag;
        this.nDice = nDice;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following dices: ");
        for (Dice d : draft)
        {
            d.dump();
        }
    }

    public boolean fillDraft() throws EmptyException, IDNotFoundException {                   // filling draft with nDice Dices from DiceBag removing dices taken from it
        for (int i = 0; i < nDice; i++) {
            final Dice d = diceBag.randDice();
            if (d == null)
                throw new EmptyException("No Dice in Bag");
            draft.add(d);
            if (!diceBag.rmDice(d))
                throw new IDNotFoundException("No dice with that id");
        }
        return true;
    }

    public void rollDraft() {                   // rollling all dices in the draft
        for (final Dice d : draft) {
            d.rollDice();
        }
    }

    public int diceRemaining() { return this.draft.size(); }

    public Dice findDice(int id) throws IDNotFoundException {         // find and return Dice with passed id
        for (Dice d : draft)
        {
            if (d.getID() == id) {
                return d.copyDice();
            }
        }
        return null;
    }

    public Iterator<Dice> itrDraft() {
        return draft.iterator();
    }

    public List<Dice> copyDraft() {
        return new ArrayList<>(draft);
    }

    public void freeDraft() {
        draft.clear();
    }

    public void rmDice(Dice d) throws IDNotFoundException, EmptyException {
        if (draft.isEmpty()) {
            throw new EmptyException("Draft is empty");
        } else {
            for (Dice dice : draft) {
                if (d.getID() == dice.getID()) {
                    draft.remove(dice);
                    return;
                }
            }

        }
        throw new IDNotFoundException("Id not found");
    }

    public void addDice(Dice d) throws SameDiceException {
        for (Dice itr : draft) {
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already in Draft");
        }
        draft.add(d);
    }

    public void setnDice(int n) { this.nDice = n; }

    public int getnDice() { return this.nDice; }

}
