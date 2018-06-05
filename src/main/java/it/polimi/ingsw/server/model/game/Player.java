package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class Player extends Observable {

    private String id;
    private boolean disconnected;
    private boolean firstTurn;
    private boolean secondTurn;
    private ObjectiveCard privObj;
    private Board board;
    private WindowCard windCard;
    private int favorPoint;
    private boolean playedDice;     //true if player play a Dice in this turn
    private boolean usedTool;       //true if player used a ToolCard in this turn
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    /**
     * Constructor
     * @param id != null
     */
    public Player(String id) {
        this.id = id;
        this.favorPoint = 0;
        this.firstTurn = true;      //first turn of round
        this.secondTurn = true;     //second turn of round
        this.privObj = null;
        this.windCard = null;
        this.board = null;
        this.playedDice = false;
        this.disconnected = false;
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

    public void setBoard(Board board) {
        this.board = board;
    }

    public WindowCard getWindowCard() {
        return windCard;
    }

    public String getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isFirstTurn() {
        return this.firstTurn;
    }

    public void resetFirstTurn() {
        this.firstTurn = true;
    }

    public void endFirstTurn() {
        this.firstTurn = false;
    }   //when first turn (of the round) of the player finished

    public boolean isSecondTurn() {
        return this.secondTurn;
    }

    public void resetSecondTurn() {
        this.secondTurn = true;
    }

    public void endSecondTurn() {
        this.secondTurn = false;
    }   //when second turn (of the round) of the player finished

    public void setPrivObj(ObjectiveCard privObj) {
        this.privObj = privObj;
    }

    public ObjectiveCard getPrivObj() {
        return privObj;
    }

    public void setFavorPoint(int favorPoint) {
        this.favorPoint = favorPoint;
    }

    public int getFavorPoint() {
        return favorPoint;
    }

    public boolean isPlayedDice() {
        return playedDice;
    }

    public void resetPlayedDice() {
        this.playedDice = false;
    }

    public void playDice() {
        this.playedDice = true;
    }

    public boolean isUsedTool() {
        return this.usedTool;
    }

    public void useTool() {
        this.usedTool = true;
    }

    public void resetUsedTool() {
        this.usedTool = false;
    }

    /**
     * Score Calculation
     * @return sum of points
     * @throws FileNotFoundException PublObjCard throw FileNotFoundException
     * @throws IDNotFoundException PublObjCard throw IDNotFoundException
     * @throws PositionException PublObjCard throw PositionException
     * @throws ValueException PublObjCard throw ValueException
     */
    public int rateScore() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        int sum = 0;
        List<ObjectiveCard> publObj = board.getPublObj();
        for (ObjectiveCard obj : publObj) {
            sum += obj.calcPoint(windCard);
        }
        sum += privObj.calcPoint(windCard);
        sum += favorPoint;
        sum -= windCard.numEmptyCells();
        return sum;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("ID: " + getId() + " PrivObj: " + getPrivObj() + " WinCard: " + getWindowCard()+ "FavorPoint: "
                + getFavorPoint());
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }
}