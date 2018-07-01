package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;
import java.util.SortedMap;

/**
 * Methods used to access view
 */
public interface ViewInterface {

    /**
     * Used to print on screen a message s
     * @param s to be printed
     */
    void print(String s);

    /**
     * Starts the graphic setting up connection parameters
     */
    void startGraphic();

    /**
     * Used to choose which window card the user want to choose from a presented list
     * @param cards cards.size() = 4
     */
    void chooseWindowCard(List<WindowCard> cards);


    /**
     * Used to show on screen a player's window card
     * @param user = Player.getId()
     * @param card = Player.getWindowCard().getName()
     */
    void showCardPlayer(String user, WindowCard card);

    /**
     * Used to print on screen a window card
     * @param window window card to be printed
     */
    void printWindowCard(WindowCard window);

    /**
     * Used to print owner's private objective
     * @param privateObj = Player.getPrivateObjective()
     */
    void printPrivateObj(ObjectiveCard privateObj);

    /**
     * Used to print public objective of this current game
     * @param publicObj publicObj.size() = 3
     */
    void printPublicObj(List<ObjectiveCard> publicObj);

    /**
     * Used to get that is turn of Player with passed username
     * @param username = game.getCurrentPlayer().getId()
     */
    void isTurn(String username);

    /**
     * Used to show on screen draft for this current round
     * @param draft = game.getBoard().getDraft()
     */
    void showDraft(List<Dice> draft);

    /**
     * Used to place dice
     * @param username of player moving the dice
     * @param dest cell where the dice is being moved
     * @param moved dice being moved
     */
    void successfulPlacementDice(String username, Cell dest, Dice moved);

    /**
     * Used when wrong placement is tried
     */
    void wrongPlacementDice(String errorMsg);

    /**
     * Used when game ends to print final ranking
     * @param ranking sorted map of player username and their points through the game
     */
    void printRanking(SortedMap<Integer, String> ranking);
}
