package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {

    void login(String user, ClientRemote skeleton) throws RemoteException;
    void tell(String s) throws RemoteException;
}
