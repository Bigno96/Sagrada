package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface of client used by server
 */
public interface ClientRemote extends Remote {

    /**
     * Used to print on client
     * @param s to be printed
     * @throws RemoteException default
     */
    void tell(String s) throws RemoteException;

    /**
     * Used to ping the client
     * @return true
     * @throws RemoteException default
     */
    boolean ping() throws RemoteException;

    /**
     * Used to get username of the client
     * @return String username
     * @throws RemoteException default
     */
    String getUsername() throws RemoteException;

    void chooseWindowCard(List<WindowCard> cards) throws RemoteException;

    void showCardPlayer(String user, WindowCard card) throws RemoteException;

    void placementDice(String username, Cell dest, Dice moved) throws  RemoteException;

    void printWindowCard(WindowCard card) throws RemoteException, IDNotFoundException;

    void showDraft(Draft draft) throws RemoteException, IDNotFoundException, SameDiceException;

    void printPublObj(List<ObjectiveCard> pubObj) throws RemoteException;

    void printPrivObj(ObjectiveCard privObj) throws RemoteException;

    void print(String s) throws RemoteException;
}
