package model.objectivecard;

import exception.IDNotFoundException;
import exception.PositionException;
import exception.ValueException;
import model.windowcard.WindowCard;

import java.io.FileNotFoundException;

public interface ObjectiveCard {

    int calcPoint(WindowCard winCard) throws ValueException, FileNotFoundException, IDNotFoundException, PositionException;
    int getId();
    String getDescr();
    int getFP();
    int getPoint();
    void setFP(int fp);
    void dump();
    String toString();

}
