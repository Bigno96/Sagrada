package model;

import java.util.*;
import java.util.logging.Logger;

import exception.IDNotFoundException;
import exception.WrongPositionException;
import exception.EmptyException;

public class WindowCard {

    private List<Cell> cellList = new ArrayList<>();
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

    public Cell getCell(int pos) throws IDNotFoundException {
        for (Cell c : cellList) {
            if (c.getPos() == pos)
                return c;
        }
        throw new IDNotFoundException("Id was not found");
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

    /*public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        Boolean first = true;

        for (Cell c : cellList) {
            if (c.isOccupied() && first) {
                first = false;
                if (!c.isBorder())
                    throw new WrongPositionException ("First Dice not correctly positioned");
                if (!c.checkColor())
                    throw new WrongPositionException("Color not correct on cell: " + c.toString());
                if (!c.checkValue())
                    throw new WrongPositionException("Value not correct on cell: " + c.toString());
            }
            else if (c.isOccupied() && !first)
                throw new WrongPositionException( "More than one dice positioned");
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOneDice() throws EmptyException, WrongPositionException {
        Boolean first = true;

        for (Cell c : cellList) {
            if (c.isOccupied() && first) {
                first = false;
                if (!c.checkColor())
                    throw new WrongPositionException("Color not correct on cell: " + c.toString());
                if (!c.checkValue())
                    throw new WrongPositionException("Value not correct on cell: " + c.toString());
            }
            else if (c.isOccupied() && !first)
                return false;
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    */

    /*public boolean checkOrtogonalPos(Cell c) throws IDNotFoundException {

        Set<Integer> neighbors = new HashSet<>();
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
            if (cellList.get(i).getDice() != null) {
                if (cellList.get(pos).getDice().getValue() == cellList.get(i).getDice().getValue() || cellList.get(pos).getDice().getColor().equals(cellList.get(i).getDice().getColor()))
                    return false;
            }
        }

        return true;
    }*/

    /*public boolean checkPlaceCond() throws WrongPositionException, EmptyException, IDNotFoundException {
        if(!checkOneDice()) {
            for (Cell c : cellList) {
                if (c.isOccupied()) {
                    if (!c.checkColor())
                        throw new WrongPositionException("Color not correct on cell: " + c.toString());
                    else if (!c.checkValue())
                        throw new WrongPositionException("Value not correct on cell: " + c.toString());
                    else if (!checkOrtogonalPos(c)) {
                        throw new WrongPositionException("Position not correct on cell " + c.toString());
                    }
                }
            }
        }
        return true;
    }*/

}
