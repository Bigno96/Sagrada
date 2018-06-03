package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.parser.Parser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolEffectRealization;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ToolCardParser implements Parser {

    private final String infoPath;

    public ToolCardParser(String infoPath) {
        this.infoPath = infoPath;
    }

    /**
     * Make the tool card with the passed id
     * @param id > 0 && id < 13
     * @return Tool Card
     * @throws FileNotFoundException when File Reader doesn't find the info file
     */
    public ToolCard makeToolCard(int id, Game game) throws FileNotFoundException {
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
                roundTrack = readRoundTrack(obj, game);
                draft = readDraft(obj, game);
                diceBag = readDiceBag(obj, game);
            }
        }

        ToolEffectRealization strategy =
                new ToolEffectRealization(game.getBoard().getRoundTrack(), game.getBoard().getDraft(), game.getBoard().getDiceBag());
        ret = new ToolCard(id, name, color, strategy);
        ret.setActor(roundTrack, draft, diceBag);

        return ret;
    }

    private RoundTrack readRoundTrack(JsonObject obj, Game game) {
        if (obj.get("roundTrack").getAsString() != null)
            return game.getBoard().getRoundTrack();
        return null;
    }

    private Draft readDraft(JsonObject obj, Game game) {
        if (obj.get("draft").getAsString() != null)
            return game.getBoard().getDraft();
        return null;
    }

    private DiceBag readDiceBag(JsonObject obj, Game game) {
        if (obj.get("roundTrack").getAsString() != null)
            return game.getBoard().getDiceBag();
        return null;
    }
}
