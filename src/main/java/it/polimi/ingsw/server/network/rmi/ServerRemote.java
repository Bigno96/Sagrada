package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface of server used by client
 */
public interface ServerRemote extends Remote {

    /**
     * Used to connect a client to the server.
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     * @throws RemoteException default
     */
    void connect(String username, ClientRemote client) throws RemoteException;

    /**
     * Used to print on Server
     * @param s to be printed
     * @throws RemoteException default
     */
    void tell(String s) throws RemoteException;

    /**
     * Used to add Player to the lobby pre game.
     * @param username != null
     * @param client instance of ClientRemote
     * @throws RemoteException default
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    void login(String username, ClientRemote client) throws RemoteException, TooManyPlayersException, GameAlreadyStartedException, SamePlayerException;

    /**
     * Used to set Window Card chosen as Player's window card
     * @param username of Player that requested
     * @param cardName of card to be set
     * @throws RemoteException default
     */
    void setWindowCard(String username, String cardName) throws RemoteException;

    /**
     * Used to obtain Window Card of usernameWanted player and show it to me player
     * @param usernameWanted = Player.getId() && Player.getWindowCard()
     * @param me = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getWindowCard(String usernameWanted, String me) throws RemoteException;

    /**
     * Used to get all username of Player in the game
     * @param currentUser = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getAllUsername(String currentUser) throws RemoteException;

    /**
     * Used to get draft for current round
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getDraft(String username) throws RemoteException;

    /**
     * Used to get round track of the current game
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getRoundTrack(String username) throws RemoteException;

    /**
     * Used to end the turn
     * @param username = Player.getId() of who ended turn
     * @throws RemoteException default
     */
    void endTurn(String username) throws RemoteException;

    /**
     * Used to get Public Objectives selected for current game
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getPublicObj(String username) throws RemoteException;

    /**
     * Used to get Private Objective of a Player. Only works if requested by the Objective's owner
     * @param username = Player.getId() of who requested
     * @throws RemoteException default
     */
    void getPrivateObj(String username) throws RemoteException;

    /**
     * Used to place dice for a user. Receiving index of the dice in the draft and cell's coordinates of the destination.
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
    void placementDice(String username, int index, int row, int col) throws RemoteException, WrongDiceSelectionException,
            EmptyException, WrongPositionException, NotTurnException, NotEmptyException, IDNotFoundException, WrongCellSelectionException, PositionException, AlreadyDoneException;

    /**
     * Used by an user to request printing of tool card
     * @param username of the player who requested tool cards
     * @throws RemoteException default
     */
    void askToolCards(String username) throws RemoteException;


    /**
     * Used by a player to get how much favor points he has left
     * @param username of the player who requested his number of favor points
     * @throws RemoteException default
     * @throws EmptyException when game is empty
     * @throws PlayerNotFoundException when player it's not in the game
     */
    void printFavorPoint(String username) throws RemoteException, EmptyException, PlayerNotFoundException;

    /**
     * Used to check if a tool card can be activated by a player or not
     * @param pick index of tool card chosen
     * @param username of the player who wants to activate the tool
     * @return true if it can be activated, false else
     * @throws RemoteException default
     * @throws EmptyException when game is empty
     * @throws PlayerNotFoundException when player it's not in the game
     * @throws IDNotFoundException when tool card is not found
     * @throws NotEnoughFavorPointsException when player has not enough favor points to play the tool card
     */
    Boolean checkPreCondition(int pick, String username) throws RemoteException, EmptyException, PlayerNotFoundException, IDNotFoundException, NotEnoughFavorPointsException;

    /**
     * Used to get which elements of board are involved in selected window card
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Actor
     * @throws RemoteException default
     * @throws IDNotFoundException when tool card is not found
     */
    List<ToolCard.Actor> getActor(int pick, String username) throws RemoteException, IDNotFoundException;

    /**
     * Used to get which parameters are requested to use selected tool card
     * @param pick index of tool card chosen by player
     * @param username of the player that requested
     * @return list of Enum Parameter
     * @throws RemoteException default
     * @throws IDNotFoundException when tool card is not found
     */
    List<ToolCard.Parameter> getParameter(int pick, String username) throws RemoteException, IDNotFoundException;

    /**
     * Used to check if the selected tool can be used with passed parameter
     * @param pick index of tool card chosen by player
     * @param dices requested by tool card, dices.size() == 0 || 1 || 2
     * @param cells requested by tool card, cells.size() == 0 || 1 || 2
     * @param diceValue if value of a dice need to be changed, 0 if not needed
     * @param diceColor if color on round track is necessary for the tool card, null if not needed
     * @return true if tool can be used with passed parameters, false else
     * @throws RemoteException default
     * @throws PositionException when wrong cells are passed
     * @throws IDNotFoundException when tool card is not found
     */
    Boolean checkTool(int pick, List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) throws RemoteException, PositionException, IDNotFoundException;

    /**
     * Realize the effect of the tool card on the passed parameter
     * @param pick index of tool card chosen by player
     * @param dices null when not needed
     * @param up null when not needed
     * @param cells null when not needed
     * @param username of who requested
     * @return true if move was successful, else false
     * @throws RemoteException default
     * @throws ValueException when wrong value are chosen
     * @throws IDNotFoundException when couldn't find a dice or when tool card is not found
     * @throws NotEmptyException when trying to stack dice on the same cell
     * @throws EmptyException when trying to get dice from empty draft or bag
     * @throws SameDiceException when trying to put the same dice twice
     * @throws RoundNotFoundException when wrong round is requested
     * @throws PlayerNotFoundException when player is not found in the game
     */
    Boolean useTool(int pick, List<Dice> dices, Boolean up, List<Cell> cells, String username) throws RemoteException, NotEmptyException, EmptyException,
            ValueException, RoundNotFoundException, IDNotFoundException, SameDiceException, PlayerNotFoundException;

    /**
     * Used to get requested Dice using coordinates to localize it through passed Actor
     * @param actor       can be Draft, Window Card or Round Track
     * @param username    of who requested
     * @param coordinates there could be various type of coordinates depending on which actor is passed
     *                    if Draft, coordinates.size() = 1 and coordinates contains index of dice in the draft
     *                    if Round Track, coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     *                    if Window Card, coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol() of where the dice is located
     * @throws RemoteException default
     * @return Dice asked by user, null if any problem occurs
     */
    Dice getDiceFromActor(ToolCard.Actor actor, String username, List<Integer> coordinates) throws RemoteException;

    /**
     * Used to get requested Cell using passed coordinates to localize it in the window card of the user
     * @param username of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = cell.getRow() and coordinates.get(1) = cell.getCol()
     * @return Cell asked by user, null if any problem occurs
     */
    Cell getCellFromWindow(String username, List<Integer> coordinates) throws RemoteException;

    /**
     * Used to get the Color of one dice of the round track
     * @param username of player who requested
     * @param coordinates coordinates.size() = 2 and coordinates.get(0) = number of round where dice is and coordinates.get(1) = index of dice in list dice round
     * @return Color asked by user, null if any problem occurs
     */
    Colors getColorFromRoundTrack(String username, List<Integer> coordinates) throws RemoteException;

    /**
     * Used to quit the game
     * @param username user that wants to quit
     */
    void quit(String username) throws RemoteException;
}
