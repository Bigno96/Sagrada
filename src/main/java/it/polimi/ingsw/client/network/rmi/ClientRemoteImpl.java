package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote {

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
    public void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException, RemoteException {
       view.chooseWindowCard(cards);
    }

    @Override
    public void showCardPlayer(String user, WindowCard card) throws RemoteException, FileNotFoundException, IDNotFoundException, PositionException, ValueException {
       view.showCardPlayer(user, card);
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) throws RemoteException {
        view.placementDice(username, dest, moved);
    }
}
