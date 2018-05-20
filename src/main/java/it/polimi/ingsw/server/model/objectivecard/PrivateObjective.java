package it.polimi.ingsw.server.model.objectivecard;

import it.polimi.ingsw.server.model.windowcard.Cell;

import java.util.logging.Logger;

public class PrivateObjective extends ObjectiveCard {

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    /**
     * Constructor
     * @param id
     * @param descr
     * @param objStrat
     */

    public PrivateObjective(int id, String descr, ObjectiveStrategy objStrat) {
        super(id, descr, objStrat);
    }

    @Override
    public void dump() {
        logger.info("id = " + getId() + " descr = " + getDescr());
    }

}
