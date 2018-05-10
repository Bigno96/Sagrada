package model.objectiveCard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.IDNotFoundException;
import exception.ValueException;
import model.Colors;
import model.windowCard.Cell;
import model.windowCard.WindowCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

public class ObjectiveStrategy {

    private WindowCard winCard;

    public ObjectiveStrategy() {
    }

    public int calcPoint(ObjectiveCard obj, WindowCard winCard) throws ValueException, FileNotFoundException, IDNotFoundException {
        this.winCard = winCard;
        if (obj instanceof PrivateObjective) {
            return calcPriv(obj.getId());
        }
        else if (obj instanceof PublicObjective) {
            return parsePubl(obj.getId());
        }

        throw new ValueException("Objective Card passed it's not correct");
    }

    private int calcPriv(int id) throws FileNotFoundException, IDNotFoundException {
        Colors col = null;
        int sum = 0;
        JsonParser parser = new JsonParser();
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PrivateCard.json";

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                col = Colors.parseColor(obj.get("effect").toString());
            }
        }

        if (col == null)
            throw new IDNotFoundException("Objective Card id not found");

        for (Iterator<Cell> itr = winCard.getOrizzItr(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.getColor() == col) {
                sum += c.getValue();
            }
        }

        return sum;
    }

    private int parsePubl(int id) throws IDNotFoundException, FileNotFoundException {
        String type = "null";
        String scope = "null";
        String grad = "null";
        String dir = "null";
        JsonParser parser = new JsonParser();
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PublicCard.json";

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id) {
                type = obj.get("type").toString();
                scope = obj.get("scope").toString();
                grad = obj.get("grad").toString();
                dir = obj.get("dir").toString();
            }
        }
        return calcPubl(type, scope, grad, dir);
    }

    private int calcPubl(String type, String scope, String grad, String dir) throws IDNotFoundException {
        if (type.equals("null"))
            throw new IDNotFoundException("Objective Card id not found");

        if (type.equals("color")) {
            if (scope.equals("diff"))
                return calcDifferentColor(dir);
            if (scope.equals("var"))
                return calcVarietyColor();
            if (scope.equals("diag"))
                return calcDiagonalColor();
        }

        if (type.equals("shade")) {
            if (scope.equals("diff"))
                return calcDifferentShade(dir);
            if (scope.equals("grad"))
                return calcGradationShade(grad);
        }

        return 0;
    }

    private int calcDifferentColor(String dir) {

        return 0;
    }

    private int calcVarietyColor() {
        return 0;
    }

    private int calcDiagonalColor() {
        return 0;
    }

    private int calcDifferentShade(String dir) {
        return 0;
    }

    private int calcGradationShade(String grad) {
        return 0;
    }
}
