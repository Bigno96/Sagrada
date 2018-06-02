package it.polimi.ingsw.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

/**
 * Parse Connection information
 */
public class CommunicationParser implements Parser {

    private final String path;

    CommunicationParser(String path) {
        this.path = path;
    }

    /**
     * @param input codification of the information ot be found
     * @return corresponding string
     */
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
