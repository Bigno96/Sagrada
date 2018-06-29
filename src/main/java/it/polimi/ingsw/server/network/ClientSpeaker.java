package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;

/**
 * Interface to hide network difference in comm Server -> Client
 */
public interface ClientSpeaker {

    /**
     * Used to print on Client
     * @param s to be printed
     */
    void tell(String s);

    /**
     * Check if the client is connected
     * @return true if pong is true in return, else false
     */
    boolean ping();

    /**
     * Used to notify successful login
     * @param s message of success for login
     */
    void loginSuccess(String s);

    /**
     * Used to send Player list of window card selected for him
     * @param cards cards.size() = 4
     */
    void sendWindowCard(List<WindowCard> cards);

    /**
     * Used to send Player the window card of player passed as parameter
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     */
    void showCardPlayer(String user, WindowCard card);

    /**
     * Usec to notify whose players next turn is
     * @param user = game.getCurrentPlayer().getId()
     */
    void nextTurn(String user);

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
    void wrongPlacementDice();

    /**
     * Used to print a window card on Player's view
     * @param card to be printed
     */
    void printWindowCard(WindowCard card);

    /**
     * Used to show Draft on Player's view
     * @param draft of the current round
     */
    void showDraft(Draft draft);

    /**
     * Used to show all Public Objectives selected for the current game on Player's view
     * @param publicObj publicObj.size() = 3
     */
    void printPublicObj(List<ObjectiveCard> publicObj);

    /**
     * Used to show Private Objective to owner's view
     * @param privateObj = Player.getPrivateObjective()
     */
    void printPrivateObj(ObjectiveCard privateObj);
}
