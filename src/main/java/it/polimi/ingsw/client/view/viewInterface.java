package it.polimi.ingsw.client.view;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;

import java.io.FileNotFoundException;
import java.util.List;

public interface viewInterface {
    void print(String s); // s -> general message
    void chooseWindowCard(int[] ids) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException; // ids -> 4 ids windowCard to be chosen
    void sendCardPlayers(int[] ids); // ids -> 3 ids windowCard other players
    void printPrivObj(int id); // id -> id of private objective
    void printPublObj(int[] ids); // ids -> 3 ids of public objectives
    void setRound(); // increment local variable of num round in the graphic system
    void isTurn(String username); // username -> user of current player
    // draftValue, draftColor -> lists of combination value-color of the dices in the draft
    void printDraft(List<Integer> draftValue, List<String> draftColor);
    // notify when a dice is placed. username -> user of the player who placed the dice. row,col -> coordinates of the position of the dice. color,value -> attributes of the dice
    void placementDice(String username, int row, int col, String color, int value);
    void startGraphic(); // method used for asking connection
}
