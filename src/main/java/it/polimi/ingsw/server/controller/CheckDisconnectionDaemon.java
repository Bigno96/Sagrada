package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class CheckDisconnectionDaemon extends TimerTask {

    private ClientSpeaker speaker;
    private Boolean disconnected;
    private Lobby lobby;
    private Timer disconnectionTimer;
    private String username;

    public CheckDisconnectionDaemon(ClientSpeaker speaker, Lobby lobby, String username) {
        this.username = username;
        this.speaker = speaker;
        this.disconnected = false;
        this.lobby = lobby;
        this.disconnectionTimer = new Timer();
    }

    @Override
    public synchronized void run() {
        if (!speaker.ping() && !disconnected) {
            out.println("Lost connection with " + username);
            try {
                lobby.removePlayer(username);
            } catch (EmptyException | PlayerNotFoundException e) {
                out.println(e.getMessage());
            }
            disconnected = true;
            lobby.setDisconnectedPlayer(username);
        }
        else if (speaker.ping() && disconnected) {
            disconnectionTimer.cancel();
            lobby.reconnectPlayer(username, speaker);
            disconnected = false;
        }
    }

}
