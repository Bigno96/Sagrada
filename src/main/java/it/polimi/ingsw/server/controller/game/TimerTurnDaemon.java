package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.util.TimerTask;

/**
 * Wait ACTION_TIMER seconds for current player to make a move. If timer expires, turn is passed. If one action
 * between placement dice or use of tool card is made, timer is reset. Every player is notified of how much time
 * current player has to make a move.
 */
public class TimerTurnDaemon extends TimerTask {

    private static final String USER = "Il giocatore ";
    private static final String HAS_YET = " ha ancora ";
    private static final String TIME_TO_END_TURN_KEYWORD = "TIME_TO_END_TURN";
    private static final String TIMER_TURN_EXCEEDED_KEYWORD = "TIMER_TURN_EXCEEDED";

    private final String toEndTurn;
    private final String timerTurnExceeded;

    private Lobby lobby;

    private int count;
    private final int actionTimer;
    private final int notifyInterval;

    public TimerTurnDaemon(Lobby lobby) {
        this.lobby = lobby;
        this.count = 0;
        ViewMessageParser dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        GameSettingsParser settings = (GameSettingsParser) ParserManager.getGameSettingsParser();

        this.toEndTurn = dictionary.getMessage(TIME_TO_END_TURN_KEYWORD);
        this.timerTurnExceeded = dictionary.getMessage(TIMER_TURN_EXCEEDED_KEYWORD);
        this.actionTimer = settings.getActionTimer();
        this.notifyInterval = settings.getNotifyInterval();
    }

    @Override
    public void run() {
        if (count < actionTimer/notifyInterval) {

            lobby.notifyAllPlayers(USER + lobby.getGame().getCurrentPlayer().getId() + HAS_YET
                                + ((actionTimer-count*notifyInterval)/1000) + toEndTurn);
            count++;
        }
        else {
            lobby.notifyAllPlayers(USER + lobby.getGame().getCurrentPlayer().getId() + timerTurnExceeded);
            lobby.getRoundController().nextTurn();
        }
    }

    /**
     * Used to restart counter, setting count = 0
     */
    public void resetCount() {
        this.count = 0;
    }
}
