package it.polimi.model;

import java.util.logging.Logger;

public class PublicObjective implements ObjectiveCard {

    private int id;
    private String descr;
    private int point;
    private int fp;
    private ObjectiveStrategy objStrat;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PublicObjective(int id, String descr, int point, ObjectiveStrategy objStrat) {
        this.id = id;
        this.descr = descr;
        this.point = point;
        this.fp = 0;
        this.objStrat = objStrat;
    }

    public void dump() {
        logger.info("id = " + getId() + " descr = " + getDescr() + " point = " + getPoint());
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public int getPoint() {
        return point;
    }

    public String getDescr() {
        return descr;
    }

    public int getId() {
        return id;
    }

    public int getFP() {
        return fp;
    }

    public void setFP(int fp) {
        this.fp = fp;
    }

    public int effect(WindowCard winCard) {
        return 0;
    }
}
