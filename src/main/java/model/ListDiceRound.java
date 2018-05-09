package model;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ListDiceRound {

    private List<Dice> listDice;
    private static final Logger logger = Logger.getLogger(ListDiceRound.class.getName());

    public ListDiceRound() {
        listDice = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following dices: ");
        for (Dice d : listDice)
        {
            d.dump();
        }
    }

    public boolean addDice(Dice d) throws SameDiceException {
        for (Dice itr : listDice) {
            if (itr.getID() == d.getID())
                throw new SameDiceException("Dice is already on Round Track");
        }
        return listDice.add(d);
    }

    public boolean addDice(List<Dice> d) throws SameDiceException {
        if (listDice.containsAll(d))
            throw new SameDiceException("Dices are already on Round Track");
        return listDice.addAll(d);
    }

    public boolean rmDice(Dice d) throws EmptyException, IDNotFoundException {
        if (listDice.isEmpty()) {
            throw new EmptyException(this.toString() + "is empty");
        } else {
            for (Dice itr : listDice) {
                if (d.getID() == itr.getID()) {
                    return listDice.remove(itr);
                }
            }
        }
        throw new IDNotFoundException("Id not found");
    }

    public List<Dice> copyListRound() {
        return new ArrayList<>(listDice);
    }

    public Iterator<Dice> itrListRound() {
        return listDice.iterator();
    }

    public Dice getDice(int i){
        return listDice.get(i);
    }
}