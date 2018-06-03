package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

public interface CalculatingPoint {
    int calcPoint(WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException, PositionException;
}
