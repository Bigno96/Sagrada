package it.polimi.ingsw.client.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
}
