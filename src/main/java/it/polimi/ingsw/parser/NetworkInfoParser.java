package it.polimi.ingsw.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static java.lang.System.*;

/**
 * Read network settings from NetworkInfo.json
 */
public class NetworkInfoParser implements Parser {

    private final String path;

    NetworkInfoParser(String path) {
        this.path = path;
    }

    private static final String SOCKET_PORT = "socketPort";
    private static final String RMI_SERVER_PORT = "rmiServerPort";
    private static final String SO_TIMEOUT = "setSoTimeout";
    private static final String LOCAL_IP = "getLocalIp";

    /**
     * @return int for port used by socket connection
     */
    public int getSocketPort() {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(path));

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
            JsonObject obj = (JsonObject) parser.parse(new FileReader(path));

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
            JsonObject obj = (JsonObject) parser.parse(new FileReader(path));

            return Integer.parseInt(obj.get(SO_TIMEOUT).getAsString());

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * @return string for ip addressing in local network
     */
    public String getLocalIp() {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader(path));

            return obj.get(LOCAL_IP).getAsString();

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return "";
        }
    }
}
