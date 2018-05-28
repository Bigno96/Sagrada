package it.polimi.ingsw.server.network;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
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

    void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException;
}
