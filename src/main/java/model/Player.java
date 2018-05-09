package model;

import java.util.logging.Logger;

public class Player {

    private int id;
    private boolean turn;
    private ObjectiveCard privObj;
    private Board board;
    private WindowCard windCard;
    private int favorPoint;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Player(int id, Board board) {
        this.id = id;
        this.favorPoint = 0;
        this.turn = true;
        this.privObj = null;
        this.windCard = null;
        this.board = board;
    }

    public void setWindowCard(WindowCard windCard){
        this.windCard = windCard;
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

    public boolean isTurn() {
        return this.turn;
    }

    public void resetTurn() {
        this.turn = true;
    }

    public void endTurn() {
        this.turn = false;
    }

    public void setPrivObj(ObjectiveCard privObj) {
        this.privObj = privObj;
    }

    public ObjectiveCard getPrivObj() {
        return privObj;
    }

    public void setFavorPoint(int favorPoint){
        this.favorPoint = favorPoint;
    }

    public int getFavorPoint(){
        return favorPoint;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("ID: " + getId() /*+ " PrivObj: " + getPrivObj() + " WinCard: " + getWind()*/ + "FavorPoint: "
                + getFavorPoint());
    }


}