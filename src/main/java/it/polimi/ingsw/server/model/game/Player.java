package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class Player extends Observable {

    private static final String NOTIFY_PRIVATE_OBJ = "PrivateObjective";

    private static final String DUMP_ID_MSG = "ID: ";
    private static final String DUMP_PRIVATE_MSG = " PrivateObj: ";
    private static final String DUMP_WINDOW_CARD_MSG = " WinCard: ";
    private static final String DUMP_FAVOR_POINT_MSG = "FavorPoint: ";

    private enum playerState { DISCONNECTED, FIRST_TURN, SECOND_TURN, PLAYED_DICE, USED_TOOL }

    private List<playerState> currentState;
    private String id;
    private ObjectiveCard privateObj;
    private Board board;
    private WindowCard windCard;
    private int favorPoint;

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    /**
     * Constructor
     * @param id != null
     */
    public Player(String id) {
        this.id = id;
        this.favorPoint = 0;
        this.currentState = new ArrayList<>();
        this.privateObj = null;
        this.windCard = null;
        this.board = null;

        currentState.add(playerState.FIRST_TURN);
        currentState.add(playerState.SECOND_TURN);
    }

    /**
     * Set the WindowCard and Number of Favor Points of the Player
     * @param windCard chosen by the Player
     */
    public void setWindowCard(WindowCard windCard) {
        this.windCard = windCard;
        this.favorPoint = windCard.getNumFavPoint();

        setChanged();
        notifyObservers(this.id);
    }

    /**
     * @param board to be set for the player
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return window card of the player
     */
    public WindowCard getWindowCard() {
        return windCard;
    }

    /**
     * @return String id of the player
     */
    public String getId() {
        return id;
    }

    /**
     * @return true if is player's first turn
     */
    public boolean isFirstTurn() {
        return currentState.contains(playerState.FIRST_TURN);
    }

    /**
     * @param firstTurn to determine if first turn has to be set or removed
     */
    public void setFirstTurn(Boolean firstTurn) {
        if (firstTurn)
            currentState.add(playerState.FIRST_TURN);
        else
            currentState.remove(playerState.FIRST_TURN);
    }

    /**
     * @return true if is player's second turn
     */
    public boolean isSecondTurn() {
        return currentState.contains(playerState.SECOND_TURN);
    }

    /**
     * @param secondTurn to determine if second turn has to be set or removed
     */
    public void setSecondTurn(Boolean secondTurn) {
        if (secondTurn)
            currentState.add(playerState.SECOND_TURN);
        else
            currentState.remove(playerState.SECOND_TURN);
    }

    /**
     * @param privateObj to be set for this player
     */
    public void setPrivateObj(ObjectiveCard privateObj) {
        this.privateObj = privateObj;

        setChanged();
        notifyObservers(NOTIFY_PRIVATE_OBJ);
    }

    /**
     * @return private objective of this player
     */
    public ObjectiveCard getPrivateObj() {
        return privateObj.copy();
    }

    /**
     * @param favorPoint to be set for this player
     */
    public void setFavorPoint(int favorPoint) {
        this.favorPoint = favorPoint;
    }

    /**
     * @return number of favor point of this player
     */
    public int getFavorPoint() {
        return favorPoint;
    }

    /**
     * @return true if player already played a dice
     */
    public boolean isPlayedDice() {
        return currentState.contains(playerState.PLAYED_DICE);
    }

    /**
     * @param playedDice to determine if played Dice has to be set or reset
     */
    public void setPlayedDice(Boolean playedDice) {
        if (playedDice)
            currentState.add(playerState.PLAYED_DICE);
        else
            currentState.remove(playerState.PLAYED_DICE);
    }

    /**
     * @return true if player already used a tool card
     */
    public boolean isUsedTool() {
        return currentState.contains(playerState.USED_TOOL);
    }

    /**
     * @param usedTool to determine if used Tool has to be set or reset
     */
    public void setUsedTool(Boolean usedTool) {
        if (usedTool)
            currentState.add(playerState.USED_TOOL);
        else
            currentState.remove(playerState.USED_TOOL);
    }


    /**
     * Score Calculation
     * @return sum of points
     * @throws FileNotFoundException PublicObjCard throw FileNotFoundException
     * @throws IDNotFoundException PublicObjCard throw IDNotFoundException
     * @throws PositionException PublicObjCard throw PositionException
     * @throws ValueException PublicObjCard throw ValueException
     */
    public int rateScore() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        int sum = 0;

        List<ObjectiveCard> publicObj = board.getPublicObj();
        for (ObjectiveCard obj : publicObj)
            sum += obj.calcPoint(windCard);

        sum += privateObj.calcPoint(windCard);
        sum += favorPoint;
        sum -= windCard.numEmptyCells();

        return sum;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_ID_MSG + getId() + DUMP_PRIVATE_MSG + getPrivateObj() + DUMP_WINDOW_CARD_MSG + getWindowCard()
                + DUMP_FAVOR_POINT_MSG + getFavorPoint());
    }

    /**
     * @return true if player is disconnected
     */
    public boolean isDisconnected() {
        return currentState.contains(playerState.DISCONNECTED);
    }

    /**
     * @param disconnected to determine if disconnected has to be set or reset
     */
    public void setDisconnected(boolean disconnected) {
        if (disconnected)
            currentState.add(playerState.DISCONNECTED);
        else
            currentState.remove(playerState.DISCONNECTED);
    }
}