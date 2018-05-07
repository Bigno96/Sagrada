package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ListRound {

    private List<Dice> listRound;
    private int nTurn;
    private Draft draft;
    private Dice dice;
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

    public ListRound(){
        listRound = new ArrayList<Dice>();
    }

    public void addDice(){
        for(Iterator<Dice> itr = draft.itrDraft(); itr.hasNext(); itr.next()){
            dice = draft.findDice(itr.next().getID());
            listRound.add(dice);
            draft.rmDice(dice);
        }
    }

    public List<Dice> getListRound(){
        return this.listRound;
    }

    public boolean rmDice(Dice d) {
        if (listRound.isEmpty()) {
            return false;
        } else {
            return listRound.remove(d);
        }
    }

   public Dice getDice(int i){
        dice = listRound.get(i);
        return dice;
    }
}