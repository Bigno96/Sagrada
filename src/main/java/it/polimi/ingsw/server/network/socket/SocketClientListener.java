package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
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
    private static final String GET_CARD_KEYWORD = "GET_CARD";

    private static final String OTHER_USER_NAME_KEYWORD = "OTHER_USER_NAME";
    private static final String USER_NAME_KEYWORD = "USER_NAME";
    private static final String GET_ALL_USER_KEYWORD = "GET_ALL_USER";

    private static final String ASK_DRAFT_KEYWORD = "ASK_DRAFT";
    private static final String ASK_PUBLIC_OBJ_KEYWORD = "ASK_PUBLIC_OBJ";
    private static final String ASK_PRIVATE_OBJ_KEYWORD = "ASK_PRIVATE_OBJ";

    private static final String END_TURN_KEYWORD = "END_TURN";
    private static final String TURN_PASSED_KEYWORD = "TURN_PASSED";

    private final Socket socket;
    private final SocketClientSpeaker speaker;
    private final Lobby lobby;

    private final CommunicationParser communication;
    private final ViewMessageParser dictionary;
    private final HashMap<String, Consumer<String>> commandMap = new HashMap<>();

    private String username;
    private String otherUsername;

    SocketClientListener(Socket socket, SocketClientSpeaker speaker, Lobby lobby) {
        this.socket = socket;
        this.speaker = speaker;
        this.lobby = lobby;
        this.communication = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        mapCommand();
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = out::println;
        Consumer<String> connect = speaker::connect;
        Consumer<String> login = speaker::login;

        Consumer<String> setCard = cardName -> lobby.getGameController().setWindowCard(username, cardName);
        Consumer<String> getCard = me -> lobby.getSpeakers().get(me).printWindowCard(lobby.getPlayers().get(otherUsername).getWindowCard());

        Consumer<String> setUserName = user -> username = user;
        Consumer<String> otherUserName = user -> otherUsername = user;
        Consumer<String> getAllUsername = me -> {
            for (String user : lobby.getPlayers().keySet())
                if (!user.equals(me))
                    lobby.getSpeakers().get(me).tell(user);
        };

        Consumer<String> askDraft = me -> lobby.getSpeakers().get(me).showDraft(lobby.getGame().getBoard().getDraft());
        Consumer<String> askPublicObj = me -> lobby.getSpeakers().get(me).printPublicObj(lobby.getGame().getBoard().getPublObj());
        Consumer<String> askPrivateObj = me -> lobby.getSpeakers().get(me).printPrivateObj(lobby.getPlayers().get(username).getPrivObj());

        Consumer<String> endTurn = me -> {
            lobby.notifyAllPlayers(me + dictionary.getMessage(TURN_PASSED_KEYWORD));
            lobby.getRoundController().nextTurn();
        };

        commandMap.put(communication.getMessage(PRINT_KEYWORD), print);
        commandMap.put(communication.getMessage(CONNECT_KEYWORD), connect);
        commandMap.put(communication.getMessage(LOGIN_KEYWORD), login);

        commandMap.put(communication.getMessage(SET_CARD_KEYWORD), setCard);
        commandMap.put(communication.getMessage(GET_CARD_KEYWORD), getCard);

        commandMap.put(communication.getMessage(USER_NAME_KEYWORD), setUserName);
        commandMap.put(communication.getMessage(OTHER_USER_NAME_KEYWORD), otherUserName);
        commandMap.put(communication.getMessage(GET_ALL_USER_KEYWORD), getAllUsername);

        commandMap.put(communication.getMessage(ASK_DRAFT_KEYWORD), askDraft);
        commandMap.put(communication.getMessage(ASK_PUBLIC_OBJ_KEYWORD), askPublicObj);
        commandMap.put(communication.getMessage(ASK_PRIVATE_OBJ_KEYWORD), askPrivateObj);

        commandMap.put(communication.getMessage(END_TURN_KEYWORD), endTurn);
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
