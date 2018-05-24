package it.polimi.ingsw.client.network.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.*;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote, Serializable {

    private String username;

    public ClientRemoteImpl(String username) throws RemoteException {
        super();
        this.username = username;
    }

    @Override
    public void tell(String s) {
        out.println(s);
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
