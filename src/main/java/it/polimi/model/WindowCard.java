package it.polimi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import exception.WrongPositionException;
import exception.EmptyException;
import java.util.Set;
import java.util.HashSet;

public class WindowCard {

    protected List<Cell> cellList = new ArrayList<Cell>();
    private int id;                 // id it's the same for 2 window card that represents front and behind of a real Window Card
    private int numFavPoint;
    private String name;
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public WindowCard (int id, String name, int numFavPoint, List<Cell> cellList){
        this.id = id;
        this.name = name;
        this.numFavPoint = numFavPoint;
        this.cellList.addAll(cellList);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumFavPoint() {
        return numFavPoint;
    }

    @Override
    public String toString() {
    return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("ID: " + getId() + " Name: " + getName() + " NumFavPoints: " + getNumFavPoint());
        for (Cell c : cellList)
            c.dump();
    }

    public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        Boolean first = true;

        for (Cell c : cellList) {
            if (c.isOccupied() && first) {
                first = false;
                if (!c.isBorder())
                    throw new WrongPositionException ("First Dice not correctly positioned");
            }
            else if (c.isOccupied() && !first)
                throw new WrongPositionException( "More than one dice positioned");
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOneDice() throws EmptyException {
        Boolean first = true;

        for (Cell c : cellList) {
            if (c.isOccupied() && first) {
                first = false;
            }
            else if (c.isOccupied() && !first)
                return false;
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOrtogonalPos(Cell c) {

        Set<Integer> neighbors = new HashSet<Integer>();
        int pos = c.getPos();

        if (pos == 0){
            neighbors.add(1);
            neighbors.add(5);
        }
        else if(pos == 4){
            neighbors.add(3);
            neighbors.add(9);
        }
        else if(pos == 15){
            neighbors.add(10);
            neighbors.add(16);
        }
        else if(pos == 19){
            neighbors.add(14);
            neighbors.add(18);
        }
        else if (pos > 0 && pos < 4) {
            neighbors.add(pos-1);
            neighbors.add(pos+1);
            neighbors.add(pos+5);
        }
        else if (pos == 5 || pos == 10) {
            neighbors.add(pos-5);
            neighbors.add(pos+1);
            neighbors.add(pos+5);
        }
        else if (pos == 9 || pos == 14) {
            neighbors.add(pos-5);
            neighbors.add(pos-1);
            neighbors.add(pos+5);
        }
        else if (pos > 15 && pos < 19) {
            neighbors.add(pos-5);
            neighbors.add(pos-1);
            neighbors.add(pos+1);
        }
        else{
            neighbors.add(pos-5);
            neighbors.add(pos-1);
            neighbors.add(pos+1);
            neighbors.add(pos+5);
        }

        for (int i : neighbors) {
            if(cellList.get(pos).getValue() == cellList.get(i).getValue() || cellList.get(pos).getColor().equals(cellList.get(i).getColor()))
               return true;
        }

        return false;
    }

    public boolean checkPlaceCond() throws WrongPositionException, EmptyException {
        if(!checkOneDice()) {
            for (Cell c : cellList) {
                if (c.isOccupied()) {
                    if (!(c.checkValue() && c.checkColor())) {
                        throw new WrongPositionException("Color or value not correct");
                    } else if (!checkOrtogonalPos(c)) {
                        throw new WrongPositionException("Position not correct");
                    }
                }
            }
        }
        return true;
    }

}
