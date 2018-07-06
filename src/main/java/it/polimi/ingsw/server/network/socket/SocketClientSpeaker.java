package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
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
    private static final String EXCEPTION_MESSAGE_KEYWORD = "EXCEPTION_MESSAGE";

    private static final String SEND_LIST_CARD_KEYWORD = "SEND_LIST_CARD";
    private static final String SHOW_USER_CARD_KEYWORD = "SHOW_USER_CARD";
    private static final String SHOW_DRAFT_KEYWORD = "SHOW_DRAFT";
    private static final String SHOW_ROUND_TRACK_KEYWORD = "SHOW_ROUND_TRACK";
    private static final String MAKE_DRAFT_KEYWORD = "MAKE_DRAFT";
    private static final String PRINT_CARD_KEYWORD = "PRINT_CARD";
    private static final String NEXT_TURN_KEYWORD = "NEXT_TURN";
    private static final String SHOW_PUBLIC_OBJ_KEYWORD = "SHOW_PUBLIC_OBJ";
    private static final String SHOW_PRIVATE_OBJ_KEYWORD = "SHOW_PRIVATE_OBJ";
    private static final String SHOW_TOOL_CARD_KEYWORD = "SHOW_TOOL_CARD";
    private static final String SUCCESSFUL_PLACE_DICE_KEYWORD = "SUCCESSFUL_PLACE_DICE";
    private static final String SHOW_FAVOR_POINT_KEYWORD = "SHOW_FAVOR_POINT";

    private static final String CARD_NAME_KEYWORD = "CARD_NAME";
    private static final String CARD_ID_KEYWORD = "CARD_ID";
    private static final String FAVOR_POINT_KEYWORD = "FAVOR_POINT";
    private static final String CARD_CELL_LIST_KEYWORD = "CARD_CELL_LIST";
    private static final String CARD_KEYWORD = "CARD";
    private static final String LIST_CARD_KEYWORD = "LIST_CARD";
    private static final String OCCUPIED_CELL_KEYWORD = "OCCUPIED_CELL";

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

    private static final String TOOL_ID_KEYWORD = "TOOL_ID";
    private static final String TOOL_NAME_KEYWORD = "TOOL_NAME";
    private static final String MAKE_TOOL_LIST_KEYWORD = "MAKE_TOOL_LIST";
    private static final String MAKE_TOOL_CARD_KEYWORD = "MAKE_TOOL_CARD";
    private static final String ADD_TOOL_CARD_LIST_KEYWORD = "ADD_TOOL_CARD_LIST";
    private static final String USED_TOOL_CARD_KEYWORD = "USED_TOOL_CARD";

    private static final String SET_RET_KEYWORD = "SET_RET";

    private static final String SET_ACTOR_KEYWORD = "SET_ACTOR";
    private static final String END_SET_ACTOR_KEYWORD = "END_SET_ACTOR";
    private static final String WINDOW_CARD_ACTOR_KEYWORD = "WINDOW_CARD_ACTOR";
    private static final String DRAFT_ACTOR_KEYWORD = "DRAFT_ACTOR";
    private static final String ROUND_TRACK_ACTOR_KEYWORD = "ROUND_TRACK_ACTOR";

    private static final String SET_DICE_KEYWORD = "SET_DICE";
    private static final String SET_CELL_KEYWORD = "SET_CELL";
    private static final String SET_COLOR_KEYWORD = "SET_COLOR";

    private static final String SET_PARAMETER_KEYWORD = "SET_PARAMETER";
    private static final String END_SET_PARAMETER_KEYWORD = "END_SET_PARAMETER";
    private static final String DICE_PARAMETER_KEYWORD = "DICE_PARAMETER";
    private static final String CELL_PARAMETER_KEYWORD = "CELL_PARAMETER";
    private static final String BOOLEAN_PARAMETER_KEYWORD = "BOOLEAN_PARAMETER";
    private static final String COLOR_PARAMETER_KEYWORD = "COLOR_PARAMETER";
    private static final String INTEGER_PARAMETER_KEYWORD = "INTEGER_PARAMETER";

    private static final String MAKE_ROUND_TRACK_KEYWORD = "MAKE_ROUND_TRACK";
    private static final String MAKE_LIST_ROUND_KEYWORD = "MAKE_LIST_ROUND";
    private static final String NUM_ROUND_KEYWORD = "NUM_ROUND";
    private static final String ADD_LIST_ROUND_KEYWORD = "ADD_LIST_ROUND";
    private static final String ADD_DICE_ROUND_KEYWORD = "ADD_DICE_ROUND";

    private static final String USER_NAME_KEYWORD = "USER_NAME";
    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";

    private static final String RANKING_KEYWORD = "RANKING";
    private static final String RANKING_ENTRY_KEYWORD = "RANKING_ENTRY";
    private static final String RANKING_PLAYER_KEYWORD = "RANKING_PLAYER";
    private static final String RANKING_POINT_KEYWORD = "RANKING_POINT";

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
     * Used to tell client an exception
     * @param exceptionClass type of exception
     * @param message message of the exception
     */
    void sendException(String exceptionClass, String message) {
         synchronized (lock) {
             socketOut.println(protocol.getMessage(EXCEPTION_MESSAGE_KEYWORD));
             socketOut.println(message);
             socketOut.println(protocol.getMessage(EXCEPTION_KEYWORD));
             socketOut.println(exceptionClass);
             socketOut.flush();
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

        socketOut.println(protocol.getMessage(FAVOR_POINT_KEYWORD));
        socketOut.println(card.getNumFavPoint());

        socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));
        socketOut.println(" ");

        card.getHorizontalItr().forEachRemaining(this::deconstructCell);

        socketOut.println(protocol.getMessage(type));
        socketOut.println(" ");
    }

    /**
     * Deconstruct Cell for passing through socket
     * @param cell to be deconstructed
     */
    private void deconstructCell(Cell cell) {
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

        if (cell.isOccupied()) {
            deconstructDice(cell.getDice(), DICE_KEYWORD);

            socketOut.println(protocol.getMessage(OCCUPIED_CELL_KEYWORD));
            socketOut.println(" ");
        }
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

    /**
     * @param username of player moving the dice
     * @param dest     cell where the dice is being moved
     * @param moved    dice being moved
     */
    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
        socketOut.println(username);

        socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));     // used to reset list of cell used in socket server listener
        socketOut.println(" ");

        deconstructCell(dest);

        deconstructDice(moved, DICE_KEYWORD);

        socketOut.println(protocol.getMessage(SUCCESSFUL_PLACE_DICE_KEYWORD));
        socketOut.println(" ");

        socketOut.flush();
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
     * @param type DICE_DRAFT_KEYWORD if one of the dice of a list, DICE_KEYWORD if it's a single dice
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

    @Override
    public void showRoundTrack(RoundTrack roundTrack) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(MAKE_ROUND_TRACK_KEYWORD));
            socketOut.println(" ");

            roundTrack.getTrackList().forEach(listDiceRound -> {
                socketOut.println(protocol.getMessage(MAKE_LIST_ROUND_KEYWORD));
                socketOut.println(" ");

                listDiceRound.itr().forEachRemaining(dice -> {
                    deconstructDice(dice, DICE_KEYWORD);

                    socketOut.println(protocol.getMessage(ADD_DICE_ROUND_KEYWORD));
                    socketOut.println(" ");
                });

                socketOut.println(protocol.getMessage(NUM_ROUND_KEYWORD));
                socketOut.println(roundTrack.getTrackList().indexOf(listDiceRound));

                socketOut.println(protocol.getMessage(ADD_LIST_ROUND_KEYWORD));
                socketOut.println(" ");
            });

            socketOut.println(protocol.getMessage(SHOW_ROUND_TRACK_KEYWORD));
            socketOut.println(" ");
        }
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printListPublicObj(List<ObjectiveCard> publicObj) {
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

    /**
     * Deconstruct Tool Card for passing through socket
     * @param tool to be deconstructed
     * @param type ADD_TOOL_CARD_LIST_KEYWORD if one of the dice of a list, MAKE_TOOL_CARD_KEYWORD if it's a single dice
     */
    private void deconstructTool(ToolCard tool, String type) {
        socketOut.println(protocol.getMessage(TOOL_ID_KEYWORD));
        socketOut.println(tool.getId());

        socketOut.println(protocol.getMessage(TOOL_NAME_KEYWORD));
        socketOut.println(tool.getName());

        socketOut.println(protocol.getMessage(FAVOR_POINT_KEYWORD));
        socketOut.println(tool.getFavorPoint());

        socketOut.println(protocol.getMessage(type));
        socketOut.println(" ");
    }

    /**
     * @param toolCards toolCards.size() = 3
     */
    @Override
    public void printListToolCard(List<ToolCard> toolCards) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(MAKE_TOOL_LIST_KEYWORD));
            socketOut.println(" ");

            toolCards.forEach(tool -> deconstructTool(tool, ADD_TOOL_CARD_LIST_KEYWORD));

            socketOut.println(protocol.getMessage(SHOW_TOOL_CARD_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param point favor point of the user that requested
     */
    @Override
    public void printNumberFavorPoint(int point) {
        synchronized (this) {
            socketOut.println(protocol.getMessage(SHOW_FAVOR_POINT_KEYWORD));
            socketOut.println(point);

            socketOut.flush();
        }
    }

    @Override
    public void successfulUsedTool(String username, ToolCard card) {
        synchronized (this) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            deconstructTool(card, MAKE_TOOL_CARD_KEYWORD);

            socketOut.println(protocol.getMessage(USED_TOOL_CARD_KEYWORD));
            socketOut.println(" ");
        }
    }

    /**
     * @param ranking sorted map of player username and their points through the game
     */
    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {
        synchronized (lock) {
            ranking.forEach((integer, username) -> {
                socketOut.println(protocol.getMessage(RANKING_PLAYER_KEYWORD));
                socketOut.println(username);

                socketOut.println(protocol.getMessage(RANKING_POINT_KEYWORD));
                socketOut.println(integer);

                socketOut.println(protocol.getMessage(RANKING_ENTRY_KEYWORD));
                socketOut.println(" ");
            });

            socketOut.println(protocol.getMessage(RANKING_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Used to set parameter return over the client to a passed Boolean
     * @param ret to be set over the client
     */
    public void setReturn(Boolean ret) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(SET_RET_KEYWORD));
            socketOut.println(ret.toString());

            socketOut.flush();
        }
    }

    /**
     * Used to set Actor over the client
     * @param actorList list of ToolCard.Actor to be set on client
     */
    public void setActor(List<ToolCard.Actor> actorList) {
        synchronized (lock) {
            actorList.forEach(actor -> {
                socketOut.println(protocol.getMessage(SET_ACTOR_KEYWORD));

                if (actor.equals(ToolCard.Actor.WINDOW_CARD))
                    socketOut.println(protocol.getMessage(WINDOW_CARD_ACTOR_KEYWORD));

                else if (actor.equals(ToolCard.Actor.DRAFT))
                    socketOut.println(protocol.getMessage(DRAFT_ACTOR_KEYWORD));

                else if (actor.equals(ToolCard.Actor.ROUND_TRACK))
                    socketOut.println(protocol.getMessage(ROUND_TRACK_ACTOR_KEYWORD));
            });

            socketOut.println(protocol.getMessage(END_SET_ACTOR_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Used to set Parameter over the client
     * @param parameterList list of ToolCard.Parameter to be set on client
     */
    public void setParameter(List<ToolCard.Parameter> parameterList) {
        synchronized (lock) {
            parameterList.forEach(parameter -> {
                socketOut.println(protocol.getMessage(SET_PARAMETER_KEYWORD));

                if (parameter.equals(ToolCard.Parameter.DICE))
                    socketOut.println(protocol.getMessage(DICE_PARAMETER_KEYWORD));

                else if (parameter.equals(ToolCard.Parameter.CELL))
                    socketOut.println(protocol.getMessage(CELL_PARAMETER_KEYWORD));

                else if (parameter.equals(ToolCard.Parameter.BOOLEAN))
                    socketOut.println(protocol.getMessage(BOOLEAN_PARAMETER_KEYWORD));

                else if (parameter.equals(ToolCard.Parameter.COLOR))
                    socketOut.println(protocol.getMessage(COLOR_PARAMETER_KEYWORD));

                else if (parameter.equals(ToolCard.Parameter.INTEGER))
                    socketOut.println(protocol.getMessage(INTEGER_PARAMETER_KEYWORD));
            });

            socketOut.println(protocol.getMessage(END_SET_PARAMETER_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Used to set Dice used by tool card over the client
     * @param dice Dice to be set on client
     */
    public void setDice(Dice dice) {
        synchronized (lock) {
            deconstructDice(dice, DICE_KEYWORD);

            socketOut.println(protocol.getMessage(SET_DICE_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Used to set Cell used by tool card over the client
     * @param cell Dice to be set on client
     */
    public void setCell(Cell cell) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));
            socketOut.println(" ");

            deconstructCell(cell);

            socketOut.println(protocol.getMessage(SET_CELL_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * Used to set Colors used by tool card over the client
     * @param color Dice to be set on client
     */
    public void setColor(Colors color) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(SET_COLOR_KEYWORD));
            socketOut.println(color.toString());

            socketOut.flush();
        }
    }
}
