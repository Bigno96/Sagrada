package it.polimi.ingsw.server.controller.observer;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Player;

import java.util.Observable;
import java.util.Observer;

public class SetObjectiveObserver implements Observer {

    private Lobby lobby;

    public SetObjectiveObserver(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void update(Observable o, Object arg) {
        Player p;

        if (arg.equals("PublicObjective"))
            lobby.getSpeakers().forEach((user, speaker) ->
                speaker.printPublicObj(lobby.getGame().getBoard().getPublObj()));

        else if (arg.equals("PrivateObjective")) {
            p = (Player) o;
            lobby.getSpeakers().get(p.getId()).printPrivateObj(p.getPrivObj());
        }
    }
}
