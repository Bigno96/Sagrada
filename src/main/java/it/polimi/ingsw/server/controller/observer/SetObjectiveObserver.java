package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Player;

import java.util.Observable;
import java.util.Observer;

/**
 * Notify players about which objective cards are chosen for the game
 */
public class SetObjectiveObserver implements Observer {

    private static final String PUBLIC_OBJECTIVE_OBSERVER_MSG = "PublicObjective";
    private static final String PRIVATE_OBJECTIVE_OBSERVER_MSG = "PrivateObjective";

    private Lobby lobby;

    public SetObjectiveObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        Player p;

        if (arg.equals(PUBLIC_OBJECTIVE_OBSERVER_MSG))      // tell public obj to everyone
            lobby.getSpeakers().forEach((user, speaker) ->
                    speaker.printListPublicObj(lobby.getGame().getBoard().getPublicObj()));

        else if (arg.equals(PRIVATE_OBJECTIVE_OBSERVER_MSG)) {      // tell private obj only to the owner
            p = (Player) o;
            lobby.getSpeakers().get(p.getId()).printPrivateObj(p.getPrivateObj());
        }
    }
}
