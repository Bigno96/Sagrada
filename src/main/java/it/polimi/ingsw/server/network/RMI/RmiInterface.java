package it.polimi.ingsw.server.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiInterface extends Remote {
    //methods client will use and server will implement
    boolean login (String usr) throws RemoteException;
    boolean logout (String usr) throws RemoteException;
}
