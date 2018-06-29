package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

/**
 * Controls actions of player
 */
public class ActionController {

    private Lobby lobby;
    private Game game;
    private Cell dest;

    private final GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();

    public ActionController(Lobby lobby, Game game) {
        this.lobby = lobby;
        this.game = game;
    }

    /**
     * @param username of player moving the dice
     * @param index in the draft of the dice
     * @param row of the destination cell
     * @param col of the destination cell
     * @throws NotTurnException when it's not the turn of the player who requested the action
     * @throws WrongDiceSelectionException when the dice selected is wrong
     * @throws WrongCellSelectionException when the cell selected does not exists
     * @throws NotEmptyException when the cell selected for the movement is already occupied
     * @throws EmptyException when the draft is empty
     * @throws PositionException when parameters of cell are wrong
     * @throws IDNotFoundException when parameters of dice are wrong
     * @throws WrongPositionException when the move is not legal
     */
    public void placeDice(String username, int index, int row, int col) throws NotTurnException, WrongDiceSelectionException,
            WrongCellSelectionException, NotEmptyException, PositionException, IDNotFoundException, WrongPositionException, EmptyException {

        if (!game.getCurrentPlayer().getId().equals(username))
            throw new NotTurnException();

        if (index < 0 || index >= game.getBoard().getDraft().getDraftList().size())
            throw new WrongDiceSelectionException();

        if (row < 0 || row >= gameSettings.getWindowCardMaxRow() || col < 0 || col >= gameSettings.getWindowCardMaxColumn())
            throw new WrongCellSelectionException();

        Dice dice = game.getBoard().getDraft().getDraftList().get(index);
        WindowCard card = game.getCurrentPlayer().getWindowCard();
        dest = card.getWindow().getCell(row, col);

        dest.setDice(dice);

        Boolean correct;

        try {
            if (card.numEmptyCells() == ((gameSettings.getWindowCardMaxColumn() * gameSettings.getWindowCardMaxRow())-1))
                correct = card.checkFirstDice();
            else
                correct = card.checkPlaceCond();

        } catch (WrongPositionException | EmptyException | PositionException e) {
            rollback();
            throw e;
        }

        if (correct) {
            game.getBoard().getDraft().rmDice(dice);
            lobby.getSpeakers().get(username).successfulPlacementDice(username, dest, dice);
        }
        else
            rollback();
    }

    /**
     * Rolling back the incorrect move
     */
    private void rollback() {
        dest.freeCell();
    }

}
