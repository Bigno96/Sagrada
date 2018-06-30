package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
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
    private static final String EXCEPTION_MESSAGE_KEYWORD = "EXCEPTION_MESSAGE";
    private static final String QUIT_KEYWORD = "QUIT";
    private static final String SERVER_NOT_RESPONDING_KEYWORD = "SERVER_NOT_RESPONDING";

    private static final String SEND_LIST_CARD_KEYWORD = "SEND_LIST_CARD";
    private static final String SHOW_USER_CARD_KEYWORD = "SHOW_USER_CARD";
    private static final String SHOW_DRAFT_KEYWORD = "SHOW_DRAFT";
    private static final String MAKE_DRAFT_KEYWORD = "MAKE_DRAFT";
    private static final String PRINT_CARD_KEYWORD = "PRINT_CARD";
    private static final String NEXT_TURN_KEYWORD = "NEXT_TURN";
    private static final String SHOW_PUBLIC_OBJ_KEYWORD = "SHOW_PUBLIC_OBJ";
    private static final String SHOW_PRIVATE_OBJ_KEYWORD = "SHOW_PRIVATE_OBJ";
    private static final String SUCCESSFUL_PLACE_DICE_KEYWORD = "SUCCESSFUL_PLACE_DICE";

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

    private static final String USER_PLACING_DICE_KEYWORD = "USER_PLACING_DICE";
    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";

    private final ViewInterface view;
    private final Socket socket;
    private final SocketServerSpeaker speaker;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;
    private final GameSettingsParser settings;

    private final HashMap<String, Supplier<String>> gameExceptionMap;
    private final HashMap<String, Supplier<String>> placementExceptionMap;
    private final HashMap<String, Consumer<String>> commandMap;

    private String exceptionMessage;

    private List<WindowCard> cards;
    private WindowCard card;
    private String cardName;
    private int cardId;
    private int favorPoint;
    private List<Cell> cellList;

    private int cellValue;
    private Colors cellColor;
    private int row;
    private int col;

    private List<Dice> draft;
    private Dice dice;
    private int diceId;
    private Colors diceColor;
    private int diceValue;

    private List<ObjectiveCard> publicObj;
    private int objId;
    private String objDescription;
    private int objPoint;

    private String username;
    private String otherUserName;

    SocketServerListener(Socket socket, ViewInterface view, SocketServerSpeaker speaker) {
        this.view = view;
        this.socket = socket;
        this.speaker = speaker;

        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();

        this.gameExceptionMap = new HashMap<>();
        this.placementExceptionMap = new HashMap<>();
        this.commandMap = new HashMap<>();

        this.cards = new ArrayList<>();
        this.cellList = new ArrayList<>();
        this.draft = new ArrayList<>();
        this.publicObj = new ArrayList<>();

        mapGameException();
        mapPlacementException();
        mapCommand();
    }

    /**
     * Maps exception with their error code to be printed
     */
    private void mapGameException() {
        gameExceptionMap.put(GameAlreadyStartedException.class.toString(), () -> dictionary.getMessage(GAME_ALREADY_STARTED_KEYWORD));
        gameExceptionMap.put(SamePlayerException.class.toString(), () -> dictionary.getMessage(SAME_PLAYER_LOGGED_KEYWORD));
        gameExceptionMap.put(TooManyPlayersException.class.toString(), () -> dictionary.getMessage(TOO_MANY_PLAYERS_KEYWORD));
    }

    private void mapPlacementException() {
        placementExceptionMap.put(NotTurnException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(WrongDiceSelectionException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(NotEmptyException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(WrongCellSelectionException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(IDNotFoundException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(PositionException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(EmptyException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(WrongPositionException.class.toString(), () -> exceptionMessage);
        placementExceptionMap.put(AlreadyDoneException.class.toString(), () -> exceptionMessage);
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = view::print;
        Consumer<String> login = string -> speaker.setLogged(parseException(string));
        Consumer<String> exception = string -> speaker.setLogged(parseException(string));
        Consumer<String> getExceptionMessage = message -> exceptionMessage = message;

        Consumer<String> sendListCard = string -> view.chooseWindowCard(cards);
        Consumer<String> showCardPlayer = string -> view.showCardPlayer(otherUserName, card);
        Consumer<String> printCard = string -> view.printWindowCard(card);
        Consumer<String> nextTurn = view::isTurn;

        Consumer<String> makeListCard = string -> cards.add(new WindowCard(cardId, cardName, favorPoint, cellList,
                                    settings.getWindowCardMaxRow(), settings.getWindowCardMaxColumn()));
        Consumer<String> makeCard = string -> card = new WindowCard(cardId, cardName, favorPoint, cellList,
                                    settings.getWindowCardMaxRow(), settings.getWindowCardMaxColumn());
        Consumer<String> makeCellList = string -> cellList.clear();
        Consumer<String> setCardName = name -> cardName = name;
        Consumer<String> setCardId = id -> cardId = Integer.parseInt(id);
        Consumer<String> setFavorPoint = point -> favorPoint = Integer.parseInt(point);

        Consumer<String> setCellValue = value -> cellValue = Integer.parseInt(value);
        Consumer<String> setCellColor = color -> cellColor = Colors.parseColor(color);
        Consumer<String> setRow = rowValue -> row = Integer.parseInt(rowValue);
        Consumer<String> setCol = colValue -> col = Integer.parseInt(colValue);
        Consumer<String> makeCell = string -> {
            try {
                cellList.add(new Cell(cellValue, cellColor, row, col,
                        settings.getWindowCardMaxRow(), settings.getWindowCardMaxColumn()));
            } catch (ValueException | PositionException e) {
                view.print(e.getMessage());
            }
        };

        Consumer<String> setDiceId = id -> diceId = Integer.parseInt(id);
        Consumer<String> setDiceValue = value -> diceValue = Integer.parseInt(value);
        Consumer<String> setDiceColor = color -> diceColor = Colors.parseColor(color);
        Consumer<String> makeDraft = string -> draft.clear();
        Consumer<String> showDraft = string -> view.showDraft(draft);
        Consumer<String> makeDiceDraft = string -> {
            try {
                draft.add(new Dice(diceId, diceColor, diceValue));
            } catch (IDNotFoundException e) {
                view.print(e.getMessage());
            }
        };
        Consumer<String> makeDice = string -> {
            try {
                dice = new Dice(diceId, diceColor, diceValue);
            } catch (IDNotFoundException e) {
                view.print(e.getMessage());
            }
        };

        Consumer<String> setObjId = id -> objId = Integer.parseInt(id);
        Consumer<String> setObjDescription = desc -> objDescription = desc;
        Consumer<String> setObjPoint = point -> objPoint = Integer.parseInt(point);
        Consumer<String> makePublicList = string -> publicObj.clear();
        Consumer<String> makePublicCard = string -> publicObj.add(new PublicObjective(objId, objDescription, objPoint));
        Consumer<String> showPublicCard = string -> view.printPublicObj(publicObj);
        Consumer<String> showPrivateCard = string -> view.printPrivateObj(new PrivateObjective(objId, objDescription));

        Consumer<String> placementDice = string -> view.successfulPlacementDice(username, cellList.get(0), dice);

        Consumer<String> setPlacementUser = placementUser -> username = placementUser;
        Consumer<String> setOtherUser = otherUsername -> otherUserName = otherUsername;

        commandMap.put(protocol.getMessage(PRINT_KEYWORD), print);
        commandMap.put(protocol.getMessage(LOGIN_SUCCESS_KEYWORD), login);
        commandMap.put(protocol.getMessage(EXCEPTION_KEYWORD), exception);
        commandMap.put(protocol.getMessage(EXCEPTION_MESSAGE_KEYWORD), getExceptionMessage);

        commandMap.put(protocol.getMessage(SEND_LIST_CARD_KEYWORD), sendListCard);
        commandMap.put(protocol.getMessage(SHOW_USER_CARD_KEYWORD), showCardPlayer);
        commandMap.put(protocol.getMessage(PRINT_CARD_KEYWORD), printCard);
        commandMap.put(protocol.getMessage(NEXT_TURN_KEYWORD), nextTurn);

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

        commandMap.put(protocol.getMessage(DICE_ID_KEYWORD), setDiceId);
        commandMap.put(protocol.getMessage(DICE_VALUE_KEYWORD), setDiceValue);
        commandMap.put(protocol.getMessage(DICE_COLOR_KEYWORD), setDiceColor);
        commandMap.put(protocol.getMessage(MAKE_DRAFT_KEYWORD), makeDraft);
        commandMap.put(protocol.getMessage(SHOW_DRAFT_KEYWORD), showDraft);
        commandMap.put(protocol.getMessage(DICE_DRAFT_KEYWORD), makeDiceDraft);
        commandMap.put(protocol.getMessage(DICE_KEYWORD), makeDice);

        commandMap.put(protocol.getMessage(OBJ_ID_KEYWORD), setObjId);
        commandMap.put(protocol.getMessage(OBJ_DESCRIPTION_KEYWORD), setObjDescription);
        commandMap.put(protocol.getMessage(OBJ_POINT_KEYWORD), setObjPoint);
        commandMap.put(protocol.getMessage(MAKE_PUBLIC_LIST_KEYWORD), makePublicList);
        commandMap.put(protocol.getMessage(MAKE_PUBLIC_OBJ_KEYWORD), makePublicCard);
        commandMap.put(protocol.getMessage(SHOW_PUBLIC_OBJ_KEYWORD), showPublicCard);
        commandMap.put(protocol.getMessage(SHOW_PRIVATE_OBJ_KEYWORD), showPrivateCard);

        commandMap.put(protocol.getMessage(SUCCESSFUL_PLACE_DICE_KEYWORD), placementDice);

        commandMap.put(protocol.getMessage(USER_PLACING_DICE_KEYWORD), setPlacementUser);
        commandMap.put(protocol.getMessage(OTHER_USER_NAME_KEYWORD), setOtherUser);
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
        if (gameExceptionMap.containsKey(s)) {                  // if it's a known exception
            view.print(gameExceptionMap.get(s).get());          // print it's message
            return false;
        }
        else if (placementExceptionMap.containsKey(s)) {
            view.print(placementExceptionMap.get(s).get());
            view.wrongPlacementDice();
        }
        else
            view.print(s);              // print anyway

        return true;
    }
}
