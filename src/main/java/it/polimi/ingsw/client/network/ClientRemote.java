package it.polimi.ingsw.client.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {

    boolean isLogged() throws RemoteException;
    void welcome() throws RemoteException;
}
