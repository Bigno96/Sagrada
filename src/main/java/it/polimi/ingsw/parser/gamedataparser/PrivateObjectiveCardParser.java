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

/**
 * Parse information of PrivateObjectiveCard
 */
public class PrivateObjectiveCardParser extends ObjectiveCardParser implements Parser {

    private static final String ID = "ID";
    private static final String TYPE = "TYPE";

    private final String infoPath;
    private final ObjectiveFactory factory;

    public PrivateObjectiveCardParser(String infoPath) {
        this.infoPath = infoPath;
        this.factory = new PrivateObjectiveFactory();
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

        ret = factory.makeCard(id, descr, 0);

        String type = parseType(objArray, id);

        factory.setParameter(ret, type, null, null, null);

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

        return null;
    }
}
