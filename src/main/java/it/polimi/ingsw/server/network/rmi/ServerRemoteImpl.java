package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;

import java.rmi.RemoteException;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    public ServerRemoteImpl() {
    }

    @Override
    public void login(String user, ClientRemote client) {
        try {
            out.println("User " + client.getUsername() + " is logging in with RMI");
            client.tell("Connection established\nWelcome " + client.getUsername());
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void tell(String s) {
        out.println(s);
    }

}
