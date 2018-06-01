package it.polimi.ingsw.server.network.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

/**
 * Read network settings from NetworkInfo.json
 */
public class NetworkInfoParser {
    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/Json/NetworkInfo.json";
    private static final String SOCKET_PORT = "socketPort";
    private static final String RMI_SERVER_PORT = "rmiServerPort";
    private static final String SO_TIMEOUT = "setSoTimeout";

    public NetworkInfoParser() {
        // just creates the instance
    }

    /**
     * @return int for port used by socket connection
     */
    public int getSocketPort() {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(PATH));

            return Integer.parseInt(obj.get(SOCKET_PORT).getAsString());

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @return int for port used to get registry where server remote interface is bind
     */
    public int getRmiServerPort() {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(PATH));

            return Integer.parseInt(obj.get(RMI_SERVER_PORT).getAsString());

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @return int, time in ms of server's silence tolerated
     */
    public int getSoTimeout() {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(PATH));

            return Integer.parseInt(obj.get(SO_TIMEOUT).getAsString());

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return 0;
        }
    }
}
