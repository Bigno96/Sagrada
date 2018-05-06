package it.polimi.model;

import exception.IDNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class ObjectiveFactory {

    private JSONParser parser = new JSONParser();
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());
    private ObjectiveStrategy objStrat;

    public ObjectiveFactory(ObjectiveStrategy objStrat) {
        this.objStrat = objStrat;
    }

    public ObjectiveCard getPrivCard(int id) {
        String descr = null;
        try {
            JSONArray objArray = (JSONArray) parser.parse(new FileReader("/home/bigno/Uni/ProgettoSoftware/Git/src/main/java/infoFile/PrivateCard.json"));

            descr = findDescr(id, objArray);

        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (ParseException e) {
            logger.info(e.getMessage());
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }

        return new PrivateObjective(id, descr, objStrat);
    }

    public ObjectiveCard getPublCard(int id) {
        int point = 0;
        String descr = null;
        try {
            JSONArray objArray = (JSONArray) parser.parse(new FileReader("/home/bigno/Uni/ProgettoSoftware/Git/src/main/java/infoFile/PublicCard.json"));

            point = findPoint(id, objArray);
            descr = findDescr(id, objArray);

        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (ParseException e) {
            logger.info(e.getMessage());
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }

        return new PublicObjective(id, descr, point, objStrat);
    }

    public int findPoint(int id, JSONArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JSONObject obj = (JSONObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return Integer.parseInt(obj.get("point").toString());
            }
        }
        throw new IDNotFoundException("Could't find matching Objective Card");
    }

    public String findDescr(int id, JSONArray objArr) throws IDNotFoundException {
        for (Object o : objArr) {
            JSONObject obj = (JSONObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                return obj.get("descr").toString();
            }
        }
        throw new IDNotFoundException("Could't find matching Objective Card");
    }

}
