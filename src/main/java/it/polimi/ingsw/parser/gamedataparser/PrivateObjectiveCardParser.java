package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.Parser;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.factory.ObjectiveFactory;
import it.polimi.ingsw.server.model.objectivecard.factory.PrivateObjectiveFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PrivateObjectiveCardParser extends ObjectiveCardParser implements Parser {

    private final String infoPath;
    private final ObjectiveFactory factory;

    public PrivateObjectiveCardParser(String infoPath) {
        this.infoPath = infoPath;
        this.factory = new PrivateObjectiveFactory();
    }

    public ObjectiveCard makeObjectiveCard(int id) throws FileNotFoundException, IDNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));
        ObjectiveCard ret;

        String descr = super.findDescr(id, objArray);

        ret = factory.makeCard(id, descr, 0);

        String type = parseType(objArray, id);

        factory.setParameter(ret, type, null, null, null);

        return ret;
    }

    private String parseType(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("type").getAsString();
            }
        }

        return null;
    }
}