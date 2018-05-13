package model;

import model.objectivecard.ObjectiveCard;
import model.windowcard.WindowCard;

import java.util.logging.Logger;

public class Player {

    private int id;
    private boolean firstTurn;
    private boolean secondTurn;
    private ObjectiveCard privObj;
    private Board board;
    private WindowCard windCard;
    private int favorPoint;
    private boolean playedDice;
    private boolean usedTool;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Player(int id, Board board) {
        this.id = id;
        this.favorPoint = 0;
        this.firstTurn = true;
        this.secondTurn = true;
        this.privObj = null;
        this.windCard = null;
        this.board = board;
        this.playedDice = false;
    }

    public void setWindowCard(WindowCard windCard) {
        this.windCard = windCard;
        this.favorPoint = windCard.getNumFavPoint();
    }

    public WindowCard getWindowCard() {
        return windCard;
    }

    public int getId() {
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
    }

    public boolean isSecondTurn() {
        return this.secondTurn;
    }

    public void resetSecondTurn() {
        this.secondTurn = true;
    }

    public void endSecondTurn() {
        this.secondTurn = false;
    }

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

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("ID: " + getId() /*+ " PrivObj: " + getPrivObj() + " WinCard: " + getWind()*/ + "FavorPoint: "
                + getFavorPoint());
    }


}