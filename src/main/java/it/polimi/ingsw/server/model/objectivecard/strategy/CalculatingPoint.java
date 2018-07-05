package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;

public interface CalculatingPoint extends Serializable {

    /**
     * Used to calc point coming from an objective card
     * @param windowCard where to apply the objective card
     * @param objectiveCard to apply
     * @return sum of points
     * @throws IDNotFoundException when error occurs in finding objective card
     * @throws PositionException when error occurs in finding cells in window card
     */
    int calcPoint(WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException, PositionException;
}
