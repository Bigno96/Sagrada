package it.polimi.ingsw.parser.gamedataparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.parser.Parser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse information of WindowCard
 */
public class WindowParser implements Parser {

    private static WindowParser ourInstance = null;
    private final GameSettingsParser gameSettings;

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String FP = "FP";
    private static final String CELL_LIST = "CELLS";
    private static final String CELL_VALUE = "VALUE";
    private static final String CELL_COLOR = "COLOR";
    private static final String ID_NOT_FOUND = "Could't find matching Window Card";

    private JsonObject winCard1;
    private JsonObject winCard2;
    private final String infoPath;

    private WindowParser(String infoPath) {
        winCard1 = null;
        winCard2 = null;
        this.infoPath = infoPath;
        this.gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    }

    public static WindowParser getInstance(String infoPath) {
        if (ourInstance == null)
            ourInstance = new WindowParser(infoPath);

        return ourInstance;
    }

    /**
     * Return the 4 WindowCards with the passed id1, id2
     * @param id1 != id2 && id1 >= 1 && id1 <= 12
     * @param id2 != id1 && >= 1 && id1 <= 12
     * @return List<WindowCard> && return.size() == 4
     * @throws FileNotFoundException when File Reader doesn't find the info file
     * @throws IDNotFoundException when card has an illegal id
     * @throws ValueException when invalid value
     * @throws PositionException when invalid position
     */
    public List<WindowCard> getWindow(int id1, int id2) throws FileNotFoundException, IDNotFoundException, ValueException, PositionException {       // returns 2 couples of Window card (front and back) based on 2 int
        List<WindowCard> ret = new ArrayList<>();
        JsonParser parser = new JsonParser();

        JsonArray winArray = (JsonArray) parser.parse(new FileReader(infoPath));

        winCard1 = scanArray(winArray, null, id1);
        ret.add(makeCard(winCard1));

        winCard2 = scanArray(winArray, winCard1.get(NAME).toString(), id1);
        ret.add(makeCard(winCard2));

        winCard1 = scanArray(winArray, null, id2);
        ret.add(makeCard(winCard1));

        winCard2 = scanArray(winArray, winCard1.get(NAME).toString(), id2);
        ret.add(makeCard(winCard2));

        return ret;
    }

    /**
     * Return a window card with the corresponding name
     * @param cardName != null
     * @return WindowCard, WindowCard.getName == cardName
     * @throws FileNotFoundException when File Reader doesn't find the info file
     * @throws IDNotFoundException when card has an illegal id
     * @throws ValueException when invalid value
     * @throws PositionException when invalid position
     */
    public WindowCard getWindow(String cardName) throws FileNotFoundException, IDNotFoundException, ValueException, PositionException {
        JsonParser parser = new JsonParser();

        JsonArray winArray = (JsonArray) parser.parse(new FileReader(infoPath));

        winCard1 = scanArray(winArray, cardName);
        return makeCard(winCard1);
    }

    /**
     * Scan a JsonArray searching for matching id
     * @param winArray != null, array of all window cards
     * @param name of the card to discard. Since two card (front and back) has the same Id, name (which is unique) is needed
     * @param id != null && winArray.contains(id)
     * @return windowCard searched as a JsonObject, with passed id but different name
     * @throws IDNotFoundException when id is not found over the json
     */
    private JsonObject scanArray (JsonArray winArray, String name, int id) throws IDNotFoundException {
        for (Object o : winArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get(ID).toString()) == id && !obj.get(NAME).toString().equals(name))
                return obj;
        }

        throw new IDNotFoundException(ID_NOT_FOUND);
    }

    /**
     * Scan a JsonArray searching for matching name
     * @param winArray != null, array of all window cards
     * @param cardName != null, name searched. Since two card (front and back) has the same Id, name (which is unique) is needed
     * @return windowCard searched as a JsonObject, if it's the card with the same name
     * @throws IDNotFoundException when id is not found over the json
     */
    private JsonObject scanArray (JsonArray winArray, String cardName) throws IDNotFoundException {
        for (Object o : winArray) {
            JsonObject obj = (JsonObject) o;
            if (obj.get(NAME).getAsString().equals(cardName))
                return obj;
        }

        throw new IDNotFoundException(ID_NOT_FOUND);
    }

    /**
     * Create an object Window Card from the passed JsonObject
     * @param obj != null, the window card wanted parsed as JsonObject
     * @return instance of Window Card
     * @throws ValueException if found value for cell does not respect Cell.value restrictions
     * @throws PositionException if position of cell in the MatrixCell is over its bound
     */
    private WindowCard makeCard (JsonObject obj) throws ValueException, PositionException {
        //setting up parameter to pass to the Constructor of WindowCard
        int id = Integer.parseInt(obj.get(ID).toString());
        int numFavPoint = Integer.parseInt(obj.get(FP).toString());

        String name = obj.get(NAME).getAsString();

        JsonArray cellArr = (JsonArray) obj.get(CELL_LIST);            // json array to extract Cells from WindowInfo
        List<Cell> cells = new ArrayList<>();

        int row = 0;
        int col = 0;
        for (Object o : cellArr) {
            if (col > gameSettings.getWindowCardMaxColumn()) {
                col = 0;
                row++;
            }
            cells.add(makeCell((JsonObject) o, row, col));
            col++;
        }

        return new WindowCard(id, name, numFavPoint, cells, gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
    }

    /**
     * Create Cell from the passed JsonObject
     * @param obj != null, represent the Cell
     * @param row >= 0 && <= MAX_WINDOW_ROW
     * @param col >= && <= MAX_WINDOW_COLUMN
     * @return Cell parsed from JsonObject
     * @throws ValueException if found value for cell does not respect Cell.value restrictions
     * @throws PositionException if position of cell in the MatrixCell is over its bound
     */
    private Cell makeCell (JsonObject obj, int row, int col) throws ValueException, PositionException {
        int value = Integer.parseInt(obj.get(CELL_VALUE).toString());
        Colors color = Colors.parseColor(obj.get(CELL_COLOR).getAsString());

        return new Cell(value, color, row, col, gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
    }

}
