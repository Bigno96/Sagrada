package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.util.Observable;
import java.util.Observer;

public class DraftObserver implements Observer {

    private Lobby lobby;

    public DraftObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        lobby.getSpeakers().values().forEach(speaker -> speaker.showDraft(lobby.getGame().getBoard().getDraft()));
    }
}
