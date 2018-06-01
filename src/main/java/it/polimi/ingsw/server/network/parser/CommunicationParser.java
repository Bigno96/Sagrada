package it.polimi.ingsw.server.network.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

/**
 * Parse Connection information
 */
public class CommunicationParser {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/Json/CommunicationProtocol.json";

    public CommunicationParser() {
        // just creates the instance
    }

    /**
     * @param input codification of the information ot be found
     * @return corresponding string
     */
    public String getMessage(String input) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(PATH));

            return obj.get(input).getAsString();

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return "";
        }
    }
}
