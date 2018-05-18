package it.polimi.ingsw.server.model.toolcard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.game.Game;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ToolFactory {

    private ToolStrategy strategy;
    private Game game;
    private final String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/ToolCard.json";

    /**
     * Constructor
     * @param strategy != null
     * @param game already started
     */
    public ToolFactory(ToolStrategy strategy, Game game) {
        this.game = game;
        this.strategy = strategy;
    }

    /**
     * Make the tool card with the passed id
     * @param id > 0 && id < 13
     * @return Tool Card
     * @throws FileNotFoundException when File Reader doesn't find the info file
     */
    public ToolCard makeToolCard(int id) throws FileNotFoundException {
        ToolCard ret;
        String name = null;
        Colors color = null;
        JsonParser parser = new JsonParser();

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                name = obj.get("name").getAsString();
                color = Colors.parseColor(obj.get("color").getAsString());
            }
        }

        ret = new ToolCard(id, name, color, strategy);
        ret.setActor(game.getBoard().getRoundTrack(), game.getBoard().getDraft(), game.getBoard().getDiceBag());

        return ret;
    }
}
