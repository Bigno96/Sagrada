package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.rmi.RemoteException;

import static java.lang.System.*;

public class RmiClientSpeaker implements ClientSpeaker {

    private ClientRemote client;

    RmiClientSpeaker(ClientRemote client) {
        this.client = client;
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        try {
            client.tell(s);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public synchronized boolean ping() {
        try {
            return client.ping();
        } catch (RemoteException e) {
            return false;
        }
    }
}
