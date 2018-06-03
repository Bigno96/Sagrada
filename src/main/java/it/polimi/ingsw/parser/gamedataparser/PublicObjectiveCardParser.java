package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.Parser;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.factory.ObjectiveFactory;
import it.polimi.ingsw.server.model.objectivecard.factory.PublicObjectiveFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PublicObjectiveCardParser extends ObjectiveCardParser implements Parser {

    private final String infoPath;
    private final ObjectiveFactory factory;

    public PublicObjectiveCardParser(String infoPath) {
        this.infoPath = infoPath;
        this.factory = new PublicObjectiveFactory();
    }

    public ObjectiveCard makeObjectiveCard(int id) throws FileNotFoundException, IDNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));
        ObjectiveCard ret;

        String descr = super.findDescr(id, objArray);
        int point = super.findPoint(id, objArray);

        ret = factory.makeCard(id, descr, point);

        String type = parseType(objArray, id);
        String scope = parseScope(objArray, id);
        String grad = parseGrad(objArray, id);
        String dir = parseDir(objArray, id);

        factory.setParameter(ret, type, scope, grad, dir);

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

    private String parseScope(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("scope").getAsString();
            }
        }

        return null;
    }

    private String parseGrad(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("grad").getAsString();
            }
        }

        return null;
    }

    private String parseDir(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("dir").getAsString();
            }
        }

        return null;
    }
}
