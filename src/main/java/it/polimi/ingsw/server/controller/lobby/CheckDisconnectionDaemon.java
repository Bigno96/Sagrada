package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.parser.ParserFactory;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.parser.CommunicationParser;

import java.util.TimerTask;

import static java.lang.System.*;

/**
 * Daemon run to check if player is disconnected. There is one to one association between player and his daemon.
 */
public class CheckDisconnectionDaemon extends TimerTask {

    private final ClientSpeaker speaker;
    private Boolean disconnected;
    private final Lobby lobby;
    private final String username;
    private final CommunicationParser protocol;

    CheckDisconnectionDaemon(ClientSpeaker speaker, Lobby lobby, String username) {
        this.username = username;
        this.speaker = speaker;
        this.disconnected = false;
        this.lobby = lobby;
        this.protocol = (CommunicationParser) ParserFactory.getCommunicationParser();
    }

    @Override
    public synchronized void run() {
        Boolean pinged = speaker.ping();                // try to ping speaker
        if (!pinged && !disconnected) {                 // failed to ping and user is not yet disconnected
            out.println(protocol.getMessage("LOST_CONNECTION") + username);
            disconnected = true;
            lobby.disconnectPlayer(username);
        }
        else if (pinged && disconnected) {              // successful ping and player was disconnected
            disconnected = false;
            lobby.reconnectPlayer(username);
            lobby.purgeRemoving(username);
        }
    }

}
