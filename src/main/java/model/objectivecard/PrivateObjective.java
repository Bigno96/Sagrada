package model.objectivecard;

import model.windowcard.Cell;

import java.util.logging.Logger;

public class PrivateObjective extends ObjectiveCard {

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PrivateObjective(int id, String descr, ObjectiveStrategy objStrat) {
        super(id, descr, objStrat);
    }

    public void dump() {
        logger.info("id = " + getId() + " descr = " + getDescr());
    }

}