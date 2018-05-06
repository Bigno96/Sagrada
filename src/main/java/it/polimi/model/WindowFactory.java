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
import java.util.List;
import java.util.logging.Logger;

public class WindowFactory {

    private JSONParser parser = new JSONParser();
    private static final Logger logger = Logger.getLogger(WindowCard.class.getName());
    private JSONObject winCard1;
    private JSONObject winCard2;

    public WindowFactory() {
        winCard1 = null;
        winCard2 = null;
    }

    //@requires id1 != id2
    //@ensures return.size() == 4
    public List<WindowCard> getWindow(int id1, int id2) {       // returns 2 couples of Window card (front and back) based on 2 int
        List<WindowCard> ret = new ArrayList<WindowCard>();
        try {
            JSONArray winArray = (JSONArray) parser.parse(new FileReader("/home/bigno/Uni/ProgettoSoftware/Git/src/main/java/infoFile/WindowInfo.json"));        // parsing file

            winCard1 = scanArray(winArray, null, id1);
            ret.add(makeCard(winCard1));
            winCard2 = scanArray(winArray, winCard1.get("name").toString(), id1);
            ret.add(makeCard(winCard2));
            winCard1 = scanArray(winArray, null, id2);
            ret.add(makeCard(winCard1));
            winCard2 = scanArray(winArray, winCard2.get("name").toString(), id2);
            ret.add(makeCard(winCard2));

        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (ParseException e) {
            logger.info(e.getMessage());
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }

        return ret;
    }

    //scan a JsonArray searching for matching id
    private JSONObject scanArray (JSONArray winArray, String name, int id) throws IDNotFoundException {     // since two card (front and back) has the same Id, name (which is unique) is needed
        for (Object o : winArray) {
            JSONObject obj = (JSONObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id && !obj.get("name").toString().equals(name)) {     // if it's not the card with the same name and has correct Id
                return obj;
            }
        }
        throw new IDNotFoundException("Could't find matching Window Card");
    }

    private WindowCard makeCard (JSONObject obj) {                  // create an object Window Card from the object coming from WindowInfo
        //setting up parameter to pass to the Constructor of WindowCard
        int id = Integer.parseInt(obj.get("ID").toString());
        int numFavPoint = Integer.parseInt(obj.get("FP").toString());

        String name = obj.get("name").toString();

        JSONArray cellArr = (JSONArray) obj.get("Cell");            // json array to extract Cells from WindowInfo
        List<Cell> cells = new ArrayList<Cell>();

        int i = 0;
        for (Object o : cellArr) {
            cells.add(makeCell((JSONObject) o, i));
            i++;
        }

        return new WindowCard(id, name, numFavPoint, cells);
    }

    private Cell makeCell (JSONObject obj, int pos) {                        // create a Cell from the object
        int value = Integer.parseInt(obj.get("value").toString());
        Cell.colors color = parseColor(obj.get("color").toString());

        return new Cell(value, color, pos);
    }

    private Cell.colors parseColor(String string) {
        if (string.equals("YELLOW"))
            return Cell.colors.YELLOW;
        else if (string.equals("RED"))
            return Cell.colors.RED;
        else if (string.equals("BLUE"))
            return Cell.colors.BLUE;
        else if (string.equals("GREEN"))
            return Cell.colors.GREEN;
        else if (string.equals("VIOLET"))
            return Cell.colors.VIOLET;

        return Cell.colors.NULL;
    }
}
