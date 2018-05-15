package it.polimi.ingsw.model.windowcard;

import java.util.*;
import java.util.logging.Logger;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.WrongPositionException;
import it.polimi.ingsw.exception.EmptyException;

public class WindowCard {

    private MatrixCell window;
    private int id;                 // id it's the same for 2 window card that represents front and behind of a real Window Card
    private int numFavPoint;
    private String name;
    private static String colorMsg = "Color not correct on cell: ";
    private static String valueMsg = "Value not correct on cell: ";

    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());

    public WindowCard (int id, String name, int numFavPoint, List<Cell> cellList){
        final int rows = 4;
        final int cols = 5;
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

    public MatrixCell getWindow() {
        return window;
    }

    @Override
    public String toString() {
    return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("ID: " + getId() + " Name: " + getName() + " NumFavPoints: " + getNumFavPoint());
        Iterator<Cell> itr = window.itrOrizz();
        while (itr.hasNext())
            itr.next().dump();
    }

    public Iterator<Cell> getOrizzItr() {
        return window.itrOrizz();
    }

    public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        if (checkOneDice()) {
            for (Iterator<Cell> itr = window.itrOrizz(); itr.hasNext();) {
                Cell c = itr.next();
                if (c.isOccupied() && !window.isBorder(c)) {
                    throw new WrongPositionException("First Dice not correctly positioned");
                }
            }
        } else
            throw new WrongPositionException("More than one dice positioned");

        return true;
    }

    public boolean checkOneDice() throws EmptyException, WrongPositionException {
        Boolean first = true;

        for (Iterator<Cell> itr = window.itrOrizz(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.isOccupied() && first) {
                first = false;
                if (!c.checkColor())
                    throw new WrongPositionException(colorMsg + c.toString());
                if (!c.checkValue())
                    throw new WrongPositionException(valueMsg + c.toString());
            }
            else if (c.isOccupied() && !first)
                return false;
        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOrtCol(Cell c, List<Cell> cellList) throws IDNotFoundException {
        for (Cell cell : cellList)
            if (cell.getDice() != null)
                if (!cell.isIgnoreColor() && c.getDice().getColor().equals(cell.getDice().getColor()))
                    return false;

        return true;
    }

    public boolean checkOrtVal(Cell c, List<Cell> cellList) throws IDNotFoundException {
        for (Cell cell : cellList)
            if (cell.getDice() != null)
                if (!cell.isIgnoreValue() && c.getDice().getValue() == cell.getDice().getValue())
                    return false;

        return true;
    }

    public boolean checkOrtPos(Cell c) throws IDNotFoundException, PositionException {
        List<Cell> cellList = window.retOrtogonal(c.getRow(), c.getCol());

        for (Cell cell : cellList)
            if (cell.getDice() != null) {
                if (!c.isIgnoreValue() && !checkOrtVal(c, cellList))
                    return false;
                if (!c.isIgnoreColor() && !checkOrtCol(c, cellList))
                    return false;
            }

        return true;
    }

    public boolean checkNeighbors(Cell c) throws PositionException {
        if (c.isIgnoreNearby())
            return true;

        List<Cell> cellList = window.retNeighbors(c.getRow(), c.getCol());

        for (Cell cell : cellList)
            if (cell.isOccupied())
                return true;

        return false;
    }

    public boolean checkPlaceCond() throws WrongPositionException, IDNotFoundException, PositionException {
        for (Iterator<Cell> itr = window.itrOrizz(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.isOccupied()) {
                if (!c.checkColor())
                    throw new WrongPositionException(colorMsg + c.toString());
                else if (!c.checkValue())
                    throw new WrongPositionException(valueMsg + c.toString());
                else if (!checkOrtPos(c))
                    throw new WrongPositionException("Position not correct on cell " + c.toString());
                else if (!checkNeighbors(c))
                    throw new WrongPositionException("Position not correct on cell (no dice around) " + c.toString());
            }
        }

        return true;
    }

}
