package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class ChooseWindowCardObserver extends Observable {

    private Lobby lobby;

    public ChooseWindowCardObserver(Lobby lobby){
        this.lobby = lobby;
    }

    public void notifyClients(HashMap<Player, List<WindowCard>> poolCards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        for (Player p: poolCards.keySet()){
            ClientSpeaker client = lobby.getSpeaker(p);
            client.chooseWindowCard(poolCards.get(p));
        }
    }
}
