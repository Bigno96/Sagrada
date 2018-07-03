package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.util.Observable;
import java.util.Observer;

/**
 * Notify players about which tool cards are chosen for the game
 */
public class SetToolCardObserver implements Observer {

    private static final String TOOL_CARD_OBSERVER_MSG = "ToolCard";

    private Lobby lobby;

    public SetToolCardObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg.equals(TOOL_CARD_OBSERVER_MSG))      // tell tool card to everyone
            lobby.getSpeakers().forEach((user, speaker) ->
                    speaker.printListToolCard(lobby.getGame().getBoard().getToolCard()));
    }
}
