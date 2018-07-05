package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.rmi.RemoteException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private static final String RMI_CONNECTION_KEYWORD = "CONNECTION_WITH_RMI";
    private static final String CONNECTION_SUCCESS_KEYWORD = "CONNECTION_SUCCESS";
    private static final String TURN_PASSED_KEYWORD = "TURN_PASSED";
    private static final String TOOL_CARD_NOT_FOUND_KEYWORD = "TOOL_CARD_NOT_FOUND";

    private final Lobby lobby;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    private Dice dice;
    private Colors col;
    private EnumMap<ToolCard.Actor, BiConsumer<String, List<Integer>>> diceActorMap;

    public ServerRemoteImpl(Lobby lobby) {
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.lobby = lobby;

        this.diceActorMap = new EnumMap<>(ToolCard.Actor.class);
        mapDiceActor();
    }

    /**
     * Maps enum map for getDiceFromActorMethod
     */
    private void mapDiceActor() {
        BiConsumer<String, List<Integer>> windowCard = (username, coordinates) -> {
            WindowCard card = lobby.getPlayers().get(username).getWindowCard();
            dice = card.getWindow().getCell(coordinates.get(0), coordinates.get(1)).getDice();
        };
        BiConsumer<String, List<Integer>> roundTrack = (username, coordinates) -> {
            Iterator<Dice> itr = lobby.getGame().getBoard().getRoundTrack().getTrackList().get(coordinates.get(0)).itr();
            IntStream.rangeClosed(0, coordinates.get(1)).forEach(integer -> dice = itr.next());
        };
        BiConsumer<String, List<Integer>> draft = (username, coordinates) ->
            dice = lobby.getGame().getBoard().getDraft().getDraftList().get(coordinates.get(0));

        diceActorMap.put(ToolCard.Actor.WINDOW_CARD, windowCard);
        diceActorMap.put(ToolCard.Actor.ROUND_TRACK, roundTrack);
        diceActorMap.put(ToolCard.Actor.DRAFT, draft);
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) {
        try {
            out.println(client.getUsername() + protocol.getMessage(RMI_CONNECTION_KEYWORD));

            client.tell(dictionary.getMessage(CONNECTION_SUCCESS_KEYWORD));

        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param s to be printed
     */
    @Override
    public void tell(String s) {
        out.println(s);
    }

    /**
     * @param username   != null
     * @param client instance of ClientRemote
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    @Override
    public synchronized void login(String username, ClientRemote client) throws TooManyPlayersException, GameAlreadyStartedException, SamePlayerException {
        RmiClientSpeaker speaker = new RmiClientSpeaker(client);

        lobby.addPlayer(username, speaker);
    }

    /**
     * @param username of Player that requested
     * @param cardName of card to be set
     */
    @Override
    public void setWindowCard(String username, String cardName) {
        lobby.getGameController().setWindowCard(username, cardName);
    }


    /**
     * @param usernameWanted = Player.getId() && Player.getWindowCard()
     * @param me             = Player.getId() of who requested
     */
    @Override
    public void getWindowCard(String usernameWanted, String me) {
        lobby.getSpeakers().get(me).printWindowCard(lobby.getPlayers().get(usernameWanted).getWindowCard());
    }

    /**
     * @param currentUser = Player.getId() of who requested
     */
    @Override
    public void getAllUsername(String currentUser) {
        for (String user : lobby.getPlayers().keySet())
            if (!user.equals(currentUser))
                lobby.getSpeakers().get(currentUser).tell(user);
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getDraft(String username) {
        lobby.getSpeakers().get(username).showDraft(lobby.getGame().getBoard().getDraft());
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getRoundTrack(String username) {
        lobby.getSpeakers().get(username).showRoundTrack(lobby.getGame().getBoard().getRoundTrack());
    }

    /**
     * @param username = Player.getId() of who ended turn
     */
    @Override
    public void endTurn(String username) {
        lobby.notifyAllPlayers(username + dictionary.getMessage(TURN_PASSED_KEYWORD));
        lobby.getRoundController().nextTurn();
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getPublicObj(String username) {
        lobby.getSpeakers().get(username).printListPublicObj(lobby.getGame().getBoard().getPublicObj());
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getPrivateObj(String username) {
        lobby.getSpeakers().get(username).printPrivateObj(lobby.getPlayers().get(username).getPrivateObj());
    }

    /**
     * @param username of player moving the dice
     * @param index    in the draft of the dice
     * @param row      of the destination cell
     * @param col      of the destination cell
     * @throws NotTurnException when it's not the turn of the player who requested the action
     * @throws WrongDiceSelectionException when the dice selected is wrong
     * @throws WrongCellSelectionException when the cell selected does not exists
     * @throws NotEmptyException when the cell selected for the movement is already occupied
     * @throws EmptyException when the draft is empty
     * @throws PositionException when parameters of cell are wrong
     * @throws IDNotFoundException when parameters of dice are wrong
     * @throws WrongPositionException when the move is not legal
     * @throws AlreadyDoneException when player has already played dice
     */
    @Override
    public void placementDice(String username, int index, int row, int col) throws WrongDiceSelectionException, EmptyException,
            WrongPositionException, NotTurnException, NotEmptyException, IDNotFoundException, WrongCellSelectionException, PositionException, AlreadyDoneException {
        lobby.getActionController().placeDice(username, index, row, col);
    }

    /**
     * @param username of the player who requested tool cards
     */
    @Override
    public void askToolCards(String username) {
        lobby.getSpeakers().get(username).printListToolCard(lobby.getGame().getBoard().getToolCard());
    }


    /**
     * @param username of the player who requested his number of favor points
     * @throws EmptyException when game is empty
     * @throws PlayerNotFoundException when player it's not in the game
     */
    @Override
    public void printFavorPoint(String username) throws EmptyException, PlayerNotFoundException {
        lobby.getSpeakers().get(username).printNumberFavorPoint(lobby.getGame().findPlayer(username).getFavorPoint());
    }

    /**
     * @param pick     index of tool card chosen
     * @param username of the player who wants to activate the tool
     * @return true if it can be activated, false else
     * @throws EmptyException when game is empty
     * @throws PlayerNotFoundException when player it's not in the game
     * @throws IDNotFoundException when tool card is not found
     */
    @Override
    public Boolean checkPreCondition(int pick, String username) throws EmptyException, PlayerNotFoundException, IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        Player p = lobby.getGame().findPlayer(username);
        WindowCard card = p.getWindowCard();
        return lobby.getGame().getBoard().getToolCard().get(pick).checkPreCondition(p, card);
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @throws IDNotFoundException when tool card is not found
     * @return list of Enum Actor
     */
    @Override
    public List<ToolCard.Actor> getActor(int pick, String username) throws IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return lobby.getGame().getBoard().getToolCard().get(pick).getActor();
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @throws IDNotFoundException when tool card is not found
     * @return list of Enum Parameter
     */
    @Override
    public List<ToolCard.Parameter> getParameter(int pick, String username) throws IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return lobby.getGame().getBoard().getToolCard().get(pick).askParameter();
    }

    /**
     * @param pick      index of tool card chosen by player
     * @param dices     requested by tool card, dices.size() == 0 || 1 || 2
     * @param cells     requested by tool card, cells.size() == 0 || 1 || 2
     * @param diceValue if value of a dice need to be changed, 0 if not needed
     * @param diceColor if color on round track is necessary for the tool card, null if not needed
     * @return true if tool can be used with passed parameters, false else
     * @throws PositionException when wrong cells are passed
     * @throws IDNotFoundException when tool card is not found
     */
    @Override
    public Boolean checkTool(int pick, List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) throws PositionException, IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return lobby.getGame().getBoard().getToolCard().get(pick).checkTool(dices, cells, diceValue, diceColor);
    }

    /**
     * @param pick  index of tool card chosen by player
     * @param dices null when not needed
     * @param up    null when not needed
     * @param cells null when not needed
     * @return true if move was successful, else false
     * @throws ValueException when wrong value are chosen
     * @throws IDNotFoundException when couldn't find a dice or when tool card is not found
     * @throws NotEmptyException when trying to stack dice on the same cell
     * @throws EmptyException when trying to get dice from empty draft or bag
     * @throws SameDiceException when trying to put the same dice twice
     * @throws RoundNotFoundException when wrong round is requested
     */
    @Override
    public Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells) throws NotEmptyException, EmptyException, ValueException,
            RoundNotFoundException, IDNotFoundException, SameDiceException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return lobby.getGame().getBoard().getToolCard().get(pick).useTool(dices, up, cells);
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
        if (diceActorMap.containsKey(actor))
            diceActorMap.get(actor).accept(username, coordinates);
        else
            dice = null;

        return dice;
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol()
     * @return Cell asked by user, null if any problem occurs
     */
    @Override
    public Cell getCellFromWindow(String username, List<Integer> coordinates) {
        return lobby.getPlayers().get(username).getWindowCard().getWindow().getCell(coordinates.get(0), coordinates.get(1));
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    @Override
    public Colors getColorFromRoundTrack(String username, List<Integer> coordinates) {
        Iterator<Dice> itr = lobby.getGame().getBoard().getRoundTrack().getTrackList().get(coordinates.get(0)).itr();
        IntStream.rangeClosed(0, coordinates.get(1)).forEach(integer -> col = itr.next().getColor());

        return col;
    }

}
