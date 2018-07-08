package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.System.out;

/**
 * Listen on socket messages from client
 */
public class SocketClientListener implements Runnable {

    private static final String QUIT_KEYWORD = "QUIT";
    private static final String PRINT_KEYWORD = "PRINT";
    private static final String CONNECT_KEYWORD = "CONNECT";
    private static final String LOGIN_KEYWORD = "LOGIN";

    private static final String EMPTY_GAME_EXCEPTION_KEYWORD = "EMPTY_GAME_EXCEPTION";
    private static final String NOT_IN_GAME_EXCEPTION_KEYWORD = "NOT_IN_GAME_EXCEPTION";
    private static final String SOMETHING_WENT_WRONG_KEYWORD = "SOMETHING_WENT_WRONG";

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
    private static final String GET_PARAMETER_KEYWORD = "GET_PARAMETER";

    private static final String START_ADD_COORDINATES_KEYWORD = "START_ADD_COORDINATES";
    private static final String ADD_COORDINATES_KEYWORD = "ADD_COORDINATES";
    private static final String GET_DICE_FROM_ACTOR_KEYWORD = "GET_DICE_FROM_ACTOR";
    private static final String GET_CELL_FROM_WINDOW_KEYWORD = "GET_CELL_FROM_WINDOW";
    private static final String GET_COLOR_FROM_ROUND_TRACK_KEYWORD = "GET_COLOR_FROM_ROUND_TRACK";

    private static final String END_TURN_KEYWORD = "END_TURN";

    private static final String DICE_ID_KEYWORD = "DICE_ID";
    private static final String DICE_COLOR_KEYWORD = "DICE_COLOR";
    private static final String DICE_VALUE_KEYWORD = "DICE_VALUE";
    private static final String DICE_DRAFT_KEYWORD = "DICE_DRAFT";
    private static final String DICE_KEYWORD = "DICE";
    private static final String MAKE_DRAFT_KEYWORD = "MAKE_DRAFT";

    private static final String CELL_VALUE_KEYWORD = "CELL_VALUE";
    private static final String CELL_COLOR_KEYWORD = "CELL_COLOR";
    private static final String CELL_ROW_KEYWORD = "CELL_ROW";
    private static final String CELL_COL_KEYWORD = "CELL_COL";
    private static final String CELL_KEYWORD = "CELL";
    private static final String CARD_CELL_LIST_KEYWORD = "CARD_CELL_LIST";
    private static final String OCCUPIED_CELL_KEYWORD = "OCCUPIED_CELL";

    private static final String QUIT_GAME_MESSAGE_KEYWORD = "QUIT_GAME_MESSAGE";
    private static final String QUIT_GAME_KEYWORD = "QUIT_GAME";
    private static final String OTHER_QUIT_GAME_MESSAGE_KEYWORD = "QUIT_GAME_MESSAGE";

    private final Socket socket;
    private final SocketClientSpeaker speaker;
    private final Lobby lobby;

    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;
    private final GameSettingsParser settings;
    private final HashMap<String, Consumer<String>> commandMap = new HashMap<>();

    private String username;
    private String otherUsername;

    private List<Integer> coordinates;

    private int indexDice;
    private Boolean up;

    private List<Dice> diceList;
    private Dice dice;
    private int diceId;
    private Colors diceColor;
    private int diceValue;

    private List<Cell> cellList;
    private int cellValue;
    private Colors cellColor;
    private int rowCell;
    private int colCell;

    SocketClientListener(Socket socket, SocketClientSpeaker speaker, Lobby lobby) {
        this.socket = socket;
        this.speaker = speaker;
        this.lobby = lobby;
        this.coordinates = new ArrayList<>();
        this.cellList = new ArrayList<>();
        this.diceList = new ArrayList<>();

        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();

        mapCommand();
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = out::println;
        Consumer<String> connect = speaker::connect;
        Consumer<String> login = speaker::login;

        Consumer<String> setCard = cardName -> lobby.getGameController().setWindowCard(username, cardName);
        Consumer<String> getCard = me -> lobby.getSpeakers().get(me).printWindowCard(lobby.getPlayers().get(otherUsername).getWindowCard());

        Consumer<String> setUserName = user -> username = user;
        Consumer<String> otherUserName = user -> otherUsername = user;
        Consumer<String> getAllUsername = me -> {
            for (String user : lobby.getPlayers().keySet())
                if (!user.equals(me))
                    lobby.getSpeakers().get(me).tell(user);
        };

        Consumer<String> askDraft = me -> speaker.showDraft(lobby.getGame().getBoard().getDraft());
        Consumer<String> askPublicObj = me -> speaker.printListPublicObj(lobby.getGame().getBoard().getPublicObj());
        Consumer<String> askPrivateObj = me -> speaker.printPrivateObj(lobby.getPlayers().get(username).getPrivateObj());
        Consumer<String> askRoundTrack = me -> speaker.showRoundTrack(lobby.getGame().getBoard().getRoundTrack());
        Consumer<String> askToolCard = me -> speaker.printListToolCard(lobby.getGame().getBoard().getToolCard());
        Consumer<String> askFavorPoint = me -> speaker.printNumberFavorPoint(lobby.getPlayers().get(me).getFavorPoint());

        Consumer<String> setIndexDice = index -> indexDice = Integer.parseInt(index);
        Consumer<String> setRowCell = row -> rowCell = Integer.parseInt(row);
        Consumer<String> setColCell = col -> colCell = Integer.parseInt(col);
        Consumer<String> placeDice = string -> placeDice(username, indexDice, rowCell, colCell);

        Consumer<String> startAddCoordinates = string -> coordinates.clear();
        Consumer<String> addCoordinate = coordinate -> coordinates.add(Integer.parseInt(coordinate));
        Consumer<String> getDiceFromActor = actor -> speaker.setDice(lobby.getActionController().getDiceFromActor(parseActor(actor), username, coordinates));
        Consumer<String> getCellFromWindow = string -> speaker.setCell(lobby.getActionController().getCellFromWindow(username, coordinates));
        Consumer<String> getColorFromRoundTrack = string -> speaker.setColor(lobby.getActionController().getColorFromRoundTrack(username, coordinates));

        Consumer<String> endTurn = me -> lobby.getRoundController().nextTurn();

        Consumer<String> setDiceId = id -> diceId = Integer.parseInt(id);
        Consumer<String> setDiceValue = value -> diceValue = Integer.parseInt(value);
        Consumer<String> setDiceColor = color -> diceColor = Colors.parseColor(color);
        Consumer<String> makeList = string -> diceList.clear();
        Consumer<String> makeDiceList = string -> {
            try {
                diceList.add(new Dice(diceId, diceColor, diceValue));

            } catch (IDNotFoundException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(SOMETHING_WENT_WRONG_KEYWORD));
                speaker.setReturn(false);
            }
        };
        Consumer<String> makeDice = string -> {
            try {
                dice = new Dice(diceId, diceColor, diceValue);

            } catch (IDNotFoundException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(SOMETHING_WENT_WRONG_KEYWORD));
                speaker.setReturn(false);
            }
        };

        Consumer<String> setCellValue = value -> cellValue = Integer.parseInt(value);
        Consumer<String> setCellColor = color -> cellColor = Colors.parseColor(color);
        Consumer<String> setRow = rowValue -> rowCell = Integer.parseInt(rowValue);
        Consumer<String> setCol = colValue -> colCell = Integer.parseInt(colValue);
        Consumer<String> makeCell = string -> {
            try {
                cellList.add(new Cell(cellValue, cellColor, rowCell, colCell,
                        settings.getWindowCardMaxRow(), settings.getWindowCardMaxColumn()));
            } catch (ValueException | PositionException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(SOMETHING_WENT_WRONG_KEYWORD));
                speaker.setReturn(false);
            }
        };
        Consumer<String> makeCellList = string -> cellList.clear();
        Consumer<String> setCellOccupied = string -> {
            try {
                cellList.get(cellList.size()-1).setDice(dice);
            } catch (NotEmptyException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(SOMETHING_WENT_WRONG_KEYWORD));
                speaker.setReturn(false);
            }
        };

        Consumer<String> quit = user -> {
            lobby.getSpeakers().forEach((u, s) -> {
                if (u.equals(user))
                    s.tell(dictionary.getMessage(QUIT_GAME_MESSAGE_KEYWORD));
                else
                    s.tell(dictionary.getMessage(OTHER_QUIT_GAME_MESSAGE_KEYWORD));
            });

            lobby.removePlayer(user);
        };

        commandMap.put(protocol.getMessage(PRINT_KEYWORD), print);
        commandMap.put(protocol.getMessage(CONNECT_KEYWORD), connect);
        commandMap.put(protocol.getMessage(LOGIN_KEYWORD), login);

        commandMap.put(protocol.getMessage(SET_CARD_KEYWORD), setCard);
        commandMap.put(protocol.getMessage(GET_CARD_KEYWORD), getCard);

        commandMap.put(protocol.getMessage(USER_NAME_KEYWORD), setUserName);
        commandMap.put(protocol.getMessage(OTHER_USER_NAME_KEYWORD), otherUserName);
        commandMap.put(protocol.getMessage(GET_ALL_USER_KEYWORD), getAllUsername);

        commandMap.put(protocol.getMessage(ASK_DRAFT_KEYWORD), askDraft);
        commandMap.put(protocol.getMessage(ASK_PUBLIC_OBJ_KEYWORD), askPublicObj);
        commandMap.put(protocol.getMessage(ASK_PRIVATE_OBJ_KEYWORD), askPrivateObj);
        commandMap.put(protocol.getMessage(ASK_ROUND_TRACK_KEYWORD), askRoundTrack);
        commandMap.put(protocol.getMessage(ASK_TOOL_CARD_KEYWORD), askToolCard);
        commandMap.put(protocol.getMessage(ASK_FAVOR_POINT_KEYWORD), askFavorPoint);

        commandMap.put(protocol.getMessage(USER_NAME_KEYWORD), setUserName);
        commandMap.put(protocol.getMessage(INDEX_DICE_PLACING_KEYWORD), setIndexDice);
        commandMap.put(protocol.getMessage(ROW_CELL_PLACING_KEYWORD), setRowCell);
        commandMap.put(protocol.getMessage(COL_CELL_PLACING_KEYWORD), setColCell);
        commandMap.put(protocol.getMessage(PLACE_DICE_KEYWORD), placeDice);

        commandMap.put(protocol.getMessage(START_ADD_COORDINATES_KEYWORD), startAddCoordinates);
        commandMap.put(protocol.getMessage(ADD_COORDINATES_KEYWORD), addCoordinate);
        commandMap.put(protocol.getMessage(GET_DICE_FROM_ACTOR_KEYWORD), getDiceFromActor);
        commandMap.put(protocol.getMessage(GET_CELL_FROM_WINDOW_KEYWORD), getCellFromWindow);
        commandMap.put(protocol.getMessage(GET_COLOR_FROM_ROUND_TRACK_KEYWORD), getColorFromRoundTrack);

        commandMap.put(protocol.getMessage(END_TURN_KEYWORD), endTurn);

        commandMap.put(protocol.getMessage(DICE_ID_KEYWORD), setDiceId);
        commandMap.put(protocol.getMessage(DICE_VALUE_KEYWORD), setDiceValue);
        commandMap.put(protocol.getMessage(DICE_COLOR_KEYWORD), setDiceColor);
        commandMap.put(protocol.getMessage(MAKE_DRAFT_KEYWORD), makeList);
        commandMap.put(protocol.getMessage(DICE_DRAFT_KEYWORD), makeDiceList);
        commandMap.put(protocol.getMessage(DICE_KEYWORD), makeDice);

        commandMap.put(protocol.getMessage(CELL_VALUE_KEYWORD), setCellValue);
        commandMap.put(protocol.getMessage(CELL_COLOR_KEYWORD), setCellColor);
        commandMap.put(protocol.getMessage(CELL_ROW_KEYWORD), setRow);
        commandMap.put(protocol.getMessage(CELL_COL_KEYWORD), setCol);
        commandMap.put(protocol.getMessage(CELL_KEYWORD), makeCell);
        commandMap.put(protocol.getMessage(CARD_CELL_LIST_KEYWORD), makeCellList);
        commandMap.put(protocol.getMessage(OCCUPIED_CELL_KEYWORD), setCellOccupied);

        commandMap.put(protocol.getMessage(QUIT_GAME_KEYWORD), quit);

        mapToolCardCommand();
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapToolCardCommand() {
        Consumer<String> checkPreCondition = pick -> {
            try {
                speaker.setReturn(lobby.getActionController().checkPreCondition(Integer.parseInt(pick), username));

            } catch (EmptyException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(EMPTY_GAME_EXCEPTION_KEYWORD));
                speaker.setReturn(false);

            } catch (PlayerNotFoundException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(NOT_IN_GAME_EXCEPTION_KEYWORD));
                speaker.setReturn(false);

            } catch (IDNotFoundException | NotEnoughFavorPointsException e) {
                speaker.sendException(e.getClass().toString(), e.getMessage());
                speaker.setReturn(false);
            }
        };
        Consumer<String> checkTool = pick -> {
            try {
                speaker.setReturn(lobby.getActionController().checkTool(Integer.parseInt(pick), diceList, cellList, diceValue, diceColor));

            } catch (PositionException e) {
                speaker.sendException(e.getClass().toString(), dictionary.getMessage(NOT_IN_GAME_EXCEPTION_KEYWORD));
                speaker.setReturn(false);

            } catch (IDNotFoundException e) {
                speaker.sendException(e.getClass().toString(), e.getMessage());
                speaker.setReturn(false);
            }
        };
        Consumer<String> useTool = pick -> {
            try {
                speaker.setReturn(lobby.getActionController().useTool(Integer.parseInt(pick), diceList, up, cellList, username));

            } catch (NotEmptyException | EmptyException | ValueException | RoundNotFoundException | IDNotFoundException |
                    PlayerNotFoundException | SameDiceException e) {
                speaker.sendException(e.getClass().toString(), e.getMessage());
                speaker.setReturn(false);
            }
        };

        commandMap.put(protocol.getMessage(CHECK_PRE_CONDITION_KEYWORD), checkPreCondition);
        commandMap.put(protocol.getMessage(CHECK_TOOL_KEYWORD), checkTool);
        commandMap.put(protocol.getMessage(USE_TOOL_KEYWORD), useTool);

        mapActorAndParameter();
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapActorAndParameter() {
        Consumer<String> getActor = pick -> {
            try {
                speaker.setActor(lobby.getActionController().getActor(Integer.parseInt(pick), username));

            } catch (IDNotFoundException e) {
                speaker.sendException(e.getClass().toString(), e.getMessage());
                speaker.setActor(Collections.emptyList());
            }
        };
        Consumer<String> getParameter = pick -> {
            try {
                speaker.setParameter(lobby.getActionController().getParameter(Integer.parseInt(pick), username));

            } catch (IDNotFoundException e) {
                speaker.sendException(e.getClass().toString(), e.getMessage());
                speaker.setParameter(Collections.emptyList());
            }
        };
        Consumer<String> setBool = bool -> up = Boolean.parseBoolean(bool);

        commandMap.put(protocol.getMessage(GET_ACTOR_KEYWORD), getActor);
        commandMap.put(protocol.getMessage(GET_PARAMETER_KEYWORD), getParameter);
        commandMap.put(protocol.getMessage(BOOLEAN_TOOL_KEYWORD), setBool);
    }

    /**
     * Used to tell action controller to try to place dice. All Exceptions are thrown back to the client through client speaker
     * @param username of player that is placing dice
     * @param indexDice index of draft of the dice being placed
     * @param rowCell row of the destination cell
     * @param colCell column of the destination cell
     */
    private void placeDice(String username, int indexDice, int rowCell, int colCell) {
        try {
            lobby.getActionController().placeDice(username, indexDice, rowCell, colCell);

        } catch (NotTurnException | WrongDiceSelectionException | NotEmptyException | WrongCellSelectionException |
                IDNotFoundException | PositionException | EmptyException | WrongPositionException | AlreadyDoneException e) {

            speaker.sendException(e.getClass().toString(), e.getMessage());
        }
    }

    /**
     * Parse a string into a ToolCard.Actor
     * @param actor string to be parsed
     * @return parsing result, nul if no corresponding found
     */
    private ToolCard.Actor parseActor(String actor) {
        if (actor.equals(ToolCard.Actor.WINDOW_CARD.toString()))
            return ToolCard.Actor.WINDOW_CARD;

        else if (actor.equals(ToolCard.Actor.ROUND_TRACK.toString()))
            return ToolCard.Actor.ROUND_TRACK;

        else if (actor.equals(ToolCard.Actor.DRAFT.toString()))
            return ToolCard.Actor.DRAFT;

        return null;
    }

    @Override
    public void run() {
        try {
            Scanner socketIn = new Scanner(new InputStreamReader(socket.getInputStream()));

            while(true) {
                String command = socketIn.nextLine();

                if (command.equals(protocol.getMessage(QUIT_KEYWORD))) {
                    break;

                } else if (commandMap.containsKey(command))       // if it's a known command
                    commandMap.get(command).accept((socketIn.nextLine()));      // execute it
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
