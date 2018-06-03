package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.Parser;

class ObjectiveCardParser implements Parser {

    public int findPoint(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id)
                return Integer.parseInt(obj.get("point").toString());
        }

        throw new IDNotFoundException("Could't find matching Objective Card");
    }

    public String findDescr(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id)
                return obj.get("descr").getAsString();
        }

        throw new IDNotFoundException("Could't find matching Objective Card");
    }
}
