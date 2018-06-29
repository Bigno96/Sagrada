package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class SetDiceObserver implements Observer {

    private Lobby lobby;

    public SetDiceObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        WindowCard card = (WindowCard) o;
        Cell dest = (Cell) arg;

        Optional<String> optionalUsername = lobby.getPlayers().values().stream()
                .filter(player -> player.getWindowCard().getId() == card.getId())
                .map(Player::getId)
                .findAny();

        optionalUsername.ifPresent(username ->
            lobby.getSpeakers().values().forEach(speaker -> speaker.successfulPlacementDice(username, dest, dest.getDice())));
    }
}
