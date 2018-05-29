package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.NotEmptyException;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Player;

import java.util.List;

public class MoveDiceController {

    public MoveDiceController(){}

    public boolean moveDiceFromDraftToCard(Player player, List<Integer> listCoordinates, Draft draft){
        // player: player who has set the dice
        // listCoordinates -> 0: index, 1: row, 2: col
        try {
            player.getWindowCard().getWindow().getCell(listCoordinates.get(1), listCoordinates.get(2)).setDice(draft.findDice(listCoordinates.get(0)));
        } catch (NotEmptyException | IDNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
