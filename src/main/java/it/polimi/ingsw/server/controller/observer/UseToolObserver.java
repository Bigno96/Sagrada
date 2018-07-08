package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.toolcard.ToolCard;

import java.util.Observable;
import java.util.Observer;

/**
 * Used to inform everyone that a player used successfully a tool card
 */
public class UseToolObserver implements Observer {

    private Lobby lobby;

    public UseToolObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {

        ToolCard observed = (ToolCard) o;
        String user = (String) arg;

        lobby.getSpeakers().values().forEach(speaker ->
            speaker.successfulUsedTool(user, observed));
    }
}
