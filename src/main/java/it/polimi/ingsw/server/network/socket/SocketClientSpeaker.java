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

    private static final String SEND_LIST_CARD_KEYWORD = "SEND_LIST_CARD";
    private static final String SHOW_USER_CARD_KEYWORD = "SHOW_USER_CARD";
    private static final String SHOW_DRAFT_KEYWORD = "SHOW_DRAFT";
    private static final String MAKE_DRAFT_KEYWORD = "MAKE_DRAFT";
    private static final String PRINT_CARD_KEYWORD = "PRINT_CARD";
    private static final String NEXT_TURN_KEYWORD = "NEXT_TURN";
    private static final String SHOW_PUBLIC_OBJ_KEYWORD = "SHOW_PUBLIC_OBJ";
    private static final String SHOW_PRIVATE_OBJ_KEYWORD = "SHOW_PRIVATE_OBJ";

    private static final String CARD_NAME_KEYWORD = "CARD_NAME";
    private static final String CARD_ID_KEYWORD = "CARD_ID";
    private static final String CARD_FAVOR_POINT_KEYWORD = "CARD_FAVOR_POINT";
    private static final String CARD_CELL_LIST_KEYWORD = "CARD_CELL_LIST";
    private static final String CARD_KEYWORD = "CARD";
    private static final String LIST_CARD_KEYWORD = "LIST_CARD";

    private static final String CELL_VALUE_KEYWORD = "CELL_VALUE";
    private static final String CELL_COLOR_KEYWORD = "CELL_COLOR";
    private static final String CELL_ROW_KEYWORD = "CELL_ROW";
    private static final String CELL_COL_KEYWORD = "CELL_COL";
    private static final String CELL_KEYWORD = "CELL";

    private static final String DICE_ID_KEYWORD = "DICE_ID";
    private static final String DICE_COLOR_KEYWORD = "DICE_COLOR";
    private static final String DICE_VALUE_KEYWORD = "DICE_VALUE";
    private static final String DICE_DRAFT_KEYWORD = "DICE_DRAFT";
    private static final String DICE_KEYWORD = "DICE";

    private static final String OBJ_ID_KEYWORD = "OBJ_ID";
    private static final String OBJ_DESCRIPTION_KEYWORD = "OBJ_DESCRIPTION";
    private static final String OBJ_POINT_KEYWORD = "OBJ_POINT";
    private static final String MAKE_PUBLIC_LIST_KEYWORD = "MAKE_PUBLIC_LIST";
    private static final String MAKE_PUBLIC_OBJ_KEYWORD = "MAKE_PUBLIC_OBJ";

    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";

    private Socket socket;
    private PrintWriter socketOut;
    private Lobby lobby;

    private final Object lock;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    SocketClientSpeaker(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
        this.lock = new Object();

        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new SocketClientListener(socket, this, lobby));

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
    public void tell(String s) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(PRINT_KEYWORD));
            socketOut.println(s);
            socketOut.flush();
        }
    }

    /**
     * @return true if pong is true in return, else false
     */
    @Override
    public boolean ping() {
        synchronized (lock) {
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
    }

    /**
     * @param s message of success for login
     */
    @Override
    public void loginSuccess(String s) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(LOGIN_SUCCESS_KEYWORD));
            socketOut.println(s);
            socketOut.flush();
        }
    }

    /**
     * Add player to the Lobby. Catch and flush exception messages.
     * @param username to be logged in
     */
     public void login(String username) {
        try {
            lobby.addPlayer(username, this);

        } catch (SamePlayerException | GameAlreadyStartedException | TooManyPlayersException e) {
            synchronized (lock) {
                socketOut.println(protocol.getMessage(EXCEPTION_KEYWORD));
                socketOut.println(e.getClass().toString());
                socketOut.flush();
            }
        }
    }

    /**
     * Deconstruct Window Card for passing through socket
     * @param card to be deconstructed
     * @param type if it's a single card, or one of the card of a list
     */
    private void deconstructCard(WindowCard card, String type) {
        socketOut.println(protocol.getMessage(CARD_NAME_KEYWORD));
        socketOut.println(card.getName());

        socketOut.println(protocol.getMessage(CARD_ID_KEYWORD));
        socketOut.println(card.getId());

        socketOut.println(protocol.getMessage(CARD_FAVOR_POINT_KEYWORD));
        socketOut.println(card.getNumFavPoint());

        socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));
        socketOut.println(" ");

        card.getHorizontalItr().forEachRemaining(cell -> {
            socketOut.println(protocol.getMessage(CELL_VALUE_KEYWORD));
            socketOut.println(cell.getValue());

            socketOut.println(protocol.getMessage(CELL_COLOR_KEYWORD));
            socketOut.println(cell.getColor());

            socketOut.println(protocol.getMessage(CELL_ROW_KEYWORD));
            socketOut.println(cell.getRow());

            socketOut.println(protocol.getMessage(CELL_COL_KEYWORD));
            socketOut.println(cell.getCol());

            socketOut.println(protocol.getMessage(CELL_KEYWORD));
            socketOut.println(" ");
        });

        socketOut.println(protocol.getMessage(type));
        socketOut.println(" ");
    }

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void sendWindowCard(List<WindowCard> cards) {
        synchronized (lock) {
            cards.forEach(card -> deconstructCard(card, LIST_CARD_KEYWORD));

            socketOut.println(protocol.getMessage(SEND_LIST_CARD_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(OTHER_USER_NAME_KEYWORD));
            socketOut.println(user);

            deconstructCard(card, CARD_KEYWORD);

            socketOut.println(protocol.getMessage(SHOW_USER_CARD_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param user = game.getCurrentPlayer().getId()
     */
    @Override
    public void nextTurn(String user) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(NEXT_TURN_KEYWORD));
            socketOut.println(user);
            socketOut.flush();
        }
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {

    }

    /**
     * @param card to be printed
     */
    @Override
    public void printWindowCard(WindowCard card) {
        synchronized (lock) {
            deconstructCard(card, CARD_KEYWORD);

            socketOut.println(protocol.getMessage(PRINT_CARD_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Deconstruct Dice for passing through socket
     * @param dice to be deconstructed
     * @param type if it's a single dice, or one of the dice of a list
     */
    private void deconstructDice(Dice dice, String type) {
        socketOut.println(protocol.getMessage(DICE_ID_KEYWORD));
        socketOut.println(dice.getID());

        socketOut.println(protocol.getMessage(DICE_VALUE_KEYWORD));
        socketOut.println(dice.getValue());

        socketOut.println(protocol.getMessage(DICE_COLOR_KEYWORD));
        socketOut.println(dice.getColor());

        socketOut.println(protocol.getMessage(type));
        socketOut.println(" ");
    }

    /**
     * @param draft of the current round
     */
    @Override
    public void showDraft(Draft draft) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(MAKE_DRAFT_KEYWORD));
            socketOut.println(" ");

            draft.getDraftList().forEach(dice -> deconstructDice(dice, DICE_DRAFT_KEYWORD));

            socketOut.println(protocol.getMessage(SHOW_DRAFT_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printPublicObj(List<ObjectiveCard> publicObj) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(MAKE_PUBLIC_LIST_KEYWORD));
            socketOut.println(" ");

            publicObj.forEach(obj -> {
                socketOut.println(protocol.getMessage(OBJ_ID_KEYWORD));
                socketOut.println(obj.getId());

                socketOut.println(protocol.getMessage(OBJ_DESCRIPTION_KEYWORD));
                socketOut.println(obj.getDescription());

                socketOut.println(protocol.getMessage(OBJ_POINT_KEYWORD));
                socketOut.println(obj.getPoint());

                socketOut.println(protocol.getMessage(MAKE_PUBLIC_OBJ_KEYWORD));
                socketOut.println(" ");
            });

            socketOut.println(protocol.getMessage(SHOW_PUBLIC_OBJ_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(OBJ_ID_KEYWORD));
            socketOut.println(privateObj.getId());

            socketOut.println(protocol.getMessage(OBJ_DESCRIPTION_KEYWORD));
            socketOut.println(privateObj.getDescription());

            socketOut.println(protocol.getMessage(SHOW_PRIVATE_OBJ_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }
}
