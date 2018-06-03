package it.polimi.ingsw.server.model.objectivecard.card;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPoint;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPublicPoint;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class PublicObjective implements ObjectiveCard {

    private int id;
    private String descr;
    private int point;
    private String type;
    private String scope;
    private String grad;
    private String dir;
    private CalculatingPoint strategy;
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PublicObjective(int id, String descr, int point) {
        this.id = id;
        this.descr = descr;
        this.point = point;
        this.strategy = new CalculatingPublicPoint();
    }

    @Override
    public void setParameter(String type, String scope, String grad, String dir) {
        this.type = type;
        this.scope = scope;
        this.grad = grad;
        this.dir = dir;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getDescr() {
        return this.descr;
    }

    @Override
    public int getPoint() {
        return this.point;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public String getGrad() {
        return this.grad;
    }

    @Override
    public String getDir() {
        return this.dir;
    }

    @Override
    public int calcPoint(WindowCard windowCard) throws IDNotFoundException, PositionException {
        return strategy.calcPoint(windowCard, this);
    }

    @Override
    public void dump() {
        final String logMsg = String.format("id = [%d] descr = [%s] point = [%d]", getId(), getDescr(), getPoint());
        logger.info(logMsg);
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }
}
