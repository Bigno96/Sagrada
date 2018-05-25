package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {
    // remote interface of server used by client

    /**
     * Used to connect a client to the server. No username check are made here.
     * @param user != null
     * @param client instance of ClientRemote, permits server to talk back to client
     * @throws RemoteException default
     */
    void connect(String user, ClientRemote client) throws RemoteException;

    /**
     * Used to print on Server
     * @param s to be printed
     * @throws RemoteException default
     */
    void tell(String s) throws RemoteException;

    /**
     * Used to add Player to the lobby pre game. Checks for username validity.
     * @param user != null
     * @param client instance of ClientRemote
     * @throws RemoteException default
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws SamePlayerException when trying to login same player twice
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    void addPlayer(String user, ClientRemote client) throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException;
}
