package it.polimi.model;

import java.util.Timer;
import java.util.TimerTask;


public class Room {

    public enum status {BOOT, START};
    private int id;
    private status status;
    private int nPlayer;
    private int delay = 60000; //60 sec

    private Timer timer = new Timer();

    private TimerTask startGame = new TimerTask() {
        @Override
        public void run() {
            //GameController.StartGame();
        }
    };

    public Room(int id, status status, int nPlayer) {
        this.id = id;
        this.status = status;
        this.nPlayer = nPlayer;
    }

    /**  public void CheckNPlayer(){
        if(this.getNPlayer()==2)
            this.StartTimer();
        if(this.getNPlayer()==4)
            GameController.StartGame();
    }*/

    public void StartTimer(){
        timer.schedule(startGame, delay);
    }

    public int getId() {
        return id;
    }

    public int getNPlayer() {
        return nPlayer;
    }

    public status getStatus() {
        return status;
    }

    public int getDelay(){
        return delay;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getId() + " Status: " + getStatus() + " nPlayer: " + getNPlayer();
    }

    public String dump()
    {
        return "ID: " + getId() + " Status: " + getStatus() + " nPlayer: " + getNPlayer();
    }

}
