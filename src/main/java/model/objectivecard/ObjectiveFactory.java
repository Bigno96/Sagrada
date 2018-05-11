package model.objectivecard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.IDNotFoundException;

import java.io.*;

public class ObjectiveFactory {

    private ObjectiveStrategy objStrat;

    public ObjectiveFactory(ObjectiveStrategy objStrat) {
        this.objStrat = objStrat;
    }

    public ObjectiveCard getPrivCard(int id) throws FileNotFoundException, IDNotFoundException {
        String descr;
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PrivateCard.json";
        JsonParser parser = new JsonParser();

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        descr = findDescr(id, objArray);

        return new PrivateObjective(id, descr, objStrat);
    }

    public ObjectiveCard getPublCard(int id) throws FileNotFoundException, IDNotFoundException {
        int point;
        String descr;
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PublicCard.json";
        JsonParser parser = new JsonParser();

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        point = findPoint(id, objArray);
        descr = findDescr(id, objArray);

        return new PublicObjective(id, descr, point, objStrat);
    }

    private int findPoint(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return Integer.parseInt(obj.get("point").toString());
            }
        }
        throw new IDNotFoundException("Could't find matching Objective Card");
    }

    private String findDescr(int id, JsonArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("descr").getAsString();
            }
        }
        throw new IDNotFoundException("Could't find matching Objective Card");
    }



}
