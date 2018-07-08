package it.polimi.ingsw.parser.messageparser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.parser.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.out;

/**
 * Parse Game Settings
 */
public class GameSettingsParser implements Parser {

    private static GameSettingsParser ourInstance = null;

    private JsonObject obj;

    private static final String GAME_NOTIFY = "GAME_COUNTDOWN_NOTIFY_INTERVAL";
    private static final String STARTING_TIMER = "GAME_IS_STARTING_TIMER";
    private static final String DAEMON_FREQUENCY = "DAEMON_FREQUENCY";
    private static final String MAX_PLAYER = "MAX_PLAYER_IN_GAME";
    private static final String TIME_UNTIL_RANDOM_CARD = "TIME_UNTIL_RANDOM_CARD";
    private static final String MAX_COL = "MAX_WINDOW_CARD_COLUMN";
    private static final String MAX_ROW = "MAX_WINDOW_CARD_ROW";
    private static final String NOTIFY_INTERVAL = "NOTIFY_INTERVAL";
    private static final String ACTION_TIMER = "ACTION_TIMER";
    private static final String NUMBER_WINDOW_CARD = "NUMBER_WINDOW_CARD";

    private GameSettingsParser(String path) {
        JsonParser parser = new JsonParser();
        try {
            obj = (JsonObject) parser.parse(new FileReader(path));
        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
        }
    }

    public static GameSettingsParser getInstance(String infoPath) {
        if (ourInstance == null)
            ourInstance = new GameSettingsParser(infoPath);

        return ourInstance;
    }

    public int getGameNotifyInterval() {
        return Integer.parseInt(obj.get(GAME_NOTIFY).getAsString());
    }

    public int getStartingTimer() {
        return Integer.parseInt(obj.get(STARTING_TIMER).getAsString());
    }

    public int getDaemonFrequency() {
        return Integer.parseInt(obj.get(DAEMON_FREQUENCY).getAsString());
    }

    public int getMaxPlayer() {
        return Integer.parseInt(obj.get(MAX_PLAYER).getAsString());
    }

    public int getWindowCardMaxColumn() {
        return Integer.parseInt(obj.get(MAX_COL).getAsString());
    }

    public int getWindowCardMaxRow() {
        return Integer.parseInt(obj.get(MAX_ROW).getAsString());
    }

    public int getTimeUntilRandomCard() {
        return Integer.parseInt(obj.get(TIME_UNTIL_RANDOM_CARD).getAsString());
    }

    public int getNotifyInterval() {
        return Integer.parseInt(obj.get(NOTIFY_INTERVAL).getAsString());
    }

    public int getActionTimer() {
        return Integer.parseInt(obj.get(ACTION_TIMER).getAsString());
    }

    public int getNumberWindowCard() {
        return Integer.parseInt(obj.get(NUMBER_WINDOW_CARD).getAsString());
    }

}
