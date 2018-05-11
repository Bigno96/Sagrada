package model.windowCard;

import java.util.*;
import java.util.logging.Logger;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.WrongPositionException;
import exception.EmptyException;

public class WindowCard {

    private MatrixCell window;
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

    public WindowCard (int id, String name, int numFavPoint, MatrixCell matrix){
        this.id = id;
        this.name = name;
        this.numFavPoint = numFavPoint;
        window = new MatrixCell(matrix);
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

    public Iterator<Cell> getVertItr() {
        return window.itrVert();
    }

    public boolean checkFirstDice() throws WrongPositionException, EmptyException {
        Boolean first = true;


        for (Iterator<Cell> itr = window.itrOrizz(); itr.hasNext(); ) {
            Cell c = itr.next();
            if (c.isOccupied() && first) {

                first = false;
                if (!window.isBorder(c))
                    throw new WrongPositionException("First Dice not correctly positioned");
                if (!c.checkColor())
                    throw new WrongPositionException("Color not correct on cell: " + c.toString());
                if (!c.checkValue())
                    throw new WrongPositionException("Value not correct on cell: " + c.toString());

            }
            else if (c.isOccupied() && !first)
                throw new WrongPositionException("More than one dice positioned");

        }

        if (first)
            throw new EmptyException("No Dice positioned");

        return true;
    }

    public boolean checkOneDice() throws EmptyException, WrongPositionException {
        Boolean first = true;
        Iterator<Cell> itr = window.itrOrizz();

        while (itr.hasNext()) {
            Cell c = itr.next();
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

    public boolean checkOrtPos(Cell c) throws PositionException, IDNotFoundException {

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
        Iterator<Cell> itr = window.itrOrizz();
        if(!checkOneDice()) {
            while (itr.hasNext()) {
                Cell c = itr.next();
                if (c.isOccupied()) {
                    if (!c.checkColor())
                        throw new WrongPositionException("Color not correct on cell: " + c.toString());
                    else if (!c.checkValue())
                        throw new WrongPositionException("Value not correct on cell: " + c.toString());
                    else if (!checkOrtPos(c))
                        throw new WrongPositionException("Position not correct on cell " + c.toString());
                    else if (!checkNeighbors(c))
                        throw new WrongPositionException("Position not correct on cell (no dice around) " + c.toString());
                }
            }
        }
        return true;
    }



}
