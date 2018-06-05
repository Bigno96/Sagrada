package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

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
    public void placementDice(String username, Cell dest, Dice moved) {
        try {
            client.placementDice(username, dest, moved);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void printWindowCard(WindowCard card) {
        try {
            client.printWindowCard(card);
        } catch (RemoteException | IDNotFoundException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void showDraft(Draft draft) {
        try {
            client.showDraft(draft);
        } catch (RemoteException | IDNotFoundException | SameDiceException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void printPublObj(List<ObjectiveCard> pubObj) {
        try {
            client.printPublObj(pubObj);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void printPrivObj(ObjectiveCard privObj) {
        try {
            client.printPrivObj(privObj);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

}
