package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.server.network.parser.CommunicationParser;

import java.net.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

/**
 * Send messages towards the client via socket
 */
public class SocketClientSpeaker implements Runnable, ClientSpeaker {

    private Socket socket;
    private PrintWriter socketOut;
    private Lobby lobby;
    private CommunicationParser communication;

    SocketClientSpeaker(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
        this.communication = new CommunicationParser();
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new SocketClientListener(socket, this));

        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Used to connect player to the Server.
     * @param username to be connected
     */
    synchronized void connect(String username) {
        out.println(username + communication.getMessage("CONNECTION_WITH_SOCKET"));

        socketOut.println(communication.getMessage("PRINT"));
        socketOut.println(communication.getMessage("CONNECTION_SUCCESS"));            // notify client the connection
        socketOut.flush();
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        socketOut.println(communication.getMessage("PRINT"));
        socketOut.println(s);
        socketOut.flush();
    }

    /**
     * @return true if pong is true in return, else false
     */
    @Override
    public synchronized boolean ping() {
        try {
            int reading = socket.getInputStream().read(new byte[8], 0, 0);
            if (reading == -1)
                return false;

            socketOut.println(communication.getMessage("PING"));
            socketOut.flush();

            return true;

        } catch (IOException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * @param s message of success for login
     */
    @Override
    public synchronized void loginSuccess(String s) {
        socketOut.println(communication.getMessage("LOGIN_SUCCESS"));
        socketOut.println(s);
        socketOut.flush();
    }

    /**
     * Add player to the Lobby. Catch and flush exception messages.
     * @param username to be logged in
     */
    synchronized void login(String username) {
        try {
            lobby.addPlayer(username, this);

        } catch (SamePlayerException | GameAlreadyStartedException | TooManyPlayersException e) {
            socketOut.println(communication.getMessage("EXCEPTION"));
            socketOut.println(e.getClass().toString());
            socketOut.flush();
        }
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) throws RemoteException, FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) throws RemoteException {

    }

}
