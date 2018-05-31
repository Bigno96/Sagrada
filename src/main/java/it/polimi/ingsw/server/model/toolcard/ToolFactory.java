package it.polimi.ingsw.server.model.toolcard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ToolFactory {

    private ToolStrategy strategy;
    private Game game;
    private final String infoPath = System.getProperty("user.dir") + "/src/main/resources/Json/ToolCard.json";


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
        RoundTrack roundTrack = null;
        Draft draft = null;
        DiceBag diceBag = null;
        JsonParser parser = new JsonParser();

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                name = obj.get("name").getAsString();
                color = Colors.parseColor(obj.get("color").getAsString());
                roundTrack = readRoundTrack(obj);
                draft = readDraft(obj);
                diceBag = readDiceBag(obj);
            }
        }

        ret = new ToolCard(id, name, color, strategy);
        ret.setActor(roundTrack, draft, diceBag);

        return ret;
    }

    private RoundTrack readRoundTrack(JsonObject obj) {
        if (obj.get("roundTrack").getAsString() != null)
            return game.getBoard().getRoundTrack();
        return null;
    }

    private Draft readDraft(JsonObject obj) {
        if (obj.get("draft").getAsString() != null)
            return game.getBoard().getDraft();
        return null;
    }

    private DiceBag readDiceBag(JsonObject obj) {
        if (obj.get("roundTrack").getAsString() != null)
            return game.getBoard().getDiceBag();
        return null;
    }
}
