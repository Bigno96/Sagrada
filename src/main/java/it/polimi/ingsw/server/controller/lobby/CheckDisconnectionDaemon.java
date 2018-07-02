package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;

import java.util.TimerTask;

import static java.lang.System.*;

/**
 * Daemon run to check if player is disconnected. There is one to one association between player and his daemon.
 */
public class CheckDisconnectionDaemon extends TimerTask {

    private static final String LOST_CONNECTION_KEYWORD = "LOST_CONNECTION";

    private final String lostConnection;

    private Boolean disconnected;
    private final Lobby lobby;
    private final String username;

    private final ClientSpeaker speaker;

    CheckDisconnectionDaemon(ClientSpeaker speaker, Lobby lobby, String username) {
        this.username = username;
        this.speaker = speaker;
        this.disconnected = false;
        this.lobby = lobby;

        CommunicationParser protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.lostConnection = protocol.getMessage(LOST_CONNECTION_KEYWORD);
    }

    @Override
    public synchronized void run() {
        Boolean pinged = speaker.ping();                // try to ping speaker
        if (!pinged && !disconnected) {                 // failed to ping and user is not yet disconnected
            out.println(lostConnection + username);
            disconnected = true;
            lobby.disconnectPlayer(username);
        }
        else if (pinged && disconnected) {              // successful ping and player was disconnected
            disconnected = false;
            lobby.reconnectPlayer(username, speaker);
            lobby.purgeRemoving(username);
        }
    }

}
