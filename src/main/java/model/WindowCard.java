package model;

import java.util.*;
import java.util.logging.Logger;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.WrongPositionException;
import exception.EmptyException;

public class WindowCard {

    private MatrixCell window = null;
    private int id;                 // id it's the same for 2 window card that represents front and behind of a real Window Card
    private int numFavPoint;
    private String name;
    private static int rows = 4;
    private static int cols = 5;

    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public WindowCard (int id, String name, int numFavPoint, List<Cell> cellList){
        this.id = id;
        this.name = name;
        this.numFavPoint = numFavPoint;
        window = new MatrixCell(rows, cols);
        this.window.loadMatrixCell(cellList);
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
        Iterator<Cell> itr = window.getIterator();
        while (itr.hasNext())
            itr.next().dump();
    }

    public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        Boolean first = true;
        Iterator<Cell> itr = window.getIterator();

        while (itr.hasNext()) {
            if (itr.next().isOccupied() && first) {
                first = false;
                if (!window.isBorder(itr.next()))
                    throw new WrongPositionException ("First Dice not correctly positioned");
                if (!itr.next().checkColor())
                    throw new WrongPositionException("Color not correct on cell: " + itr.next().toString());
                if (!itr.next().checkValue())
                    throw new WrongPositionException("Value not correct on cell: " + itr.next().toString());
            }
            else if (itr.next().isOccupied() && !first)
                throw new WrongPositionException( "More than one dice positioned");
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOneDice() throws EmptyException, WrongPositionException {
        Boolean first = true;
        Iterator<Cell> itr = window.getIterator();

        while (itr.hasNext()) {
            if (itr.next().isOccupied() && first) {
                first = false;
                if (!itr.next().checkColor())
                    throw new WrongPositionException("Color not correct on cell: " + itr.next().toString());
                if (!itr.next().checkValue())
                    throw new WrongPositionException("Value not correct on cell: " + itr.next().toString());
            }
            else if (itr.next().isOccupied() && !first)
                return false;
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOrtPos(Cell c) throws IDNotFoundException, PositionException {

        List<Cell> cellList = window.retOrtogonal(c.getRow(), c.getCol());

        for (Cell cell : cellList) {
            if (cell.getDice() != null) {
                if (c.getDice().getValue() == cell.getDice().getValue() || c.getDice().getColor().equals(cell.getDice().getColor()))
                    return false;
            }
        }

        return true;
    }

    public boolean checkNeighbors(Cell c) throws PositionException {

        List<Cell> cellList = window.retNeighbors(c.getRow(), c.getCol());

        for (Cell cell : cellList) {
            if (cell.isOccupied()) {
                return true;
            }
        }

        return false;
    }

    public boolean checkPlaceCond() throws WrongPositionException, EmptyException, IDNotFoundException, PositionException {
        Iterator<Cell> itr = window.getIterator();
        if(!checkOneDice()) {
            while (itr.hasNext()) {
                if (itr.next().isOccupied()) {
                    if (!itr.next().checkColor())
                        throw new WrongPositionException("Color not correct on cell: " + itr.next().toString());
                    else if (!itr.next().checkValue())
                        throw new WrongPositionException("Value not correct on cell: " + itr.next().toString());
                    else if (!checkOrtPos(itr.next()))
                        throw new WrongPositionException("Position not correct on cell " + itr.next().toString());
                    else if (!checkNeighbors(itr.next()))
                        throw new WrongPositionException("Position not correct on cell (no dice around) " + itr.next().toString());
                }
            }
        }
        return true;
    }



}
