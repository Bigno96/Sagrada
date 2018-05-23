package it.polimi.ingsw.client.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {

    void tell(String s) throws RemoteException;
    String getUsername() throws RemoteException;
}
