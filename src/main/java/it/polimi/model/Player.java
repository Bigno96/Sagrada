package it.polimi.model;

import java.util.logging.Logger;

public class Player {

    private int id;
    //private PrivateObjective privObj;
    private WindowCard windCard;
    private int favorPoint;
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Player(int id) {
        this.id = id;
        this.favorPoint = 0;
        //this.privObj = null;
        this.windCard = null;
    }

    public int getId() {
        return id;
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