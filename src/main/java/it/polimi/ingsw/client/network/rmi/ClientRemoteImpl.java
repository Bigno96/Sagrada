package it.polimi.ingsw.client.network.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.*;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote, Serializable {

    private String username;

    ClientRemoteImpl(String username) throws RemoteException {
        super();
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
        out.println(s);
    }

    /**
     * @return this username
     */
    @Override
    public String getUsername() {
        return this.username;
    }
}
