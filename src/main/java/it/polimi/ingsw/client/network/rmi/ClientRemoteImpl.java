package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote {

    private String username;
    private final ViewInterface view;

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
     * @return true
     */
    @Override
    public boolean ping() {
        return true;
    }

    /**
     * @return this username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        view.chooseWindowCard(cards);
    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {
        view.showCardPlayer(user, card);
    }

    @Override
    public void nextTurn(String user) {
        view.isTurn(user);
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {
        view.placementDice(username, dest, moved);
    }

    @Override
    public void printWindowCard(WindowCard card) {
        view.printWindowCard(card);
    }

    @Override
    public void showDraft(Draft draft) throws RemoteException, IDNotFoundException, SameDiceException {
        view.showDraft(draft.getDraftList());
    }

    @Override
    public void printPublObj(List<ObjectiveCard> pubObj) throws RemoteException {
        view.printPublObj(pubObj);
    }

    @Override
    public void printPrivObj(ObjectiveCard privObj) throws RemoteException {
        view.printPrivObj(privObj);
    }

    @Override
    public void print(String s) throws RemoteException {
        view.print(s);
    }
}
