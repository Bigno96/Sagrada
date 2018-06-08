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

    private final Lobby lobby;
    private int count;
    private final GameSettingsParser settings;
    private final ViewMessageParser dictionary;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
        count = 0;
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    @Override
    public synchronized void run() {
        if (count < settings.getStartingTimer()/settings.getGameNotifyInterval()) {
            lobby.notifyAllPlayers( dictionary.getMessage(TIMER_ON_KEYWORD)+
                                    ((settings.getStartingTimer()-count*settings.getGameNotifyInterval())/1000) +
                                    dictionary.getMessage(SECONDS_COUNTDOWN_KEYWORD));
            count++;
        } else {
            this.cancel();
            lobby.startGame();
        }
    }
}

