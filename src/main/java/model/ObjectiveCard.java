package model;

public interface ObjectiveCard {

    int effect(WindowCard winCard);
    int getId();
    String getDescr();
    int getFP();
    int getPoint();
    void setFP(int fp);
    void dump();
    String toString();

}
