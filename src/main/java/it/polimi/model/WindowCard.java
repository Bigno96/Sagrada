package it.polimi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WindowCard {

    private List<Cell> cellList = new ArrayList<Cell>();
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

}
