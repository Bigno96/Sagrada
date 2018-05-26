package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.view.ViewInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote, Serializable {

    private String username;
    private ViewInterface view;

    ClientRemoteImpl(String username, ViewInterface view) throws RemoteException {
        super();
        this.view = view;
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @param s to be printed
     */
    @Override
    public void tell(String s) {
        view.print(s);
    }

    /**
     * @return this username
     */
    @Override
    public String getUsername() {
        return this.username;
    }
}
