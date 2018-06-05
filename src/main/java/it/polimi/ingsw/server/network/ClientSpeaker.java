package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;

public interface ClientSpeaker {
    // interface to hide network difference in comm Server -> Client

    /**
     * Used to print on Client
     * @param s to be printed
     */
    void tell(String s);

    /**
     * Check if the client is connected
     * @return true if pong is true in return, else false
     */
    boolean ping();

    /**
     * @param s message of success for login
     */
    void loginSuccess(String s);

    void chooseWindowCard(List<WindowCard> cards);

    void showCardPlayer(String user, WindowCard card);

    void placementDice(String username, Cell dest, Dice moved);

    void printWindowCard(WindowCard card);

    void showDraft(Draft draft);

    void printPublObj(List<ObjectiveCard> pubObj);

    void printPrivObj(ObjectiveCard privObj);
}
