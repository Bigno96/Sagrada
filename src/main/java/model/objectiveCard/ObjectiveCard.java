package model.objectiveCard;

import exception.IDNotFoundException;
import exception.ValueException;
import model.windowCard.WindowCard;

import java.io.FileNotFoundException;

public interface ObjectiveCard {

    int calcPoint(WindowCard winCard) throws ValueException, FileNotFoundException, IDNotFoundException;
    int getId();
    String getDescr();
    int getFP();
    int getPoint();
    void setFP(int fp);
    void dump();
    String toString();

}
