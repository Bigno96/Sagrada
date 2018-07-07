package it.polimi.ingsw.parser.messageparser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.parser.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.out;


/**
 * Parse messages to be displayed by the view
 */
public class ViewMessageParser implements Parser {

    private static ViewMessageParser ourInstance = null;

    private final String path;

    private ViewMessageParser(String path) {
        this.path = path;
    }

    public static ViewMessageParser getInstance(String infoPath) {
        if (ourInstance == null)
            ourInstance = new ViewMessageParser(infoPath);

        return ourInstance;
    }

    public String getMessage(String input) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(path));

            return obj.get(input).getAsString();

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return "";
        }
    }
}
