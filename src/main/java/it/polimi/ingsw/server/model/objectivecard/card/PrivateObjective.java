package it.polimi.ingsw.server.model.objectivecard.card;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPoint;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPrivatePoint;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;
import java.util.logging.Logger;

public class PrivateObjective implements ObjectiveCard, Serializable {

    private int id;
    private String description;
    private String type;
    private CalculatingPoint strategy;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PrivateObjective(int id, String description) {
        this.id = id;
        this.description = description;
        this.strategy = new CalculatingPrivatePoint();
    }

    @Override
    public void setParameter(String type, String scope, String grad, String dir) {
        this.type = type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getPoint() {
        return 0;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public String getGrad() {
        return null;
    }

    @Override
    public String getDir() {
        return null;
    }

    @Override
    public int calcPoint(WindowCard windowCard) throws IDNotFoundException, PositionException {
        return strategy.calcPoint(windowCard, this);
    }

    @Override
    public void dump() {
        final String logMsg = String.format("id = [%d] description = [%s] point = [%d]", getId(), getDescription(), getPoint());
        logger.info(logMsg);
    }

    @Override
    public ObjectiveCard copy() {
        PrivateObjective ret = new PrivateObjective(this.id, this.description);
        ret.setParameter(this.type, "null", "null", "null");
        return ret;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }


}
