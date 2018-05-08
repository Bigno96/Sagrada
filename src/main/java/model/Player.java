package model;

import java.util.logging.Logger;

public class Player {

    private int id;
    private boolean firstTurn;
    private boolean secondTurn;
    private PrivateObjective privObj;
    private Board board;
    private WindowCard windCard;
    private int favorPoint;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Player(int id, Board board) {
        this.id = id;
        this.favorPoint = 0;
        this.firstTurn = true;
        this.secondTurn = true;
        this.privObj = null;
        this.windCard = null;
        this.board = board;
    }

    public Player(Player p) {
        this.id = p.getId();
        this.favorPoint = p.getFavorPoint();
        this.firstTurn = true;
        this.secondTurn = true;
        this.privObj = null;
        this.windCard = null;
        this.board = p.getBoard();
    }

    public void addWindowCard(WindowCard wind){
        this.windCard= wind;
    }

    public int getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public void endFirstTurn() {

        this.firstTurn = false;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void resetFirstTurn() {
        firstTurn = true;
    }

    public void endSecondTurn() {
        this.secondTurn = false;
    }

    public boolean isSecondTurn() {
        return secondTurn;
    }

    public void resetSecondTurn() {
        secondTurn=true;
    }

    /*public void setPrivObj(PrivateObjective privObj) {
        this.privObj = privObj;
    }*/

    /*public PrivateObjective getPrivObj() {
        return privObj;
    }*/

    public void setWind(WindowCard windCard) {
        this.windCard = windCard;
    }

    public WindowCard getWind() {
        return windCard;
    }

    public void setFavorPoint(int favorInt){
        this.favorPoint = favorInt;
    }

    public int getFavorPoint(){
        return favorPoint;
    }

    public Player getCopy() {
        return new Player(this);
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