package it.polimi.ingsw.server.model.objectivecard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ObjectiveStrategy {

    private WindowCard winCard;
    private ObjectiveCard objective;
    private ObjectiveCalculator calculator;

    public ObjectiveStrategy() {
       calculator = new ObjectiveCalculator();
    }

    public int calcPoint(ObjectiveCard obj, WindowCard winCard) throws ValueException, FileNotFoundException, IDNotFoundException, PositionException {
        this.winCard = winCard;
        this.objective = obj;

        if (obj instanceof PrivateObjective)
            return parsePriv(obj.getId());
        else if (obj instanceof PublicObjective)
            return parsePubl(obj.getId());

        throw new ValueException("Objective Card passed it's not correct");
    }

    private int parsePriv(int id) throws FileNotFoundException, IDNotFoundException {
        Colors col = Colors.NULL;
        JsonParser parser = new JsonParser();
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PrivateCard.json";

        JsonArray objArray = (JsonArray) parser.parse(new FileReader(infoPath));

        for (Object o : objArray) {
            JsonObject obj = (JsonObject) o;
            if (Integer.parseInt(obj.get("ID").toString()) == id)
                col = Colors.parseColor(obj.get("effect").getAsString());
        }

        if (col.equals(Colors.NULL))
            throw new IDNotFoundException("Objective Card id not found");

        return calculator.calcPointPriv(col, winCard);
    }

    private int parsePubl(int id) throws IDNotFoundException, FileNotFoundException, PositionException {
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
                type = obj.get("type").getAsString();
                scope = obj.get("scope").getAsString();
                grad = obj.get("grad").getAsString();
                dir = obj.get("dir").getAsString();
            }
        }

        if (type.equals("color"))
            return parseCol(scope, dir);
        else
            return parseShade(scope, grad, dir);
    }

    private int parseCol(String scope, String dir) throws IDNotFoundException, PositionException {
        if (scope.equals("diff"))
            if (dir.equals("col"))
                return calculator.calcDifferentColumnColor(winCard, objective);
            else
                return calculator.calcDifferentRowColor(winCard, objective);
        if (scope.equals("var"))
            return calculator.calcVarietyColor(winCard, objective);
        else // scope.equals("diag")
            return calculator.calcDiagonalColor(winCard);
    }

    private int parseShade(String scope, String grad, String dir) throws IDNotFoundException {
        if (scope.equals("diff"))
            if (dir.equals("col"))
                return calculator.calcDifferentColumnShade(winCard, objective);
            else
                return calculator.calcDifferentRowShade(winCard, objective);
        if (scope.equals("var"))
            return calculator.calcVarietyShade(winCard, objective);
        else { //scope.equals("grad")
            if (grad.equals("light"))
                return calculator.calcGradationShade(1 ,2, winCard, objective);
            if (grad.equals("medium"))
                return calculator.calcGradationShade(3 ,4, winCard, objective);
            else //grad.equals("dark")
                return calculator.calcGradationShade(5 ,6, winCard, objective);
        }
    }
}
