package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.*;

public class ChooseWindowCardObserver implements Observer {

    private Lobby lobby;

    public ChooseWindowCardObserver(Lobby lobby){
        this.lobby = lobby;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object poolCards) {
        if (poolCards instanceof HashMap) {
            ((HashMap<Player, List<WindowCard>>) poolCards).forEach((key, value) -> {
                ClientSpeaker client = lobby.getSpeaker(key);
                try {
                    client.chooseWindowCard(value);
                } catch (FileNotFoundException | IDNotFoundException | PositionException | ValueException e) {
                    out.println(e.getMessage());
                }
            });
        }
    }

    public void notify(String userName, String name){
        WindowFactory windFact = new WindowFactory();
        (lobby.getPlayers()).forEach((key, value) -> {
           if (key.equals(userName)){
               try {
                   value.setWindowCard(windFact.getWindow(name));
               } catch (FileNotFoundException | IDNotFoundException | ValueException | PositionException e) {
                   e.printStackTrace();
               }
           }else{
               //notify other users
           }
        });
    }
}
