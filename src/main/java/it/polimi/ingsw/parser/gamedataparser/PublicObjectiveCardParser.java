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

/**
 * Parse information of PublicObjectiveCard
 */
public class PublicObjectiveCardParser extends ObjectiveCardParser implements Parser {

    private static PublicObjectiveCardParser ourInstance = null;

    private static final String ID = "ID";
    private static final String TYPE = "TYPE";
    private static final String SCOPE = "SCOPE";
    private static final String GRADATION = "GRADATION";
    private static final String DIRECTION = "DIRECTION";

    private final String infoPath;
    private final ObjectiveFactory factory;

    private PublicObjectiveCardParser(String infoPath) {
        this.infoPath = infoPath;
        this.factory = new PublicObjectiveFactory();
    }

    public static PublicObjectiveCardParser getInstance(String infoPath) {
        if (ourInstance == null)
            ourInstance = new PublicObjectiveCardParser(infoPath);

        return ourInstance;
    }

    /**
     * Build the Objective Card corresponding to passed id.
     * @param id != null
     * @return Objective Card
     * @throws FileNotFoundException when JsonParser is linked to a wrong file
     * @throws IDNotFoundException when id is not found over the json
     */
    public ObjectiveCard makeObjectiveCard(int id) throws FileNotFoundException, IDNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));
        ObjectiveCard ret;

        String descr = super.findDescr(id, objArray);
        int point = super.findPoint(id, objArray);

        ret = factory.makeCard(id, descr, point);

        String type = parseType(objArray, id);
        String scope = parseScope(objArray, id);
        String grad = parseGradation(objArray, id);
        String dir = parseDirection(objArray, id);

        factory.setParameter(ret, type, scope, grad, dir);

        return ret;
    }

    /**
     * Find TYPE property over the passed JsonArray using id
     * @param objArray != null
     * @param id != null, jsonArray.contains(id)
     * @return TYPE for specific id card, null if not found
     */
    private String parseType(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id) {
                return obj.get(TYPE).getAsString();
            }
        }

        return "null";
    }

    /**
     * Find SCOPE property over the passed JsonArray using id
     * @param objArray != null
     * @param id != null, jsonArray.contains(id)
     * @return SCOPE for specific id card, null if not found
     */
    private String parseScope(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id) {
                return obj.get(SCOPE).getAsString();
            }
        }

        return "null";
    }

    /**
     * Find GRADATION property over the passed JsonArray using id
     * @param objArray != null
     * @param id != null, jsonArray.contains(id)
     * @return GRADATION for specific id card, null if not found
     */
    private String parseGradation(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id) {
                return obj.get(GRADATION).getAsString();
            }
        }

        return "null";
    }

    /**
     * Find DIRECTION property over the passed JsonArray using id
     * @param objArray != null
     * @param id != null, jsonArray.contains(id)
     * @return DIRECTION for specific id card, null if not found
     */
    private String parseDirection(JsonArray objArray, int id) {
        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id) {
                return obj.get(DIRECTION).getAsString();
            }
        }

        return "null";
    }
}
