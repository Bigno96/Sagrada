package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.server.network.parser.ViewMessageParser;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.*;

class CliAskConnection {

    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ServerSpeaker serverSpeaker;
    private final Scanner inKeyboard;
    private final HashMap<String, ServerSpeaker> connParam;

    private final ViewMessageParser dictionary;
    private final HashMap<String, Supplier<String>> connectionMap;
    private final HashMap<String, Consumer<Boolean>> connectionAction;

    CliAskConnection(){
        this.socketConnection = false;
        this.rmiConnection = false;
        this.inKeyboard = new Scanner(in);
        this.connParam = new HashMap<>();
        this.dictionary = new ViewMessageParser();
        this.connectionMap = new HashMap<>();
        this.connectionAction = new HashMap<>();
        mapConnection();
    }

    /**
     * Maps exception with their error code to be printed
     */
    private void mapConnection() {
        connectionMap.put("s", () -> dictionary.getMessage("SOCKET_CHOSEN"));
        connectionMap.put("d", () -> dictionary.getMessage("SOCKET_CHOSEN"));
        connectionMap.put("r", () -> dictionary.getMessage("RMI_CHOSEN"));

        Consumer<Boolean> socket = this::setSocketConnection;
        Consumer<Boolean> rmi = this::setRmiConnection;

        connectionAction.put("s", socket);
        connectionAction.put("d", socket);
        connectionAction.put("r", rmi);
    }

    private void setSocketConnection(boolean bool) {
        this.socketConnection = bool;
    }

    private void setRmiConnection(boolean bool) {
        this.rmiConnection = bool;
    }

    /**
     * Ask user connection parameters
     * @param cli != null, passed to the speaker
     * @return HashMap<username, instance of speaker chosen>
     */
    HashMap<String, ServerSpeaker> startConnection(CliSystem cli) {

        out.println(dictionary.getMessage("INSERT_NAME"));           // ask name of the user
        String userName = inKeyboard.nextLine();

        askConnection();            // ask type of connection wanted
        String ip = requestIp();

        if (socketConnection) {
            serverSpeaker = new SocketServerSpeaker(ip, cli);       // delegate to a socket connection
        } else if (rmiConnection) {
            serverSpeaker = new RmiServerSpeaker(ip, userName, cli);             // delegate to a rmi connection
        }

        while (!serverSpeaker.connect(userName)) {
            out.println(dictionary.getMessage("NO_SERVER_LISTENING"));  // ip didn't connected
            ip = requestIp();
            serverSpeaker.setIp(ip);
        }

        while (!serverSpeaker.login(userName)) {
            out.println(dictionary.getMessage("INSERT_NAME_AGAIN"));
            userName = inKeyboard.nextLine();
        }

        connParam.put(userName, serverSpeaker);

        return connParam;
    }

    /**
     * Ask for type of Connection to use
     */
    private void askConnection() {
        do {
            out.println(dictionary.getMessage("CHOOSE_CONNECTION"));

            Scanner input = new Scanner(System.in);
            String s = input.nextLine();

            if (!connectionMap.containsKey(s))
                out.println(dictionary.getMessage("INCORRECT_ENTRY"));
            else {
                connectionMap.get(s).get();
                connectionAction.get(s).accept(true);
            }

        } while (!socketConnection && !rmiConnection);
    }

    /**
     * Ask for ip
     * @return String read from cmd as input
     */
    private String requestIp() {
        Boolean ok = false;
        String ret = "";

        while(!ok) {
            out.println(dictionary.getMessage("INSERT_IP"));
            Scanner input = new Scanner(System.in);
            ret = input.nextLine();

            ok = validIP(ret);                   // verify if ip is a valid address

            if(!ok)
                out.println(dictionary.getMessage("WRONG_IP"));
        }

        return ret;
    }

    /**
     * Verify if passed String represents a valid IPv4 or IPv6 address
     * @param ip != null
     * @return true if valid ip address is passed, else false
     */
    private boolean validIP(String ip) {
        if (ip.isEmpty())
            return false;

        String[] parts = ip.split("\\.");

        if (parts.length != 4 && parts.length != 6)
            return false;

        List<Integer> intParts = new ArrayList<>();
        Arrays.asList(parts).forEach(string -> intParts.add(Integer.parseInt(string)));        // convert to integer

        Boolean okay = intParts.stream()
                .noneMatch(x -> x < 0 || x > 255);

        return okay && !ip.endsWith(".");
    }

}
