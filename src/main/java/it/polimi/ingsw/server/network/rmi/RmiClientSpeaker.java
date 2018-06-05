package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
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

    /**
     * Check if the client is connected
     * @return true if pong is true in return, else false
     */
    @Override
    public synchronized boolean ping() {
        try {
            return client.ping();
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * @param s message of success for login
     */
    @Override
    public void loginSuccess(String s) {
        try {
            client.tell(s);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        try {
            client.chooseWindowCard(cards);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {
        try {
            client.showCardPlayer(user, card);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) throws RemoteException {
        client.placementDice(username, dest, moved);
    }

    @Override
    public void printWindowCard(WindowCard card) throws RemoteException, IDNotFoundException {
        client.printWindowCard(card);
    }

    @Override
    public void showDraft(Draft draft) throws RemoteException, IDNotFoundException, SameDiceException {
        client.showDraft(draft);
    }

    @Override
    public void printPublObj(List<ObjectiveCard> pubObj) throws RemoteException {
        client.printPublObj(pubObj);
    }

    @Override
    public void printPrivObj(ObjectiveCard privObj) throws RemoteException {
        client.printPrivObj(privObj);
    }

    @Override
    public void print(String s) throws RemoteException {
        client.print(s);
    }
}
