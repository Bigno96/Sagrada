package model.windowcard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.IDNotFoundException;
import exception.PositionException;
import exception.ValueException;
import model.Colors;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WindowFactory {

    private JsonObject winCard1;
    private JsonObject winCard2;

    public WindowFactory() {
        winCard1 = null;
        winCard2 = null;
    }

    /**requires id1 != id2
    ensures return.size() == 4*/
    public List<WindowCard> getWindow(int id1, int id2) throws FileNotFoundException, IDNotFoundException, ValueException, PositionException {       // returns 2 couples of Window card (front and back) based on 2 int
        List<WindowCard> ret = new ArrayList<>();
        JsonParser parser = new JsonParser();
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/WindowCard.json";

        JsonArray winArray = (JsonArray) parser.parse(new FileReader(infoPath));

        winCard1 = scanArray(winArray, null, id1);
        ret.add(makeCard(winCard1));
        winCard2 = scanArray(winArray, winCard1.get("name").toString(), id1);
        ret.add(makeCard(winCard2));
        winCard1 = scanArray(winArray, null, id2);
        ret.add(makeCard(winCard1));
        winCard2 = scanArray(winArray, winCard2.get("name").toString(), id2);
        ret.add(makeCard(winCard2));

        return ret;
    }

    //scan a JsonArray searching for matching id
    private JsonObject scanArray (JsonArray winArray, String name, int id) throws IDNotFoundException {     // since two card (front and back) has the same Id, name (which is unique) is needed
        for (Object o : winArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id && !obj.get("name").toString().equals(name)) {     // if it's not the card with the same name and has correct Id
                return obj;
            }
        }
        throw new IDNotFoundException("Could't find matching Window Card");
    }

    private WindowCard makeCard (JsonObject obj) throws ValueException, PositionException {                  // create an object Window Card from the object coming from WindowInfo
        //setting up parameter to pass to the Constructor of WindowCard
        int id = Integer.parseInt(obj.get("ID").toString());
        int numFavPoint = Integer.parseInt(obj.get("FP").toString());

        String name = obj.get("name").toString();

        JsonArray cellArr = (JsonArray) obj.get("Cell");            // json array to extract Cells from WindowInfo
        List<Cell> cells = new ArrayList<>();

        int row = 0;
        int col = 0;
        for (Object o : cellArr) {
            if (col > 4) {
                col = 0;
                row++;
            }
            cells.add(makeCell((JsonObject) o, row, col));
            col++;
        }

        return new WindowCard(id, name, numFavPoint, cells);
    }

    private Cell makeCell (JsonObject obj, int row, int col) throws ValueException, PositionException {                        // create a Cell from the object
        int value = Integer.parseInt(obj.get("value").toString());
        Colors color = Colors.parseColor(obj.get("color").toString());

        return new Cell(value, color, row, col);
    }

}
