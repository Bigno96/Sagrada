package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
    private static final String ASK_ROUND_TRACK_KEYWORD = "ASK_ROUND_TRACK";
    private static final String ASK_TOOL_CARD_KEYWORD = "ASK_TOOL_CARD";
    private static final String ASK_FAVOR_POINT_KEYWORD = "ASK_FAVOR_POINT";

    private static final String PLACE_DICE_KEYWORD = "PLACE_DICE";
    private static final String INDEX_DICE_PLACING_KEYWORD = "INDEX_DICE_PLACING";
    private static final String ROW_CELL_PLACING_KEYWORD = "ROW_CELL_PLACING";
    private static final String COL_CELL_PLACING_KEYWORD = "COL_CELL_PLACING";

    private static final String CHECK_PRE_CONDITION_KEYWORD = "CHECK_PRE_CONDITION";
    private static final String CHECK_TOOL_KEYWORD = "CHECK_TOOL";
    private static final String USE_TOOL_KEYWORD = "USE_TOOL";
    private static final String BOOLEAN_TOOL_KEYWORD = "BOOLEAN_TOOL";

    private static final String GET_ACTOR_KEYWORD = "GET_ACTOR";
    private static final String WINDOW_CARD_ACTOR_KEYWORD = "WINDOW_CARD_ACTOR";
    private static final String DRAFT_ACTOR_KEYWORD = "DRAFT_ACTOR";
    private static final String ROUND_TRACK_ACTOR_KEYWORD = "ROUND_TRACK_ACTOR";

    private static final String START_ADD_COORDINATES_KEYWORD = "START_ADD_COORDINATES";
    private static final String ADD_COORDINATES_KEYWORD = "ADD_COORDINATES";
    private static final String GET_DICE_FROM_ACTOR_KEYWORD = "GET_DICE_FROM_ACTOR";
    private static final String GET_CELL_FROM_WINDOW_KEYWORD = "GET_CELL_FROM_WINDOW";
    private static final String GET_COLOR_FROM_ROUND_TRACK_KEYWORD = "GET_COLOR_FROM_ROUND_TRACK";

    private static final String GET_PARAMETER_KEYWORD = "GET_PARAMETER";
    private static final String DICE_PARAMETER_KEYWORD = "DICE_PARAMETER";
    private static final String CELL_PARAMETER_KEYWORD = "CELL_PARAMETER";
    private static final String BOOLEAN_PARAMETER_KEYWORD = "BOOLEAN_PARAMETER";
    private static final String COLOR_PARAMETER_KEYWORD = "COLOR_PARAMETER";
    private static final String INTEGER_PARAMETER_KEYWORD = "INTEGER_PARAMETER";

    private static final String CARD_CELL_LIST_KEYWORD = "CARD_CELL_LIST";
    private static final String OCCUPIED_CELL_KEYWORD = "OCCUPIED_CELL";

    private static final String CELL_VALUE_KEYWORD = "CELL_VALUE";
    private static final String CELL_COLOR_KEYWORD = "CELL_COLOR";
    private static final String CELL_ROW_KEYWORD = "CELL_ROW";
    private static final String CELL_COL_KEYWORD = "CELL_COL";
    private static final String CELL_KEYWORD = "CELL";

    private static final String MAKE_DRAFT_KEYWORD = "MAKE_DRAFT";
    private static final String DICE_ID_KEYWORD = "DICE_ID";
    private static final String DICE_COLOR_KEYWORD = "DICE_COLOR";
    private static final String DICE_VALUE_KEYWORD = "DICE_VALUE";
    private static final String DICE_DRAFT_KEYWORD = "DICE_DRAFT";
    private static final String DICE_KEYWORD = "DICE";

    private static final String END_TURN_KEYWORD = "END_TURN";
    private static final String QUIT_GAME_KEYWORD = "QUIT_GAME";

    private String ip;
    private Socket socket;
    private static PrintWriter socketOut;
    private Boolean logged;

    private final ViewInterface view;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    private Boolean ret;
    private List<ToolCard.Actor> actorList;
    private List<ToolCard.Parameter> parameterList;
    private Dice dice;
    private Cell cell;
    private Colors color;

    private final Object lock;
    private final Semaphore semaphore;

    public SocketServerSpeaker(String ip, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.logged = null;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.lock = new Object();
        this.semaphore = new Semaphore(0);
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
    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    /**
     * Used to release permits on semaphore
     */
    void releaseSemaphore() {
        semaphore.release();
    }

    /**
     * Used to acquire permits on semaphore
     */
    private void acquireSemaphore() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
            SocketServerListener listener = new SocketServerListener(socket, view, this);
            new Thread(listener).start();

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
    public void askRoundTrack(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_ROUND_TRACK_KEYWORD));
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

    /**
     * @param username of player that requested
     */
    @Override
    public void askToolCards(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_TOOL_CARD_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askFavorPoints(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(ASK_FAVOR_POINT_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
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
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(INDEX_DICE_PLACING_KEYWORD));
            socketOut.println(index);

            socketOut.println(protocol.getMessage(ROW_CELL_PLACING_KEYWORD));
            socketOut.println(row);

            socketOut.println(protocol.getMessage(COL_CELL_PLACING_KEYWORD));
            socketOut.println(col);

            socketOut.println(protocol.getMessage(PLACE_DICE_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();
        }
    }

    /**
     * @param ret to be set
     */
    public void setReturn(Boolean ret) {
        this.ret = ret;
    }

    /**
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return true id can be activated, false else
     */
    @Override
    public Boolean checkPreCondition(int pick, String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(CHECK_PRE_CONDITION_KEYWORD));
            socketOut.println(pick);

            socketOut.flush();

            acquireSemaphore();

            return ret;
        }
    }

    /**
     * @param actor string to be parsed, add member to actorList
     */
    public void setActor(String actor) {
        if (actor.equals(protocol.getMessage(WINDOW_CARD_ACTOR_KEYWORD)))
            actorList.add(ToolCard.Actor.WINDOW_CARD);

        else if (actor.equals(protocol.getMessage(ROUND_TRACK_ACTOR_KEYWORD)))
            actorList.add(ToolCard.Actor.ROUND_TRACK);

        else if (actor.equals(protocol.getMessage(DRAFT_ACTOR_KEYWORD)))
            actorList.add(ToolCard.Actor.DRAFT);
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Actor if everything okay, Collections.emptyList() if not okay
     */
    @Override
    public List<ToolCard.Actor> getActor(int pick, String username) {
        actorList = new ArrayList<>();

        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(GET_ACTOR_KEYWORD));
            socketOut.println(pick);

            socketOut.flush();

            acquireSemaphore();

            return actorList;
        }
    }

    /**
     * @param parameter string to be parsed, add member to parameterList
     */
    public void setParameter(String parameter) {
        if (parameter.equals(protocol.getMessage(DICE_PARAMETER_KEYWORD)))
             parameterList.add(ToolCard.Parameter.DICE);

        else if (parameter.equals(protocol.getMessage(CELL_PARAMETER_KEYWORD)))
            parameterList.add(ToolCard.Parameter.CELL);

        else if (parameter.equals(protocol.getMessage(BOOLEAN_PARAMETER_KEYWORD)))
            parameterList.add(ToolCard.Parameter.BOOLEAN);

        else if (parameter.equals(protocol.getMessage(COLOR_PARAMETER_KEYWORD)))
            parameterList.add(ToolCard.Parameter.COLOR);

        else if (parameter.equals(protocol.getMessage(INTEGER_PARAMETER_KEYWORD)))
            parameterList.add(ToolCard.Parameter.INTEGER);
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Parameter if everything okay, Collections.emptyList() if not okay
     */
    @Override
    public List<ToolCard.Parameter> getParameter(int pick, String username) {
        parameterList = new ArrayList<>();

        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(GET_PARAMETER_KEYWORD));
            socketOut.println(pick);

            socketOut.flush();

            acquireSemaphore();

            return parameterList;
        }
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
     * @param pick      index of tool card chosen by player
     * @param dices     requested by tool card, dices.size() == 0 || 1 || 2
     * @param cells     requested by tool card, cells.size() == 0 || 1 || 2
     * @param diceValue if value of a dice need to be changed, 0 if not needed
     * @param diceColor if color on round track is necessary for the tool card, null if not needed
     * @return true if tool can be used with passed parameters, false else
     */
    @Override
    public Boolean checkTool(int pick, List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) {
        synchronized (lock) {
            if (diceColor == null)
                diceColor = Colors.WHITE;

            socketOut.println(protocol.getMessage(MAKE_DRAFT_KEYWORD));             // clear list of dice in the listener
            socketOut.println(" ");

            dices.forEach(d -> deconstructDice(d, DICE_DRAFT_KEYWORD));

            socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));         // clear list of cells in the listener
            socketOut.println(" ");

            cells.forEach(this::deconstructCell);

            socketOut.println(protocol.getMessage(DICE_VALUE_KEYWORD));
            socketOut.println(diceValue);

            socketOut.println(protocol.getMessage(DICE_COLOR_KEYWORD));
            socketOut.println(diceColor.toString());

            socketOut.println(protocol.getMessage(CHECK_TOOL_KEYWORD));
            socketOut.println(pick);

            socketOut.flush();

            acquireSemaphore();

            return ret;
        }
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param dices    null when not needed
     * @param up       null when not needed
     * @param cells    null when not needed
     * @param username of who requested
     ** @return true if move was successful, else false
     */
    @Override
    public Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells, String username) {
        synchronized (lock) {
            if (up == null)
                up = false;

            socketOut.println(protocol.getMessage(MAKE_DRAFT_KEYWORD));             // clear list of dice in the listener
            socketOut.println(" ");

            dices.forEach(d -> deconstructDice(d, DICE_DRAFT_KEYWORD));

            socketOut.println(protocol.getMessage(CARD_CELL_LIST_KEYWORD));         // clear list of cells in the listener
            socketOut.println(" ");

            cells.forEach(this::deconstructCell);

            socketOut.println(protocol.getMessage(BOOLEAN_TOOL_KEYWORD));
            socketOut.println(up.toString());

            socketOut.println(protocol.getMessage(USE_TOOL_KEYWORD));
            socketOut.println(pick);

            socketOut.flush();

            acquireSemaphore();

            return ret;
        }
    }

    /**
     * @param dice to be set
     */
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    /**
     * @param actor       can be Draft, Window Card or Round Track
     * @param username    of who requested
     * @param coordinates there could be various type of coordinates depending on which actor is passed
     *                    if Draft, coordinates.size() = 1 and coordinates contains index of dice in the draft
     *                    if Round Track, coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     *                    if Window Card, coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol() of where the dice is located
     * @return Dice asked by user, null if any problem occurs
     */
    @Override
    public Dice getDiceFromActor(ToolCard.Actor actor, String username, List<Integer> coordinates) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(START_ADD_COORDINATES_KEYWORD));
            socketOut.println(" ");

            coordinates.forEach(coordinate -> {
                socketOut.println(protocol.getMessage(ADD_COORDINATES_KEYWORD));
                socketOut.println(coordinate);
            });

            socketOut.println(protocol.getMessage(GET_DICE_FROM_ACTOR_KEYWORD));
            socketOut.println(actor.toString());

            socketOut.flush();

            acquireSemaphore();

            return dice;
        }
    }

    /**
     * @param cell to be set
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol()
     * @return Cell asked by user, null if any problem occurs
     */
    @Override
    public Cell getCellFromWindow(String username, List<Integer> coordinates) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(START_ADD_COORDINATES_KEYWORD));
            socketOut.println(" ");

            coordinates.forEach(coordinate -> {
                socketOut.println(protocol.getMessage(ADD_COORDINATES_KEYWORD));
                socketOut.println(coordinate);
            });

            socketOut.println(protocol.getMessage(GET_CELL_FROM_WINDOW_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();

            acquireSemaphore();

            return cell;
        }
    }

    /**
     * @param color to be set
     */
    public void setColor(Colors color) {
        this.color = color;
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    @Override
    public Colors getColorFromRoundTrack(String username, List<Integer> coordinates) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(USER_NAME_KEYWORD));
            socketOut.println(username);

            socketOut.println(protocol.getMessage(START_ADD_COORDINATES_KEYWORD));
            socketOut.println(" ");

            coordinates.forEach(coordinate -> {
                socketOut.println(protocol.getMessage(ADD_COORDINATES_KEYWORD));
                socketOut.println(coordinate);
            });

            socketOut.println(protocol.getMessage(GET_COLOR_FROM_ROUND_TRACK_KEYWORD));
            socketOut.println(" ");

            socketOut.flush();

            acquireSemaphore();

            return color;
        }
    }

    /**
     * @param username user that wants to quit
     */
    @Override
    public void quit(String username) {
        synchronized (lock) {
            socketOut.println(protocol.getMessage(QUIT_GAME_KEYWORD));
            socketOut.println(username);

            socketOut.flush();
        }
    }

}
