package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientRemote extends Remote {
    // remote interface of client used by server

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

    void chooseWindowCard(List<WindowCard> cards) throws RemoteException, FileNotFoundException, IDNotFoundException, PositionException, ValueException;
}
