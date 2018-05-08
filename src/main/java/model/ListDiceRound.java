package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ListDiceRound {

    private List<Dice> listDiceRound;
    private static final Logger logger = Logger.getLogger(ListDiceRound.class.getName());

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following dices: ");
        for (Dice d : listDiceRound)
        {
            d.dump();
        }
    }

    public ListDiceRound() {
        listDiceRound = new ArrayList<>();
    }

    public void addDice(Dice d){
        listDiceRound.add(d);
    }

    public void addDice(List<Dice> d){
        listDiceRound.addAll(d);
    }

    public List<Dice> copyListRound() {
        return new ArrayList<>(listDiceRound);
    }

    public Iterator<Dice> itrListRound() {
        return listDiceRound.iterator();
    }

    public boolean rmDice(Dice d) {
        if (listDiceRound.isEmpty()) {
            return false;
        } else {
            return listDiceRound.remove(d);
        }
    }

   public Dice getDice(int i){
        return listDiceRound.get(i);
    }
}