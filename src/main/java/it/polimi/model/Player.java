package it.polimi.model;

import it.polimi.controller.PlayerController;

public class Player {

    private int id;
    private PrivateObjective PrivObj;
    private WindowCard WinCard;
    private int FavorPoint;

    public Player(int id) {
        this.id = id;
        this.FavorPoint = 0;
        this.PrivObj = null;
        this.WinCard = null;
    }

    public getID() {
        return id;
    }

    public setPrivObj(PrivateObjective PrivObj) {
        this.PrivObj = PrivObj;
    }

    public getPrivObj() {
        return PrivObj;
    }

    public setWind(WindowCard WindCard) {
        this.WindCard = WindCard;
    }

    public getWind() {
        return WindCard;
    }

    public setFavorPoint(int FavorInt){
        this.FavorPoint = FavorPoint;
    }

    public getFavorPoint(){
        return Favorpoint;
    }

    //Move

    //EndTurn

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getID() + " PrivObj: " + getPrivObj() + " WinCard: " + getWind()
                + "FavorPoint" + get FavorPoint();
    }

    public String dump()
    {

        return "ID: " + getID() + " PrivObj: " + getPrivObj() + " WinCard: " + getWind() + "FavorPoint"
                + get FavorPoint();
    }


}