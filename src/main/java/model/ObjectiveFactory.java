package model;

import exception.IDNotFoundException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.util.logging.Logger;

public class ObjectiveFactory {

    private static final Logger logger = Logger.getLogger(ObjectiveFactory.class.getName());
    private ObjectiveStrategy objStrat;

    public ObjectiveFactory(ObjectiveStrategy objStrat) {
        this.objStrat = objStrat;
    }

    public ObjectiveCard getPrivCard(int id) throws FileNotFoundException, IDNotFoundException {
        String descr;
        JsonReader reader = null;
        try {
            String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PrivateCard.json";
            InputStream file = new FileInputStream(infoPath);
            reader = Json.createReader(file);

            JsonArray objArray = (JsonArray) reader.read();
            descr = findDescr(id, objArray);

        } finally {
            if (reader != null)
                reader.close();
        }

        return new PrivateObjective(id, descr, objStrat);
    }

    public ObjectiveCard getPublCard(int id) throws FileNotFoundException, IDNotFoundException {
        int point;
        String descr;
        JsonReader reader = null;
        try {
            String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PublicCard.json";
            InputStream file = new FileInputStream(infoPath);
            reader = Json.createReader(file);
            JsonArray objArray = (JsonArray) reader.read();

            point = findPoint(id, objArray);
            descr = findDescr(id, objArray);
        } finally {
            if (reader != null)
                reader.close();
        }

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
                return obj.getString("descr");
            }
        }
        throw new IDNotFoundException("Could't find matching Objective Card");
    }



}
