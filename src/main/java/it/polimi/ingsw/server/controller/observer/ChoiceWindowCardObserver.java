package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class ChoiceWindowCardObserver implements Observer {

    private Lobby lobby;

    public ChoiceWindowCardObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!arg.equals("nextTurn")) {
            WindowCard card = lobby.getPlayers().get(arg.toString()).getWindowCard();

            Consumer<Map.Entry<String, ClientSpeaker>> notifyCard = entry ->
                    entry.getValue().showCardPlayer(arg.toString(), card);

            lobby.getSpeakers().entrySet().parallelStream().forEach(notifyCard);
        }
    }
}
