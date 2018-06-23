package it.polimi.ingsw.server.model.objectivecard.card;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPoint;
import it.polimi.ingsw.server.model.objectivecard.strategy.CalculatingPublicPoint;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;
import java.util.logging.Logger;

public class PublicObjective implements ObjectiveCard, Serializable {

    private int id;
    private String description;
    private int point;

    private String type;
    private String scope;
    private String grad;
    private String dir;

    private CalculatingPoint strategy;

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public PublicObjective(int id, String description, int point) {
        this.id = id;
        this.description = description;
        this.point = point;
        this.strategy = new CalculatingPublicPoint();
    }

    /**
     * @param type  keyword for json
     * @param scope keyword for json
     * @param grad  keyword for json
     * @param dir   keyword for json
     */
    @Override
    public void setParameter(String type, String scope, String grad, String dir) {
        this.type = type;
        this.scope = scope;
        this.grad = grad;
        this.dir = dir;
    }

    /**
     * @return id of the card
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @return description of the card
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @return value point of the card
     */
    @Override
    public int getPoint() {
        return this.point;
    }

    /**
     * @return json parameter type of this card
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * @return json parameter scope of this card
     */
    @Override
    public String getScope() {
        return this.scope;
    }

    /**
     * @return json parameter gradation of this card
     */
    @Override
    public String getGrad() {
        return this.grad;
    }

    /**
     * @return json parameter direction of this card
     */
    @Override
    public String getDir() {
        return this.dir;
    }

    /**
     * @param windowCard where objective card is applied to
     * @return point provided by the objective card
     * @throws IDNotFoundException when an error in window card identification occurs
     * @throws PositionException when an error in finding positions in the window card occurs
     */
    @Override
    public int calcPoint(WindowCard windowCard) throws IDNotFoundException, PositionException {
        return strategy.calcPoint(windowCard, this);
    }

    @Override
    public void dump() {
        final String logMsg = String.format("id = [%d] description = [%s] point = [%d]", getId(), getDescription(), getPoint());
        logger.info(logMsg);
    }

    /**
     * @return copy of the objective card
     */
    @Override
    public ObjectiveCard copy() {
        PublicObjective ret = new PublicObjective(this.id, this.description, this.point);
        ret.setParameter(this.type, this.scope, this.grad, this.dir);
        return ret;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }
}
