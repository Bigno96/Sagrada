package it.polimi.ingsw.server.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {

    void login (String user) throws RemoteException;
    void logout (String user) throws RemoteException;
}
