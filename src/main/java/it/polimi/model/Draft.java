package it.polimi.model;

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

    public void fillDraft() {                   // filling draft with nDice Dices from DiceBag removing dices taken from it
        for (int i = 0; i < nDice; i++) {
            final Dice d = diceBag.randDice();
            draft.add(d);
            diceBag.rmDice(d);
        }
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

    public boolean rmDice(Dice d) { return draft.remove(d); }

    public boolean addDice(Dice d) { return draft.add(d); }

}
