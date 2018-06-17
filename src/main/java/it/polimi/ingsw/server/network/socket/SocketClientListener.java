package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.lang.System.*;

/**
 * Listen on socket messages from client
 */
public class SocketClientListener implements Runnable {

    private static final String QUIT_KEYWORD = "QUIT";
    private static final String PRINT_KEYWORD = "PRINT";
    private static final String CONNECT_KEYWORD = "CONNECT";
    private static final String LOGIN_KEYWORD = "LOGIN";

    private static final String SET_CARD_KEYWORD = "SET_CARD";
    private static final String USER_NAME_KEYWORD = "USER_NAME";

    private Socket socket;
    private SocketClientSpeaker speaker;
    private Lobby lobby;

    private final CommunicationParser communication;
    private final HashMap<String, Consumer<String>> commandMap = new HashMap<>();

    private String username;

    SocketClientListener(Socket socket, SocketClientSpeaker speaker, Lobby lobby) {
        this.socket = socket;
        this.speaker = speaker;
        this.lobby = lobby;
        this.communication = (CommunicationParser) ParserManager.getCommunicationParser();
        mapCommand();
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = out::println;
        Consumer<String> connect = user -> speaker.connect(user);
        Consumer<String> login = user -> speaker.login(user);

        Consumer<String> setUserName = user -> username = user;
        Consumer<String> setCard = cardName -> lobby.getGameController().setWindowCard(username, cardName);

        commandMap.put(communication.getMessage(PRINT_KEYWORD), print);
        commandMap.put(communication.getMessage(CONNECT_KEYWORD), connect);
        commandMap.put(communication.getMessage(LOGIN_KEYWORD), login);

        commandMap.put(communication.getMessage(USER_NAME_KEYWORD), setUserName);
        commandMap.put(communication.getMessage(SET_CARD_KEYWORD), setCard);
    }

    @Override
    public void run() {
        try {
            Scanner socketIn = new Scanner(new InputStreamReader(socket.getInputStream()));

            while(true) {
                String command = socketIn.nextLine();

                if (command.equals(communication.getMessage(QUIT_KEYWORD))) {
                    break;

                } else if (commandMap.containsKey(command))       // if it's a known command
                    commandMap.get(command).accept((socketIn.nextLine()));      // execute it
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
