package it.polimi.model;

public class Player {

    private int id;
    //private PrivateObjective PrivObj;
    //private WindowCard WindCard;
    private int FavorPoint;

    public Player(int id) {
        this.id = id;
        this.FavorPoint = 0;
        //this.PrivObj = null;
        //this.WinCard = null;
    }

    public int getID() {
        return id;
    }

    /*public void setPrivObj(PrivateObjective PrivObj) {
        this.PrivObj = PrivObj;
    }*/

    /*public PrivateObjective getPrivObj() {
        return PrivObj;
    }*/

    /*public void setWind(WindowCard WindCard) {
        this.WindCard = WindCard;
    }*/

    /*public WindowCard getWind() {
        return WindCard;
    }*/

    public void setFavorPoint(int FavorInt){
        this.FavorPoint = FavorInt;
    }

    public int getFavorPoint(){
        return FavorPoint;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getID() /*+ " PrivObj: " + getPrivObj() + " WinCard: " + getWind()*/
                + "FavorPoint: " + getFavorPoint();
    }

    public String dump()
    {
        return "ID: " + getID() /*+ " PrivObj: " + getPrivObj() + " WinCard: " + getWind()*/ + "FavorPoint: "
                + getFavorPoint();
    }


}