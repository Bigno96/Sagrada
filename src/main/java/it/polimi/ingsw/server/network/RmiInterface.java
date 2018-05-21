package it.polimi.ingsw.server.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiInterface extends Remote {
    //methods client will use and server will implement
    void login (String usr);
    void logout (String usr);
}
