package it.polimi.ingsw.client.view;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.util.List;

public interface ViewInterface {
    void print(String s); // s -> general message
    void startGraphic() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException; // method used for asking connection
    void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException; // id1, id2 -> ids windowCard to be chosen
    // saves in the hashmap the choice of the window card made by the user who is passed. user -> username player who has picked the window card. name -> name of the window card that has to be created.
    void setCardPlayer(String user, WindowCard card) throws IDNotFoundException, FileNotFoundException, PositionException, ValueException;
    void printPrivObj(PrivateObjective privObj); // id -> id of private objective
    void printPublObj(List<PublicObjective> publObj); // ids -> 3 ids of public objectives
    void setRound(); // increment local variable of num round in the graphic system
    void isTurn(String username); // username -> user of current player
    // draftValue, draftColor -> lists of combination value-color of the dices in the draft
    void showDraft(List<Dice> draft) throws IDNotFoundException, SameDiceException;
    // notify when a dice is placed. username -> user of the player who placed the dice. row,col -> coordinates of the position of the dice. color,value -> attributes of the dice
    void placementDice(String username, Cell dest, Dice moved);
}
