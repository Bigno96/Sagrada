package it.polimi.model;

import it.polimi.controller.GameController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * timer
 * game
 * player
 */

public class Room {

    private int nPlayer = 0;
    private int delay; //60 sec
    private int id;
    private Timer timer = new Timer();

    private TimerTask startGame = new TimerTask() {
        @Override
        public void run() {
            GameController.StartGame();
        }
    };

    public Room(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    public void StartTimer(){
        timer.schedule(startGame, delay);
    }

    public int getId() {
        return id;
    }

    public int getDelay() {
        return delay;
    }

    public int getNPlayer() {
        return nPlayer;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + "ID: " + getId() + " Delay: " + getDelay() + " nPlayer: " + getNPlayer();
    }

    public String dump()
    {
        return "ID: " + getId() + " Delay: " + getDelay() + " nPlayer: " + getNPlayer();
    }

}
