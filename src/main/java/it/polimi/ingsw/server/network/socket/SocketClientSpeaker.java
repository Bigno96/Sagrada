package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.net.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

public class SocketClientSpeaker implements Runnable, ClientSpeaker {

    private Socket socket;
    private PrintWriter socketOut;
    private Lobby lobby;
    private static String parse = "parseException";
    private static String print = "print";

    SocketClientSpeaker(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
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
        out.println(username + " is connecting with Socket");

        socketOut.println(print);
        socketOut.println("Connection Established");            // notify client the connection
        socketOut.flush();
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        socketOut.println(print);
        socketOut.println(s);
        socketOut.flush();
    }

    @Override
    public synchronized boolean ping() {
        try {
            int reading = socket.getInputStream().read(new byte[1024], 0, 0);
            if (reading == -1)
                return false;

            socketOut.println("ping");
            socketOut.flush();

            return true;

        } catch (IOException | NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public synchronized void loginSuccess(String s) {
        socketOut.println("loginSuccess");
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

        } catch (SamePlayerException e) {
            socketOut.println(parse);
            socketOut.println("SamePlayerException");
            socketOut.flush();

        } catch (GameAlreadyStartedException e) {
            socketOut.println(parse);
            socketOut.println("GameAlreadyStartedException");
            socketOut.flush();

        } catch (TooManyPlayersException e) {
            socketOut.println(parse);
            socketOut.println("TooManyPlayersException");
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
