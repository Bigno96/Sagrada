package it.polimi.ingsw.server.network;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
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

    void placementDice(String username, Cell dest, Dice moved) throws  RemoteException;

    void printWindowCard(WindowCard card) throws RemoteException, IDNotFoundException;

    void showDraft(Draft draft) throws RemoteException, IDNotFoundException, SameDiceException;

    void printPublObj(List<ObjectiveCard> pubObj) throws RemoteException;

    void printPrivObj(ObjectiveCard privObj) throws RemoteException;

    void print(String s) throws RemoteException;
}
