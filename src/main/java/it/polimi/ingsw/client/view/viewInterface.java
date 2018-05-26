package it.polimi.ingsw.client.view;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;

import java.io.FileNotFoundException;
import java.util.List;

public interface viewInterface {
    void print(String s); // s -> general message
    void chooseWindowCard(int id1, int id2) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException; // id1, id2 -> ids windowCard to be chosen
    // saves in the hashmap the choice of the window card made by the user who is passed. user -> username player who has picked the window card. name -> name of the window card that has to be created.
    void sendCardPlayer(String user, String name) throws IDNotFoundException, FileNotFoundException, PositionException, ValueException;
    void printPrivObj(int id); // id -> id of private objective
    void printPublObj(int[] ids); // ids -> 3 ids of public objectives
    void setRound(); // increment local variable of num round in the graphic system
    void isTurn(String username); // username -> user of current player
    // draftValue, draftColor -> lists of combination value-color of the dices in the draft
    void showDraft(List<Integer> draftId, List<Integer> draftValue, List<String> draftColor) throws IDNotFoundException, SameDiceException;
    // notify when a dice is placed. username -> user of the player who placed the dice. row,col -> coordinates of the position of the dice. color,value -> attributes of the dice
    void placementDice(String username, int row, int col, String color, int value);
    void startGraphic() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException, SameDiceException; // method used for asking connection
}
