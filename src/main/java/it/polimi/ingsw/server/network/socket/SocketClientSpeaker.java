package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

/**
 * Implementation of Socket version of client speaker
 */
public class SocketClientSpeaker implements Runnable, ClientSpeaker {

    private static final String SOCKET_CONNECTION_KEYWORD = "CONNECTION_WITH_SOCKET";
    private static final String PRINT_KEYWORD = "PRINT";
    private static final String PING_KEYWORD = "PING";
    private static final String CONNECTION_SUCCESS_KEYWORD = "CONNECTION_SUCCESS";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String EXCEPTION_KEYWORD = "EXCEPTION";

    private Socket socket;
    private PrintWriter socketOut;
    private Lobby lobby;

    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    SocketClientSpeaker(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
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
        out.println(username + protocol.getMessage(SOCKET_CONNECTION_KEYWORD));

        socketOut.println(protocol.getMessage(PRINT_KEYWORD));
        socketOut.println(dictionary.getMessage(CONNECTION_SUCCESS_KEYWORD));            // notify client the connection
        socketOut.flush();
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        socketOut.println(protocol.getMessage(PRINT_KEYWORD));
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

            socketOut.println(protocol.getMessage(PING_KEYWORD));
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
        socketOut.println(protocol.getMessage(LOGIN_SUCCESS_KEYWORD));
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
            socketOut.println(protocol.getMessage(EXCEPTION_KEYWORD));
            socketOut.println(e.getClass().toString());
            socketOut.flush();
        }
    }

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void sendWindowCard(List<WindowCard> cards) {

    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {

    }

    /**
     * @param user = game.getCurrentPlayer().getId()
     */
    @Override
    public void nextTurn(String user) {

    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {

    }

    /**
     * @param card to be printed
     */
    @Override
    public void printWindowCard(WindowCard card) {

    }

    /**
     * @param draft of the current round
     */
    @Override
    public void showDraft(Draft draft) {

    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printPublicObj(List<ObjectiveCard> publicObj) {

    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {

    }
}
