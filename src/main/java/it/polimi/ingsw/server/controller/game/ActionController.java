package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * Controls actions of player
 */
public class ActionController {

    private static final String TOOL_CARD_NOT_FOUND_KEYWORD = "TOOL_CARD_NOT_FOUND";
    private static final String NOT_ENOUGH_FAVOR_POINT_KEYWORD = "NOT_ENOUGH_FAVOR_POINT";

    private final Lobby lobby;
    private final Game game;
    private Cell dest;

    private final ViewMessageParser dictionary;
    private final GameSettingsParser gameSettings;

    private Dice dice;
    private Colors col;
    private EnumMap<ToolCard.Actor, BiConsumer<String, List<Integer>>> diceActorMap;

    public ActionController(Lobby lobby, Game game) {
        this.lobby = lobby;
        this.game = game;
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();

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
     * @param username of player moving the dice
     * @param index in the draft of the dice
     * @param row of the destination cell
     * @param col of the destination cell
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
    public void placeDice(String username, int index, int row, int col) throws NotTurnException, WrongDiceSelectionException,
            WrongCellSelectionException, NotEmptyException, PositionException, IDNotFoundException, WrongPositionException, EmptyException, AlreadyDoneException {

        if (!game.getCurrentPlayer().getId().equals(username))
            throw new NotTurnException();

        if (game.getCurrentPlayer().isPlayedDice())
            throw new AlreadyDoneException();

        if (index < 0 || index >= game.getBoard().getDraft().getDraftList().size())
            throw new WrongDiceSelectionException();

        if (row < 0 || row >= gameSettings.getWindowCardMaxRow() || col < 0 || col >= gameSettings.getWindowCardMaxColumn())
            throw new WrongCellSelectionException();

        Dice d = game.getBoard().getDraft().getDraftList().get(index);
        WindowCard card = game.getCurrentPlayer().getWindowCard();
        dest = card.getWindow().getCell(row, col);

        dest.setDice(d);

        Boolean correct;

        try {
            if (card.numEmptyCells() == ((gameSettings.getWindowCardMaxColumn() * gameSettings.getWindowCardMaxRow())-1))
                correct = card.checkFirstDice();
            else
                correct = card.checkPlaceCond();

        } catch (WrongPositionException | EmptyException | PositionException e) {
            rollback();
            throw e;
        }

        if (correct) {
            game.getBoard().getDraft().rmDice(dice);
            card.setPlacement(dest);
        }
        else
            rollback();
    }

    /**
     * Rolling back the incorrect move
     */
    private void rollback() {
        dest.freeCell();
    }

    /**
     * @param pick     index of tool card chosen
     * @param username of the player who wants to activate the tool
     * @return true if it can be activated, false else
     * @throws EmptyException when game is empty
     * @throws PlayerNotFoundException when player it's not in the game
     * @throws IDNotFoundException when tool card is not found
     * @throws NotEnoughFavorPointsException when player has not enough favor points to play the tool card
     */
    public Boolean checkPreCondition(int pick, String username) throws EmptyException, PlayerNotFoundException, IDNotFoundException, NotEnoughFavorPointsException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }

        ToolCard toolCard = game.getBoard().getToolCard().get(pick);
        Player p = game.findPlayer(username);
        WindowCard card = p.getWindowCard();

        if (!toolCard.checkFavorPoint(p))
            throw new NotEnoughFavorPointsException(dictionary.getMessage(NOT_ENOUGH_FAVOR_POINT_KEYWORD));

        return toolCard.checkPreCondition(p, card);
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @throws IDNotFoundException when tool card is not found
     * @return list of Enum Actor
     */
    public List<ToolCard.Actor> getActor(int pick, String username) throws IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return game.getBoard().getToolCard().get(pick).getActor();
    }

    /**
     * @param pick     index of tool card chosen by player
     * @param username of the player that requested
     * @throws IDNotFoundException when tool card is not found
     * @return list of Enum Parameter
     */
    public List<ToolCard.Parameter> getParameter(int pick, String username) throws IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return game.getBoard().getToolCard().get(pick).askParameter();
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
    public Boolean checkTool(int pick, List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) throws PositionException, IDNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }
        return game.getBoard().getToolCard().get(pick).checkTool(dices, cells, diceValue, diceColor);
    }

    /**
     * @param pick  index of tool card chosen by player
     * @param dices null when not needed
     * @param up    null when not needed
     * @param cells null when not needed
     * @param username of who requested
     * @return true if move was successful, else false
     * @throws ValueException when wrong value are chosen
     * @throws IDNotFoundException when couldn't find a dice or when tool card is not found
     * @throws NotEmptyException when trying to stack dice on the same cell
     * @throws EmptyException when trying to get dice from empty draft or bag
     * @throws SameDiceException when trying to put the same dice twice
     * @throws RoundNotFoundException when wrong round is requested
     * @throws PlayerNotFoundException when player is not found in the game
     */
    public Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells, String username) throws NotEmptyException, EmptyException, ValueException,
            RoundNotFoundException, IDNotFoundException, SameDiceException, PlayerNotFoundException {
        if (pick < 0 || pick > 2) {
            throw new IDNotFoundException(dictionary.getMessage(TOOL_CARD_NOT_FOUND_KEYWORD));
        }

        ToolCard card = game.getBoard().getToolCard().get(pick);

        if (card.useTool(dices, up, cells)) {
            Player p = game.findPlayer(username);
            p.setFavorPoint(p.getFavorPoint() - card.addFavorPoint());
            card.setChangedAndNotify();
            return true;
        }

        return false;
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
    public Cell getCellFromWindow(String username, List<Integer> coordinates) {
        return lobby.getPlayers().get(username).getWindowCard().getWindow().getCell(coordinates.get(0), coordinates.get(1));
    }

    /**
     * @param username    of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    public Colors getColorFromRoundTrack(String username, List<Integer> coordinates) {
        Iterator<Dice> itr = lobby.getGame().getBoard().getRoundTrack().getTrackList().get(coordinates.get(0)).itr();
        IntStream.rangeClosed(0, coordinates.get(1)).forEach(integer -> col = itr.next().getColor());

        return col;
    }
}
