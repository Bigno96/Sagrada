package model;

import java.util.*;
import java.util.logging.Logger;


public class Round {

    private List<Player> roundList;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Round(){
        roundList = new ArrayList<Player>();
    }


    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following players: ");
        for (Player p : roundList)
        {
            p.dump();
        }
    }

    public boolean addPlayer(Player p){
        return roundList.add(p);
    }

    public boolean rmPlayer(Player p) {
        if (roundList.isEmpty()) {
            return false;
        } else {
            roundList.remove(p);
            return true;
        }
    }

    public Player nextTurn(){

        for(Player itr: roundList){
            if(itr.isFirstTurn()){
                itr.endFirstTurn();
                return itr;
            }
        }
        if(roundList.get(roundList.size()-1).isSecondTurn()){
            roundList.get(roundList.size()-1).endSecondTurn();
            return roundList.get(roundList.size()-1);
        }else {
            for (ListIterator<Player> itr = roundList.listIterator(roundList.size()); itr.hasPrevious(); itr.previous()) {
                if (itr.previous().isSecondTurn()) {
                    itr.previous().endSecondTurn();
                    return itr.previous();
                }
            }
        }

        return null;
    }

    public void nextRound() throws NullPointerException{
        Player firstIsLast = null;
        firstIsLast = roundList.get(0);
        roundList.add(firstIsLast);
        Player first = roundList.get(0);
        rmPlayer(first);

        Player p = null;
        for(Player itr: roundList){
            try {
                p.resetFirstTurn();
            }catch (NullPointerException e){
                throw new NullPointerException("Null pointer exception");
            }
            try {
                p.resetSecondTurn();
            }catch (NullPointerException e){
                throw new NullPointerException("Null pointer exception");
            }
        }

    }
}