package it.polimi.model;

import java.util.ArrayList;
import java.util.List;

public class WindowCard {

    private static List<Cell> cellList = new ArrayList<Cell>(20);
    private int id;
    private int numFavPoint;
    private String name;

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
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getId() + " Name: " + getName() + " NumFavPoints: " + getNumFavPoint();
    }

    public String dump()
    {
        return "ID: " + getId() + " Name: " + getName() + " NumFavPoints: " + getNumFavPoint();
    }



}
