package it.polimi.ingsw.client.network;

import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;

import java.util.List;

/**
 * Interface to hide network difference in comm Client -> Server
 */
public interface ServerSpeaker {

    /**
     * Used locally to change ip after a failed connection toward server
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    void setIp(String ip);

    /**
     * Used to connect client to server. No control on username yet.
     * @param username != null
     * @return true if connection was successful, false else
     */
    boolean connect(String username);

    /**
     * Used to login client to server. Username and other restrictions are controlled.
     * @param username != null
     * @return true if login was successful, false else
     */
    boolean login(String username);

    /**
     * Used to set the window card chosen by passed player
     * @param username = Player.getId()
     * @param cardName = Player.getWindowCard().getName()
     */
    void setWindowCard(String username, String cardName);

    /**
     * Used to get which window card has the passed player
     * @param usernameWanted = Player.getId()
     * @param me username of player that requested
     */
    void askWindowCard(String usernameWanted, String me);

    /**
     * Used to get username of all Players in the game
     * @param currentUser username of player that requested
     */
    void getAllUsername(String currentUser);

    /**
     * Used to get Draft of current round
     * @param username of player that requested
     */
    void askDraft(String username);

    /**
     * Used to get Round Track of the current game
     * @param username of player that requested
     */
    void askRoundTrack(String username);

    /**
     * Used to get public objectives of the game
     * @param username of player that requested
     */
    void askPublicObj(String username);

    /**
     * Used to get private objective of the passed player. Only works for owner private objective.
     * @param username = Player.getId() && Player.getPrivateObj()
     */
    void askPrivateObj(String username);

    /**
     * Used to ask tool cards of the game
     * @param username of player that requested
     */
    void askToolCards(String username);

    /**
     * Used to ask number of player's favor points
     * @param username of player that requested
     */
    void askFavorPoints(String username);

    /**
     * Used to pass the current player
     * @param username of Player that wants to end his turn
     */
    void endTurn(String username);

    /**
     * Used to place dice, passing index of dice in the draft and row and column of the destination cell
     * @param username of player moving the dice
     * @param index in the draft of the dice
     * @param row of the destination cell
     * @param col of the destination cell
     */
    void placementDice(String username, int index, int row, int col);

    /**
     * Used to check if a tool card can be activated by player
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return true if it can be activated, false else
     */
    Boolean checkPreCondition(int pick, String username);

    /**
     * Used to get which elements of board are involved in selected window card
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Actor if everything okay, Collections.emptyList() if not okay
     */
    List<ToolCard.Actor> getActor(int pick, String username);

    /**
     * Used to get which parameters are requested to use selected tool card
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Parameter if everything okay, Collections.emptyList() if not okay
     */
    List<ToolCard.Parameter> getParameter(int pick, String username);

    /**
     * Used to check if the selected tool can be used with passed parameter
     * @param pick index of tool card chosen by player
     * @param dices requested by tool card, dices.size() == 0 || 1 || 2
     * @param cells requested by tool card, cells.size() == 0 || 1 || 2
     * @param diceValue if value of a dice need to be changed, 0 if not needed
     * @param diceColor if color on round track is necessary for the tool card, null if not needed
     * @return true if tool can be used with passed parameters, false else
     */
    Boolean checkTool(int pick, List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor);

    /**
     * Realize the effect of the tool card on the passed parameter
     * @param pick index of tool card chosen by player
     * @param dices null when not needed
     * @param up null when not needed
     * @param cells null when not needed
     * @param username of who requested
     * @return true if move was successful, else false
     */
    Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells, String username);

    /**
     * Used to get requested Dice using passed coordinates to localize it through passed Actor
     * @param actor can be Draft, Window Card or Round Track
     * @param username of who requested
     * @param coordinates there could be various type of coordinates depending on which actor is passed
     *                    if Draft, coordinates.size() = 1 and coordinates contains index of dice in the draft
     *                    if Round Track, coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     *                    if Window Card, coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol() of where the dice is located
     * @return Dice asked by user, null if any problem occurs
     */
    Dice getDiceFromActor(ToolCard.Actor actor, String username, List<Integer> coordinates);

    /**
     * Used to get requested Cell using passed coordinates to localize it in the window card of the user
     * @param username of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol()
     * @return Cell asked by user, null if any problem occurs
     */
    Cell getCellFromWindow(String username, List<Integer> coordinates);

    /**
     * Used to get the Color of one dice of the round track
     * @param username of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    Colors getColorFromRoundTrack(String username, List<Integer> coordinates);

    /**
     * Used to quit the game
     * @param username user that wants to quit
     */
    void quit(String username);
}

