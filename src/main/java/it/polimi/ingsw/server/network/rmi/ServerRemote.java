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

    void setWindowCard(String userName, String name) throws RemoteException;

    void askWindowCard(String userName) throws RemoteException, IDNotFoundException;

    void askUsers(String currUser) throws RemoteException;

    void askDraft(String username) throws RemoteException, IDNotFoundException, SameDiceException;

    void askPublObj(String username) throws RemoteException;

    void askPrivObj(String username) throws RemoteException;

    void moveDiceFromDraftToCard(String username, int index, int row, int col) throws  RemoteException;
}
