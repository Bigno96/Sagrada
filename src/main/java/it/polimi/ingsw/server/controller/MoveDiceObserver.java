package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MoveDiceObserver implements Observer {

    private Lobby lobby;
    private MoveDiceController diceController;

    public MoveDiceObserver(Lobby lobby){
        this.lobby = lobby;
        diceController = new MoveDiceController();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable player, Object listCoordinates) {
        // player: player who has set the dice
        // listCoordinates -> 0: index, 1: row, 2: col
        if (diceController.moveDiceFromDraftToCard((Player) player, (List<Integer>) listCoordinates, lobby.getGame().getBoard().getDraft())){
            (lobby.getPlayers()).forEach((key, value) -> {
                if (!key.equals(((Player) player).getId())) {
                    //notify other users
                    ClientSpeaker client = lobby.getSpeaker(lobby.getPlayers().get(key));
                    try {
                        client.placementDice(key, ((Player) player).getWindowCard().getWindow().getCell(((List<Integer>) listCoordinates).get(1), ((List<Integer>) listCoordinates).get(2)), lobby.getGame().getBoard().getDraft().findDice(((List<Integer>) listCoordinates).get(0)));
                    } catch (RemoteException | IDNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
