package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * Notify all whose player turn is
 */
public class TurnObserver implements Observer {

   private Lobby lobby;

    public TurnObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        Consumer<Map.Entry<String, ClientSpeaker>> notifyPlayerTurn = entry ->
            entry.getValue().nextTurn(lobby.getGame().getCurrentPlayer().getId());

        if (arg.equals("nextTurn")) {
            lobby.getSpeakers().entrySet().forEach(notifyPlayerTurn);
            if (lobby.getGame().getCurrentPlayer().isDisconnected())
                lobby.getRoundController().nextTurn();
        }
    }
}
