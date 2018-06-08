package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {
    // remote interface of server used by client

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

    void setWindowCard(String username, String cardName) throws RemoteException;

    void getWindowCard(String usernameWanted, String me) throws RemoteException;

    void getAllUsername(String currUser) throws RemoteException;

    void getDraft(String username) throws RemoteException;

    void endTurn(String username) throws RemoteException;

    void getPublObj(String username) throws RemoteException;

    void getPrivObj(String username) throws RemoteException;

    void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException;
}
