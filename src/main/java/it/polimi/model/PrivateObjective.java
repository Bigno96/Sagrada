package it.polimi.model;

import java.util.logging.Logger;

public class PrivateObjective implements ObjectiveCard {

    private int id;
    private String descr;
    private int fp;
    private ObjectiveStrategy objStrat;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PrivateObjective(int id, String descr, ObjectiveStrategy objStrat) {
        this.id = id;
        this.descr = descr;
        this.fp = 0;
        this.objStrat = objStrat;
    }

    public void dump() {
        logger.info("id = " + getId() + " descr = " + getDescr());
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
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

    public int getPoint() {
        return 0;
    }

    public void setFP(int fp) {
        this.fp = fp;
    }

    public int effect(WindowCard winCard) {
        return 0;
    }
}
