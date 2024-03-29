package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
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
    void printListPublicObj(List<ObjectiveCard> publicObj);

    /**
     * Used to print tool cards of this current game
     * @param toolCards toolCards.size() = 3
     */
    void printListToolCard(List<ToolCard> toolCards);

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
     * Used to show on screen Round Track at the end of round
     * @param roundTrack = game.getBoard().getRoundTrack()
     */
    void showRoundTrack(RoundTrack roundTrack);

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
     * Print number of favor point of user that requested
     * @param point number of favor point
     */
    void printFavorPoints(int point);

    /**
     * Used when game ends to print final ranking
     * @param ranking sorted map of player username and their points through the game
     */
    void printRanking(SortedMap<Integer, String> ranking);

    /**
     * Used to show on screen that Player username has successfully used Tool Card card
     * @param username of who used the tool
     * @param card tool card used
     */
    void successfulUsedTool(String username, ToolCard card);
}
