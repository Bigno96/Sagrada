package it.polimi.ingsw.server.network;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientSpeaker {
    // interface to hide network difference in comm Server -> Client

    /**
     * Used to print on Client
     * @param s to be printed
     */
    void tell(String s);

    /**
     * Check if the client is connected
     * @return true if pong is true in return, else false
     */
    boolean ping();

    /**
     * @param s message of success for login
     */
    void loginSuccess(String s);

    void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException;

    void showCardPlayer(String user, WindowCard card) throws RemoteException, FileNotFoundException, IDNotFoundException, PositionException, ValueException;

    void placementDice(String username, Cell dest, Dice moved) throws  RemoteException;
}
