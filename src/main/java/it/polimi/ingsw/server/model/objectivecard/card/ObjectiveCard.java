package it.polimi.ingsw.server.model.objectivecard.card;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

public interface ObjectiveCard {

    /**
     * Used by parser to set parameter for objective calculator
     * @param type keyword for json
     * @param scope keyword for json
     * @param grad keyword for json
     * @param dir keyword for json
     */
    void setParameter(String type, String scope, String grad, String dir);

    /**
     * @return id of the card
     */
    int getId();

    /**
     * @return description of the card
     */
    String getDescription();

    /**
     * @return value point of the card
     */
    int getPoint();

    /**
     * @return json parameter type of this card
     */
    String getType();

    /**
     * @return json parameter scope of this card
     */
    String getScope();

    /**
     * @return json parameter gradation of this card
     */
    String getGrad();

    /**
     * @return json parameter direction of this card
     */
    String getDir();

    /**
     * Used to calculate point for the objective card
     * @param windowCard where objective card is applied to
     * @return point provided by the objective card
     * @throws IDNotFoundException when an error in window card identification occurs
     * @throws PositionException when an error in finding positions in the window card occurs
     */
    int calcPoint(WindowCard windowCard) throws IDNotFoundException, PositionException;

    void dump();

    /**
     * @return copy of the objective card
     */
    ObjectiveCard copy();
}
