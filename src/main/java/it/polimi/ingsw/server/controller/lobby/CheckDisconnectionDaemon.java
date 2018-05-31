package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class CheckDisconnectionDaemon extends TimerTask {

    private ClientSpeaker speaker;
    private Boolean disconnected;
    private Lobby lobby;
    private String username;

    public CheckDisconnectionDaemon(ClientSpeaker speaker, Lobby lobby, String username) {
        this.username = username;
        this.speaker = speaker;
        this.disconnected = false;
        this.lobby = lobby;
    }

    @Override
    public synchronized void run() {
        Boolean pinged = speaker.ping();
        if (!pinged && !disconnected) {
            out.println("Lost connection with " + username);
            disconnected = true;
            lobby.disconnectPlayer(username);
        }
        else if (pinged && disconnected) {
            disconnected = false;
            lobby.reconnectPlayer(username);
            lobby.purgeRemoving(username);
        }
    }

}
