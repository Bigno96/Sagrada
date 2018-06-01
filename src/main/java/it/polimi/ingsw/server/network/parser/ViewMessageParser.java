package it.polimi.ingsw.server.network.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

public class ViewMessageParser {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/Json/ViewDictionary.json";

    public ViewMessageParser() {
        // just creates the instance
    }

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
