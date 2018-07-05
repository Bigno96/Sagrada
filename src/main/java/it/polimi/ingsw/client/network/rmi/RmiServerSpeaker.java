package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Rmi version of server speaker
 */
public class RmiServerSpeaker implements ServerSpeaker {

    private static final String USER_CONNECTING_KEYWORD = "USER_CONNECTING";
    private static final String SERVER_REMOTE_KEYWORD = "SERVER_REMOTE";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String SERVER_NOT_RESPONDING_KEYWORD = "SERVER_NOT_RESPONDING";
    private static final String SOMETHING_WENT_WRONG_KEYWORD = "SOMETHING_WENT_WRONG";

    private static final String NOT_YOUR_TURN_EXCEPTION_KEYWORD = "NOT_YOUR_TURN_EXCEPTION";
    private static final String ALREADY_DONE_KEYWORD = "ALREADY_DONE";
    private static final String NOT_EMPTY_EXCEPTION_KEYWORD = "NOT_EMPTY_EXCEPTION";
    private static final String EMPTY_CARD_KEYWORD = "EMPTY_CARD_EXCEPTION";
    private static final String WRONG_POSITION_EXCEPTION_KEYWORD = "WRONG_POSITION_EXCEPTION";
    private static final String WRONG_DICE_INDEX_PLACEMENT_KEYWORD = "WRONG_DICE_INDEX_PLACEMENT";
    private static final String WRONG_CELL_PLACEMENT_KEYWORD = "WRONG_CELL_PLACEMENT";
    private static final String EMPTY_GAME_EXCEPTION_KEYWORD = "EMPTY_GAME_EXCEPTION";
    private static final String NOT_IN_GAME_EXCEPTION_KEYWORD = "NOT_IN_GAME_EXCEPTION";

    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server
    private final ViewInterface view;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    public RmiServerSpeaker(String ip, String username, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        try {
            this.client = new ClientRemoteImpl(username, view);
        } catch (RemoteException e) {
            view.print(e.getMessage());
        }
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean connect(String username) {
        view.print(dictionary.getMessage(USER_CONNECTING_KEYWORD) + ip);

        NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();

        try {
            Registry registry = LocateRegistry.getRegistry(ip, parser.getRmiServerPort());
            server = (ServerRemote) registry.lookup(protocol.getMessage(SERVER_REMOTE_KEYWORD));        // find remote interface

            server.connect(username, client);

            return true;

        } catch (RemoteException | NotBoundException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username != null
     * @return true if login was successful, false else
     */
    @Override
    public boolean login(String username) {
        try {
            server.login(username, client);                             // add this player to a game Lobby
            server.tell("User " + username + " " + protocol.getMessage(LOGIN_SUCCESS_KEYWORD));
            return true;

        } catch (SamePlayerException | TooManyPlayersException | GameAlreadyStartedException | RemoteException e) {
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
        try {
            server.setWindowCard(username, cardName);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param usernameWanted = Player.getId()
    * @param me username of player that requested
     */
    @Override
    public void askWindowCard(String usernameWanted, String me) {
        try {
            server.getWindowCard(usernameWanted, me);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param currentUser username of player that requested
     */
    @Override
    public void getAllUsername(String currentUser) {
        try {
            server.getAllUsername(currentUser);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askDraft(String username) {
        try {
            server.getDraft(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askRoundTrack(String username) {
        try {
            server.getRoundTrack(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askPublicObj(String username) {
        try {
            server.getPublicObj(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username = Player.getId() && Player.getPrivateObj()
     */
    @Override
    public void askPrivateObj(String username) {
        try {
            server.getPrivateObj(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askToolCards(String username) {
        try {
            server.askToolCards(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askFavorPoints(String username) {
        try {
            server.printFavorPoint(username);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));

        } catch (PlayerNotFoundException e) {
            view.print(dictionary.getMessage(NOT_IN_GAME_EXCEPTION_KEYWORD));

        } catch (EmptyException e) {
            view.print(dictionary.getMessage(EMPTY_GAME_EXCEPTION_KEYWORD));
        }
    }

    /**
     * @param username of Player that wants to end his turn
     */
    @Override
    public void endTurn(String username) {
        try {
            server.endTurn(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
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
        try {
            server.placementDice(username, index, row, col);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));

        } catch (EmptyException e) {
            view.wrongPlacementDice(dictionary.getMessage(EMPTY_CARD_KEYWORD));

        } catch (NotTurnException e) {
            view.wrongPlacementDice(dictionary.getMessage(NOT_YOUR_TURN_EXCEPTION_KEYWORD));

        } catch (AlreadyDoneException e) {
            view.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));

        } catch (WrongPositionException e) {
            view.wrongPlacementDice(dictionary.getMessage(WRONG_POSITION_EXCEPTION_KEYWORD) + " - " + e.getMessage());

        } catch (WrongDiceSelectionException | IDNotFoundException e) {
            view.wrongPlacementDice(dictionary.getMessage(WRONG_DICE_INDEX_PLACEMENT_KEYWORD));

        } catch (NotEmptyException e) {
            view.wrongPlacementDice(dictionary.getMessage(NOT_EMPTY_EXCEPTION_KEYWORD) + " - " + e.getMessage());

        } catch (WrongCellSelectionException | PositionException e) {
            view.wrongPlacementDice(dictionary.getMessage(WRONG_CELL_PLACEMENT_KEYWORD));
        }
    }

    /**
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return true if can be activated, false else
     */
    @Override
    public Boolean checkPreCondition(int pick, String username) {
        try {
            return server.checkPreCondition(pick, username);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return false;

        } catch (PlayerNotFoundException e) {
            view.print(dictionary.getMessage(NOT_IN_GAME_EXCEPTION_KEYWORD));
            return false;

        } catch (EmptyException e) {
            view.print(dictionary.getMessage(EMPTY_GAME_EXCEPTION_KEYWORD));
            return false;

        } catch (IDNotFoundException | NotEnoughFavorPointsException e) {
            view.print(e.getMessage());
            return false;

        }
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Actor if everything okay, Collections.emptyList() if not okay
     */
    @Override
    public List<ToolCard.Actor> getActor(int pick, String username) {
        try {
            return server.getActor(pick, username);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return Collections.emptyList();

        } catch (IDNotFoundException e) {
            view.print(e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Parameter if everything okay, Collections.emptyList() if not okay
     */
    @Override
    public List<ToolCard.Parameter> getParameter(int pick, String username) {
        try {
            return server.getParameter(pick, username);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return Collections.emptyList();

        } catch (IDNotFoundException e) {
            view.print(e.getMessage());
            return Collections.emptyList();
        }
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
        try {
            return server.checkTool(pick, dices, cells, diceValue, diceColor);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return false;

        } catch (PositionException e) {
            view.print(dictionary.getMessage(SOMETHING_WENT_WRONG_KEYWORD));
            return false;

        } catch (IDNotFoundException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param pick  index of tool card chosen by player
     * @param dices null when not needed
     * @param up    null when not needed
     * @param cells null when not needed
     * @param username of who requested
     * @return true if move was successful, else false
     */
    @Override
    public Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells, String username) {
        try {
            return server.useTool(pick, dices, up, cells, username);

        } catch (RemoteException e) {
           view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
           return false;

        } catch (NotEmptyException | EmptyException | ValueException | RoundNotFoundException | IDNotFoundException |
                SameDiceException | PlayerNotFoundException e) {
            view.print(e.getMessage());
            return false;
        }
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
        try {
            return server.getDiceFromActor(actor, username, coordinates);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return null;
        }
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol()
     * @return Cell asked by user, null if any problem occurs
     */
    @Override
    public Cell getCellFromWindow(String username, List<Integer> coordinates) {
        try {
            return server.getCellFromWindow(username, coordinates);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return null;
        }
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    @Override
    public Colors getColorFromRoundTrack(String username, List<Integer> coordinates) {
        try {
            return server.getColorFromRoundTrack(username, coordinates);

        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            return null;
        }
    }
}


