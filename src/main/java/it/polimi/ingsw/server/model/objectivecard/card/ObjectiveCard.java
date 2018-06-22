package it.polimi.ingsw.server.model.objectivecard.card;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;

public interface ObjectiveCard {

    void setParameter(String type, String scope, String grad, String dir);
    int getId();
    String getDescription();
    int getPoint();
    String getType();
    String getScope();
    String getGrad();
    String getDir();
    int calcPoint(WindowCard windowCard) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException;
    void dump();
    ObjectiveCard copy();
}
