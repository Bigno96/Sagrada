package it.polimi.model;

import exception.IDNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class WindowFactory {

    private JSONParser parser = new JSONParser();
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());
    private JSONObject winCard1;
    private JSONObject winCard2;
    private JSONObject winCard3;
    private JSONObject winCard4;

    public WindowFactory() {
        winCard1 = null;
        winCard2 = null;
        winCard3 = null;
        winCard4 = null;
    }

    //@requires id1 != id2
    //@ensures return.size() == 4
    public List<WindowCard> getWindow(int id1, int id2) throws IDNotFoundException{       // returns 2 couples of Window card (front and back) based on 2 int
        try {
            JSONArray winArray = (JSONArray) parser.parse(new FileReader("/home/bigno/Uni/ProgettoSoftware/Git/src/main/java/it/polimi/model/WindowInfo.json"));
            List<WindowCard> ret = new ArrayList<WindowCard>();

            for (Object o : winArray) {
                JSONObject obj = (JSONObject) o;
                if (Integer.parseInt(obj.get("ID").toString()) == id1) {
                    if (winCard1 == null) {
                        winCard1 = obj;
                    }
                    else winCard2 = obj;
                }
                else if (Integer.parseInt(obj.get("ID").toString()) == id2) {
                    if (winCard3 == null) {
                        winCard3 = obj;
                    }
                    else winCard4 = obj;
                }
            }

            if (winCard1 == null || winCard2 == null || winCard3 == null || winCard4 == null) {
                throw new IDNotFoundException();
            }

            return ret;

        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (ParseException e) {
            logger.info(e.getMessage());
        }

        return Collections.emptyList();
    }



}
