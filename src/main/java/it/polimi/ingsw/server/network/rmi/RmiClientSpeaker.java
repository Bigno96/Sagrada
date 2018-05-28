package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.List;

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

    @Override
    public void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        client.chooseWindowCard(cards);
    }
}
