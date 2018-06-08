package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.Parser;

/**
 * Parse common information of PublicObjectiveCard and PrivateObjectiveCard
 */
class ObjectiveCardParser implements Parser {

    private static final String ID = "ID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String POINT = "POINT";
    private static final String ID_NOT_FOUND = "Could't find matching Objective Card";

    /**
     * Find corresponding point of given id from passed JsonArray.
     * @param id != null
     * @param objArr != null, got from PrivateCard.json or PublicCard.json
     * @return point of tool card
     * @throws IDNotFoundException when passed id doesn't match any in the Json
     */
    public int findPoint(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id)
                return Integer.parseInt(obj.get(POINT).toString());
        }

        throw new IDNotFoundException(ID_NOT_FOUND);
    }

    /**
     * Find corresponding description of given id from passed JsonArray.
     * @param id != null
     * @param objArr != null, got from PrivateCard.json or PublicCard.json
     * @return description of tool card
     * @throws IDNotFoundException when passed id doesn't match any in the Json
     */
    public String findDescr(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id)
                return obj.get(DESCRIPTION).getAsString();
        }

        throw new IDNotFoundException(ID_NOT_FOUND);
    }
}
