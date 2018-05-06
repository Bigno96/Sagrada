package model;

import exception.EmptyException;

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
        draft = new ArrayList<Dice>();
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

    public boolean fillDraft() throws EmptyException {                   // filling draft with nDice Dices from DiceBag removing dices taken from it
        for (int i = 0; i < nDice; i++) {
            final Dice d = diceBag.randDice();
            if (d == null)
                throw new EmptyException("No Dice in Draft");
            if (!draft.add(d))
                return false;
            if (!diceBag.rmDice(d))
                return false;
        }
        return true;
    }

    public void rollDraft() {                   // rollling all dices in the draft
        for (final Dice d : draft) {
            d.rollDice();
        }
    }

    public int diceRemaining() { return this.draft.size(); }

    public Dice findDice(int id) {         // find and return Dice with passed id
        for (final Dice d : draft)
        {
            if (d.getID() == id) {
                return d;
            }
        }
        return null;
    }

    public Iterator<Dice> itrDraft() {
        return draft.iterator();
    }

    public boolean rmDice(Dice d) {
        if (draft.isEmpty()) {
            return false;
        } else {
            return draft.remove(d);
        }
    }

    public boolean addDice(Dice d) {
        for (final Dice itr : draft) {
            if (itr.equals(d))
                return false;
        }
        return draft.add(d);
    }

    public void setnDice(int n) { this.nDice = n; }

    public int getnDice() { return this.nDice; }

}
