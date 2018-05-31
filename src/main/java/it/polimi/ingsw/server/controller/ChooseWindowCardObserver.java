package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.util.*;

public class ChooseWindowCardObserver implements Observer {

    private Lobby lobby;
    private WindowCardController cardController;

    public ChooseWindowCardObserver(Lobby lobby){
        this.lobby = lobby;
        cardController = new WindowCardController();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object poolCards) {
        /*if (poolCards instanceof HashMap) {
            ((HashMap<Player, List<WindowCard>>) poolCards).forEach((key, value) -> {
                ClientSpeaker client = lobby.getSpeaker(key);
                try {
                    client.chooseWindowCard(value);
                } catch (FileNotFoundException | IDNotFoundException | PositionException | ValueException e) {
                    out.println(e.getMessage());
                }
            });
        }*/
    }

    /*public void notify(String userName, String name){
        (lobby.getPlayers()).forEach((key, value) -> {
           if (key.equals(userName)){
               value.setWindowCard(cardController.checkChoiceWindowCard(userName, name));
           }else{
               //notify other users
               ClientSpeaker client = lobby.getSpeaker(lobby.getPlayers().get(key));
               try {
                   client.showCardPlayer(key, cardController.checkChoiceWindowCard(userName, name));
               } catch (RemoteException | FileNotFoundException | IDNotFoundException | PositionException | ValueException e) {
                   e.printStackTrace();
               }
           }
        });
    }

    public WindowCardController getCardController() {
        return cardController;
    }*/
}
