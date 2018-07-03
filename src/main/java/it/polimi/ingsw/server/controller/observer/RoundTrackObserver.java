package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.util.Observable;
import java.util.Observer;

/**
 * Notify at the end of round the change of round track, printing it on all view's
 */
public class RoundTrackObserver implements Observer {

    private Lobby lobby;

    public RoundTrackObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        lobby.getSpeakers().values().forEach(speaker -> speaker.showRoundTrack(lobby.getGame().getBoard().getRoundTrack()));
    }
}
