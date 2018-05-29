package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CheckDisconnectionDaemon extends TimerTask {

    private HashMap<String, ClientSpeaker> speakers;
    private HashMap<String, Timer> disconnectedPlayer;
    private Lobby lobby;

    CheckDisconnectionDaemon(HashMap<String, ClientSpeaker> speakers, HashMap<String, Timer> disconnectedPlayer, Lobby lobby) {
        this.speakers = speakers;
        this.disconnectedPlayer = disconnectedPlayer;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        for(Map.Entry<String,ClientSpeaker> entry : speakers.entrySet()) {
            if (!disconnectedPlayer.containsKey(entry.getKey()) && !entry.getValue().ping())
                lobby.disconnection(entry.getKey());
        }
        for(Map.Entry<String,ClientSpeaker> entry : speakers.entrySet()) {
            if (disconnectedPlayer.containsKey(entry.getKey()) && entry.getValue().ping()) {
                lobby.reconnectPlayer(entry.getKey(), entry.getValue());
            }
        }
    }
}
