package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.parser.GameSettingsParser;
import it.polimi.ingsw.parser.ParserFactory;

import java.util.*;

/**
 * Run every NOTIFY_INTERVAL second. Counts down the start of the game. After GAME_STARTING_TIMER, starts the game.
 */
public class StartGame extends TimerTask {

    private final Lobby lobby;
    private int count;
    private final GameSettingsParser settings;

    StartGame(Lobby lobby) {
        this.lobby = lobby;
        count = 0;
        this.settings = (GameSettingsParser) ParserFactory.getGameSettingsParser();
    }

    @Override
    public synchronized void run() {
        if (count < settings.getStartingTimer()/settings.getGameNotifyInterval()) {
            lobby.notifyAllPlayers("Timer is on: " +
                                    ((settings.getStartingTimer()-count*settings.getGameNotifyInterval())/1000) +
                                    " seconds before game starts");
            count++;
        } else {
            this.cancel();
            lobby.startGame();
        }
    }
}

