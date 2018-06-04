package it.polimi.ingsw.client.network;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public interface ServerSpeaker {
    // interface to hide network difference in comm Client -> Server

    /**
     * Used locally to change ip after a failed connection toward server
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    void setIp(String ip);

    /**
     * Used to connect client to server. No control on username yet.
     * @param username != null
     * @return true if connection was successful, false else
     */
    boolean connect(String username);

    /**
     * Used to login client to server. Username and other restrictions are controlled.
     * @param username != null
     * @return true if login was successful, false else
     */
    boolean login(String username);

    void setWindowCard(String username, String name) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException, RemoteException;

    void askWindowCard(String username) throws RemoteException, IDNotFoundException;

    void askUsers(String currUser) throws RemoteException;

    void askDraft(String username) throws RemoteException, IDNotFoundException, SameDiceException;

    void askPublObj(String username) throws RemoteException;

    void askPrivObj(String username) throws RemoteException;

    void endTurn(String username);

    void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException;

}

