package it.polimi.ingsw.parser.messageparser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.parser.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

/**
 * Parse Game Settings
 */
public class GameSettingsParser implements Parser {

    private JsonObject obj;

    private static final String GAME_NOTIFY = "GAME_COUNTDOWN_NOTIFY_INTERVAL";
    private static final String STARTING_TIMER = "GAME_IS_STARTING_TIMER";
    private static final String DAEMON_FREQUENCY = "DAEMON_FREQUENCY";
    private static final String MAX_PLAYER = "MAX_PLAYER_IN_GAME";
    private static final String REMOVING_TIMER = "REMOVING_PLAYER_TIMER";

    public GameSettingsParser(String path) {
        JsonParser parser = new JsonParser();
        try {
            obj = (JsonObject) parser.parse(new FileReader(path));
        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
        }
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

    public int getRemovingTimer() {
        return Integer.parseInt(obj.get(REMOVING_TIMER).getAsString());
    }
}