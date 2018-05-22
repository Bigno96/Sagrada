package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {

    void login (String user, ClientRemote skeleton) throws RemoteException;
    void logout (String user) throws RemoteException;
}
