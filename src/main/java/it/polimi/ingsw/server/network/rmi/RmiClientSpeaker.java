package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.rmi.RemoteException;
import java.util.List;
import java.util.SortedMap;

import static java.lang.System.*;

/**
 * Implementation of Rmi version of client speaker
 */
public class RmiClientSpeaker implements ClientSpeaker {

    private ClientRemote client;

    public RmiClientSpeaker(ClientRemote client) {
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

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void sendWindowCard(List<WindowCard> cards) {
        try {
            client.chooseWindowCard(cards);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {
        try {
            client.showCardPlayer(user, card);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param user = game.getCurrentPlayer().getId()
     */
    @Override
    public void nextTurn(String user) {
        try {
            client.nextTurn(user);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param username of player moving the dice
     * @param dest cell where the dice is being moved
     * @param moved dice being moved
     */
    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        try {
            client.successfulPlacementDice(username, dest, moved);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param card to be printed
     */
    @Override
    public void printWindowCard(WindowCard card) {
        try {
            client.printWindowCard(card);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param draft of the current round
     */
    @Override
    public void showDraft(Draft draft) {
        try {
            client.showDraft(draft);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printPublicObj(List<ObjectiveCard> publicObj) {
        try {
            client.printPublicObj(publicObj);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        try {
            client.printPrivateObj(privateObj);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param ranking sorted map of player username and their points through the game
     */
    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {
        try {
            client.printRanking(ranking);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

}
