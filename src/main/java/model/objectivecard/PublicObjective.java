package model.objectivecard;

import model.windowcard.Cell;

import java.util.logging.Logger;

public class PublicObjective extends ObjectiveCard {

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PublicObjective(int id, String descr, int point, ObjectiveStrategy objStrat) {
        super(id, descr, point, objStrat);
    }

    public void dump() {
        final String logMsg = String.format("id = [%d] descr = [%s] point = [%d]", getId(), getDescr(), getPoint());
        logger.info(logMsg);
    }

}
