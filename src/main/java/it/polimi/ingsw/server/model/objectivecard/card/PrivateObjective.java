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

    private static final String NULL = "null";

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

    /**
     * @param type  keyword for json
     * @param scope keyword for json
     * @param grad  keyword for json
     * @param dir   keyword for json
     */
    @Override
    public void setParameter(String type, String scope, String grad, String dir) {
        this.type = type;
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
        return 0;
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
        return NULL;
    }

    /**
     * @return json parameter gradation of this card
     */
    @Override
    public String getGrad() {
        return getScope();
    }

    /**
     * @return json parameter direction of this card
     */
    @Override
    public String getDir() {
        return getScope();
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
        PrivateObjective ret = new PrivateObjective(this.id, this.description);
        ret.setParameter(this.type, NULL, NULL, NULL);
        return ret;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }


}
