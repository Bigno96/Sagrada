package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Thread reading socket input coming from server
 */
public class SocketServerListener implements Runnable {

    private static final String GAME_ALREADY_STARTED_KEYWORD = "GAME_ALREADY_STARTED_MSG";
    private static final String SAME_PLAYER_LOGGED_KEYWORD = "SAME_PLAYER_MSG";
    private static final String TOO_MANY_PLAYERS_KEYWORD = "TOO_MANY_PLAYERS_MSG";

    private static final String PRINT_KEYWORD = "PRINT";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String EXCEPTION_KEYWORD = "EXCEPTION";
    private static final String QUIT_KEYWORD = "QUIT";
    private static final String SERVER_NOT_RESPONDING_KEYWORD = "SERVER_NOT_RESPONDING";
    private static final String SEND_LIST_CARD_KEYWORD = "SEND_LIST_CARD";

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

    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";
    private static final String SHOW_USER_CARD_KEYWORD = "SHOW_USER_CARD";

    private final ViewInterface view;
    private final Socket socket;
    private final SocketServerSpeaker speaker;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    private final HashMap<String, Supplier<String>> exceptionMap = new HashMap<>();
    private final HashMap<String, Consumer<String>> commandMap = new HashMap<>();

    private List<WindowCard> cards;
    private String cardName;
    private int cardId;
    private int favorPoint;
    private List<Cell> cellList;

    private int value;
    private Colors color;
    private int row;
    private int col;
    private static final int MAX_ROW = 3;
    private static final int MAX_COL = 4;

    private WindowCard card;
    private String otherUserName;

    SocketServerListener(Socket socket, ViewInterface view, SocketServerSpeaker speaker) {
        this.view = view;
        this.socket = socket;
        this.speaker = speaker;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.cards = new ArrayList<>();
        this.cellList = new ArrayList<>();

        mapException();
        mapCommand();
    }

    /**
     * Maps exception with their error code to be printed
     */
    private void mapException() {
        exceptionMap.put(GameAlreadyStartedException.class.toString(), () -> dictionary.getMessage(GAME_ALREADY_STARTED_KEYWORD));
        exceptionMap.put(SamePlayerException.class.toString(), () -> dictionary.getMessage(SAME_PLAYER_LOGGED_KEYWORD));
        exceptionMap.put(TooManyPlayersException.class.toString(), () -> dictionary.getMessage(TOO_MANY_PLAYERS_KEYWORD));
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = view::print;
        Consumer<String> login = string -> speaker.setLogged(parseException(string));
        Consumer<String> exception = string -> speaker.setLogged(parseException(string));

        Consumer<String> sendListCard = string -> view.chooseWindowCard(cards);
        Consumer<String> showCardPlayer = string -> view.showCardPlayer(otherUserName, card);

        Consumer<String> makeListCard = string -> cards.add(new WindowCard(cardId, cardName, favorPoint, cellList));
        Consumer<String> makeCard = string -> card = new WindowCard(cardId, cardName, favorPoint, cellList);
        Consumer<String> makeCellList = string -> cellList.clear();
        Consumer<String> setCardName = name -> cardName = name;
        Consumer<String> setCardId = id -> cardId = Integer.parseInt(id);
        Consumer<String> setFavorPoint = point -> favorPoint = Integer.parseInt(point);

        Consumer<String> setCellValue = cellValue -> value = Integer.parseInt(cellValue);
        Consumer<String> setCellColor = cellColor -> color = Colors.parseColor(cellColor);
        Consumer<String> setRow = rowValue -> row = Integer.parseInt(rowValue);
        Consumer<String> setCol = colValue -> col = Integer.parseInt(colValue);
        Consumer<String> makeCell = string -> {
            try {
                cellList.add(new Cell(value, color, row, col, MAX_ROW, MAX_COL));
            } catch (ValueException | PositionException e) {
                view.print(e.getMessage());
            }
        };

        Consumer<String> setOtherUser = username -> otherUserName = username;

        commandMap.put(protocol.getMessage(PRINT_KEYWORD), print);
        commandMap.put(protocol.getMessage(LOGIN_SUCCESS_KEYWORD), login);
        commandMap.put(protocol.getMessage(EXCEPTION_KEYWORD), exception);

        commandMap.put(protocol.getMessage(SEND_LIST_CARD_KEYWORD), sendListCard);

        commandMap.put(protocol.getMessage(LIST_CARD_KEYWORD), makeListCard);
        commandMap.put(protocol.getMessage(CARD_KEYWORD), makeCard);
        commandMap.put(protocol.getMessage(CARD_CELL_LIST_KEYWORD), makeCellList);
        commandMap.put(protocol.getMessage(CARD_NAME_KEYWORD), setCardName);
        commandMap.put(protocol.getMessage(CARD_ID_KEYWORD), setCardId);
        commandMap.put(protocol.getMessage(CARD_FAVOR_POINT_KEYWORD), setFavorPoint);

        commandMap.put(protocol.getMessage(CELL_VALUE_KEYWORD), setCellValue);
        commandMap.put(protocol.getMessage(CELL_COLOR_KEYWORD), setCellColor);
        commandMap.put(protocol.getMessage(CELL_ROW_KEYWORD), setRow);
        commandMap.put(protocol.getMessage(CELL_COL_KEYWORD), setCol);
        commandMap.put(protocol.getMessage(CELL_KEYWORD), makeCell);

        commandMap.put(protocol.getMessage(OTHER_USER_NAME_KEYWORD), setOtherUser);
        commandMap.put(protocol.getMessage(SHOW_USER_CARD_KEYWORD), showCardPlayer);
    }

    /**
     * Listening for incoming server messages
     */
    @Override
    public void run() {
        try {
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();
            socket.setSoTimeout(parser.getSoTimeout());

            while (true) {
                String command = socketIn.readLine();

                if (command.equals(protocol.getMessage(QUIT_KEYWORD)))
                    break;

                else if (commandMap.containsKey(command))       // if it's a known command
                    commandMap.get(command).accept((socketIn.readLine()));      // execute it
            }

        } catch (IOException e) {
            view.print(e.getMessage());
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            speaker.interrupt();
        }
    }

    /**
     * Find if s is the print of an Exception.
     * @param s != null
     * @return true if it was a string coming from an Exception, false else
     */
    private boolean parseException(String s) {
        if (exceptionMap.containsKey(s)) {                  // if it's a known exception
            view.print(exceptionMap.get(s).get());          // print it's message
            return false;
        }
        else
            view.print(s);              // print anyway

        return true;
    }
}
