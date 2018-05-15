package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.model.windowcard.Cell;
import it.polimi.ingsw.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class ObjectiveCard {

    private int id;
    private String descr;
    private int point;
    private ObjectiveStrategy objStrat;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public ObjectiveCard(int id, String descr, ObjectiveStrategy objStrat) {
        this.id = id;
        this.descr = descr;
        this.objStrat = objStrat;
    }

    public ObjectiveCard(int id, String descr, int point, ObjectiveStrategy objStrat) {
        this.id = id;
        this.descr = descr;
        this.point = point;
        this.objStrat = objStrat;
    }

    public int getId() {
        return id;
    }

    public String getDescr() {
        return descr;
    }

    public int calcPoint(WindowCard winCard) throws ValueException, FileNotFoundException, IDNotFoundException, PositionException {
        return objStrat.calcPoint(this, winCard);
    }

    public int getPoint() {
        return this.point;
    }

    public void dump() {
        final String logMsg = String.format("id = [%d] descr = [%s] point = [%d]", getId(), getDescr(), getPoint());
        logger.info(logMsg);
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }
}
