package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import java.util.HashMap;
import java.util.List;

public class WindowCardController {

    private HashMap<Player, List<WindowCard>> poolCards = new HashMap<>();
    private WindowCard card;

    public WindowCardController(){}

    public void setPoolCards(HashMap<Player, List<WindowCard>> poolCards) {
        this.poolCards = poolCards;
    }

    public WindowCard checkChoiceWindowCard(String username, String name){
        poolCards.forEach((key, value) -> {
            if (key.equals(username))
                for (int i=0; i<4; i++)
                    if (value.get(i).getName().equals(name))
                        card = value.get(i);
        });
        return card;
    }
}
