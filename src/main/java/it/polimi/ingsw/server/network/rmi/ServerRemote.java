package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface of server used by client
 */
public interface ServerRemote extends Remote {

    /**
     * Used to connect a client to the server.
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     * @throws RemoteException default
     */
    void connect(String username, ClientRemote client) throws RemoteException;

    /**
     * Used to print on Server
     * @param s to be printed
     * @throws RemoteException default
     */
    void tell(String s) throws RemoteException;

    /**
     * Used to add Player to the lobby pre game.
     * @param username != null
     * @param client instance of ClientRemote
     * @throws RemoteException default
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    void login(String username, ClientRemote client) throws RemoteException, TooManyPlayersException, GameAlreadyStartedException, SamePlayerException;

    /**
     * Used to set Window Card chosen as Player's window card
     * @param username of Player that requested
     * @param cardName of card to be set
     * @throws RemoteException default
     */
    void setWindowCard(String username, String cardName) throws RemoteException;

    /**
     * Used to obtain Window Card of usernameWanted player and show it to me player
     * @param usernameWanted = Player.getId() && Player.getWindowCard()
     * @param me = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getWindowCard(String usernameWanted, String me) throws RemoteException;

    /**
     * Used to get all username of Player in the game
     * @param currentUser = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getAllUsername(String currentUser) throws RemoteException;

    /**
     * Used to get draft for current round
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getDraft(String username) throws RemoteException;

    /**
     * Used to end the turn
     * @param username = Player.getId() of who ended turn
     * @throws RemoteException default
     */
    void endTurn(String username) throws RemoteException;

    /**
     * Used to get Public Objectives selected for current game
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getPublicObj(String username) throws RemoteException;

    /**
     * Used to get Private Objective of a Player. Only works if requested by the Objective's owner
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getPrivateObj(String username) throws RemoteException;

    void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException;
}
