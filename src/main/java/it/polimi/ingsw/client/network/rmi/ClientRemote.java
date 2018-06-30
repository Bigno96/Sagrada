package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

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

    /**
     * Used to ask user what card does he want from the 4 selected for him
     * @param cards cards.size() = 4
     * @throws RemoteException default
     */
    void chooseWindowCard(List<WindowCard> cards) throws RemoteException;

    /**
     * Used to tell which card a certain player has chosen
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     * @throws RemoteException default
     */
    void showCardPlayer(String user, WindowCard card) throws RemoteException;

    /**
     * Used to notify whose turn is
     * @param user = game.getCurrentPlayer()
     * @throws RemoteException default
     */
    void nextTurn(String user) throws RemoteException;

    /**
     * Used to place dice
     * @param username of player moving the dice
     * @param dest cell where the dice is being moved
     * @param moved dice being moved
     * @throws RemoteException default
     */
    void successfulPlacementDice(String username, Cell dest, Dice moved) throws RemoteException;

    /**
     * Used to print a window card
     * @param card to print
     * @throws RemoteException default
     */
    void printWindowCard(WindowCard card) throws RemoteException;

    /**
     * Used to show current round draft pool
     * @param draft draft.size() > 0 && draft.size() <= game.getNumberPlayer() * 2 +1
     * @throws RemoteException default
     */
    void showDraft(Draft draft) throws RemoteException;

    /**
     * Used to show public objective cards
     * @param publicObj publicObj.size() = 3
     * @throws RemoteException default
     */
    void printPublicObj(List<ObjectiveCard> publicObj) throws RemoteException;

    /**
     * Used to show private card to player owner
     * @param privateObj = Player.getPrivateObj()
     * @throws RemoteException default
     */
    void printPrivateObj(ObjectiveCard privateObj) throws RemoteException;
}
