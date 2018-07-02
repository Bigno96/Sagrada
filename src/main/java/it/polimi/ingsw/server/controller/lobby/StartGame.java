package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.*;

/**
 * Run every NOTIFY_INTERVAL second. Counts down the start of the game. After GAME_STARTING_TIMER, starts the game.
 */
public class StartGame extends TimerTask {

    private static final String TIMER_ON_KEYWORD = "TIMER_ON";
    private static final String SECONDS_COUNTDOWN_KEYWORD = "SECONDS_COUNTDOWN";

    private final String timerOn;
    private final String countdown;

    private final Lobby lobby;
    private int count;

    private final int gameTimer;
    private final int notifyInterval;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
        count = 0;
        GameSettingsParser settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        ViewMessageParser dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        this.timerOn = dictionary.getMessage(TIMER_ON_KEYWORD);
        this.countdown = dictionary.getMessage(SECONDS_COUNTDOWN_KEYWORD);
        this.gameTimer = settings.getStartingTimer();
        this.notifyInterval = settings.getGameNotifyInterval();
    }

    @Override
    public synchronized void run() {
        if (count < gameTimer/notifyInterval) {
            lobby.notifyAllPlayers( timerOn + ((gameTimer-count*notifyInterval)/1000) + countdown);
            count++;

        } else {
            this.cancel();
            lobby.startGame();
        }
    }
}

