package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ListRound {

    private List<Dice> listRound;
    private static final Logger logger = Logger.getLogger(ListRound.class.getName());

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following dices: ");
        for (Dice d :listRound )
        {
            d.dump();
        }
    }

    public ListRound() {
        listRound = new ArrayList<>();
    }

    public void addDice(Dice d){
        listRound.add(d);
    }

    public void addDice(List<Dice> d){
        listRound.addAll(d);
    }

    public List<Dice> copyListRound() {
        return new ArrayList<>(listRound);
    }

    public Iterator<Dice> itrListRound() {
        return listRound.iterator();
    }

    public boolean rmDice(Dice d) {
        if (listRound.isEmpty()) {
            return false;
        } else {
            return listRound.remove(d);
        }
    }

   public Dice getDice(int i){
        return listRound.get(i);
    }
}