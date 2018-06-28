package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of socket version of server speaker
 */
public class SocketServerSpeaker implements ServerSpeaker {

    private static final String USER_CONNECTING_KEYWORD = "USER_CONNECTING";
    private static final String CONNECT_KEYWORD = "CONNECT";
    private static final String LOGIN_KEYWORD = "LOGIN";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String PRINT_KEYWORD = "PRINT";

    private static final String SET_CARD_KEYWORD = "SET_CARD";
    private static final String GET_CARD_KEYWORD = "GET_CARD";

    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";
    private static final String USER_NAME_KEYWORD = "USER_NAME";
    private static final String GET_ALL_USER_KEYWORD = "GET_ALL_USER";

    private static final String ASK_DRAFT_KEYWORD = "ASK_DRAFT";
    private static final String ASK_PUBLIC_OBJ_KEYWORD = "ASK_PUBLIC_OBJ";
    private static final String ASK_PRIVATE_OBJ_KEYWORD = "ASK_PRIVATE_OBJ";

    private static final String END_TURN_KEYWORD = "END_TURN";

    private String ip;
    private Socket socket;
    private static PrintWriter socketOut;
    private Boolean logged;

    private final ViewInterface view;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    private final Object lock;

    public SocketServerSpeaker(String ip, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.logged = null;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.lock = new Object();
    }

    /**
     * Interrupts this thread
     */
    void interrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * set Logged
     * @param logged true if user logged
     */
    void setLogged(Boolean logged) {
        this.logged = logged;
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean connect(String username) {
        NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();

        view.print(dictionary.getMessage(USER_CONNECTING_KEYWORD) + ip);

        try {
            socket = new Socket(ip, parser.getSocketPort());
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(new SocketServerListener(socket, view, this));

            synchronized (lock) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println(protocol.getMessage(CONNECT_KEYWORD));       // asking for connection
                socketOut.println(username);                                    // username passed
                socketOut.flush();
            }

            return true;

        } catch(IOException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean login(String username) {
        try {
            synchronized (lock) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println(protocol.getMessage(LOGIN_KEYWORD));             // ask for login
                socketOut.println(username);                                        // username passed
                socketOut.flush();
            }

            synchronized (this) {
                while (logged == null)              // while server hasn't responded
                    wait(100);
            }

            if (!logged)
                return false;

            synchronized (lock) {
                socketOut.println(protocol.getMessage(PRINT_KEYWORD));
                socketOut.println("User " + username + " " + protocol.getMessage(LOGIN_SUCCESS_KEYWORD));      // inform server login was successful
                socketOut.flush();
            }

            return true;

        } catch (IOException | InterruptedException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username = Player.getId()
     * @param cardName = Player.getWindowCard().getName()
     */
    @Override
    public void setWindowCard(String username, String cardName) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(SET_CARD_KEYWORD));
            socketOut.println(cardName);

            socketOut.flush();
        }
    }

    /**
     * @param usernameWanted = Player.getId()
     * @param me             username of player that requested
     */
    @Override
    public void askWindowCard(String usernameWanted, String me) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(OTHER_USER_NAME_KEYWORD));
            socketOut.println(usernameWanted);

            socketOut.println(protocol.getMessage(GET_CARD_KEYWORD));
            socketOut.println(me);

            socketOut.flush();
        }
    }

    /**
     * @param currentUser username of player that requested
     */
    @Override
    public void getAllUsername(String currentUser) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(GET_ALL_USER_KEYWORD));
            socketOut.println(currentUser);

            socketOut.flush();
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askDraft(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_DRAFT_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askPublicObj(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_PUBLIC_OBJ_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

    /**
     * @param username = Player.getId() && Player.getPrivateObj()
     */
    @Override
    public void askPrivateObj(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_PRIVATE_OBJ_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

    @Override
    public void askToolCards(String username) {

    }

    @Override
    public void askFavorPoints(String username) {

    }

    /**
     * @param username of Player that wants to end his turn
     */
    @Override
    public void endTurn(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(END_TURN_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

    /**
     * @param username of player moving the dice
     * @param index    in the draft of the dice
     * @param row      of the destination cell
     * @param col      of the destination cell
     */
    @Override
    public void placementDice(String username, int index, int row, int col) {

    }

}
