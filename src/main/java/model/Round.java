package model;

import exception.EmptyException;

import java.util.*;
import java.util.logging.Logger;


public class Round {

    private List<Player> roundList;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Round(){
        roundList = new ArrayList<>();
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

    public void addPlayer(Player p){
        roundList.add(p);
    }

    public void rmPlayer(Player p) throws EmptyException {
        if (roundList.isEmpty()) {
            throw new EmptyException("Round List is empty");
        } else {
            roundList.remove(p);

        }
    }

    public Player nextPlayer(){
        for (Player itr: roundList) {
            if(itr.isFirstTurn()) {
                itr.endFirstTurn();
                return itr;
            }
        }

        for (ListIterator<Player> itr = roundList.listIterator(roundList.size()); itr.hasPrevious();) {
            Player p = itr.previous();
            if (p.isSecondTurn()) {
                p.endSecondTurn();
                return p;
            }
        }

        return null;
    }

    public void nextRound() {
        List<Player> tmp = new ArrayList<>();
        for(ListIterator<Player> itr = roundList.listIterator(1); itr.hasNext();) {
            tmp.add(itr.next());
        }
        tmp.add(roundList.get(0));

        roundList = tmp;

        for(Player p: roundList){
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