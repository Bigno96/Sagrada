package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.game.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Run every NOTIFY_INTERVAL second. Counts down the start of the game. After GAME_STARTING_TIMER, starts the game.
 */
public class StartGame extends TimerTask {

    private static final String TIMER_ON_KEYWORD = "TIMER_ON";
    private static final String SECONDS_COUNTDOWN_KEYWORD = "SECONDS_COUNTDOWN";
    private static final String GAME_WILL_START_KEYWORD = "GAME_WILL_START";

    private final String timerOn;
    private final String countdown;

    private final Lobby lobby;
    private int count;

    private final int gameTimer;
    private final int notifyInterval;

    private final ViewMessageParser dictionary;
    private final GameSettingsParser settings;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
        count = 0;

        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();

        this.timerOn = dictionary.getMessage(TIMER_ON_KEYWORD);
        this.countdown = dictionary.getMessage(SECONDS_COUNTDOWN_KEYWORD);
        this.gameTimer = settings.getStartingTimer();
        this.notifyInterval = settings.getGameNotifyInterval();
    }

    @Override
    public synchronized void run() {
        if (!checkStartGame()) {
            this.cancel();
            Timer starting = new Timer();
            starting.scheduleAtFixedRate(new CheckStartGameDaemon((HashMap<String, Player>) lobby.getPlayers(), lobby), 0, settings.getDaemonFrequency());
            lobby.getSpeakers().values().forEach(speaker -> speaker.tell(dictionary.getMessage(GAME_WILL_START_KEYWORD)));
        }
        else {
            if (count < gameTimer/notifyInterval) {
                lobby.notifyAllPlayers( timerOn + ((gameTimer-count*notifyInterval)/1000) + countdown);
                count++;

            } else {
                this.cancel();
                lobby.startGame();
            }
        }
    }

    /**
     * Check if game is ready to be create
     * @return true if at least 2 player are connected, false else
     */
    private synchronized boolean checkStartGame() {
        return lobby.getPlayers().values().stream()
                .filter(entry -> !entry.isDisconnected())
                .collect(Collectors.toList())
                .size() >= 2;
    }
}

