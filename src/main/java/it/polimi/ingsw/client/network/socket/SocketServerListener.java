package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Thread reading socket input coming from server
 */
public class SocketServerListener implements Runnable {

    private static final String GAME_ALREADY_STARTED_KEYWORD = "GAME_ALREADY_STARTED_MSG";
    private static final String SAME_PLAYER_LOGGED_KEYWORD = "SAME_PLAYER_MSG";
    private static final String TOO_MANY_PLAYERS_KEYWORD = "TOO_MANY_PLAYERS_MSG";
    private static final String PRINT_KEYWORD = "PRINT";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String EXCEPTION_KEYWORD = "EXCEPTION";
    private static final String QUIT_KEYWORD = "QUIT";
    private static final String SERVER_NOT_RESPONDING_KEYWORD = "SERVER_NOT_RESPONDING";

    private final ViewInterface view;
    private final Socket socket;
    private final SocketServerSpeaker speaker;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    private final HashMap<String, Supplier<String>> exceptionMap = new HashMap<>();
    private final HashMap<String, Consumer<String>> commandMap = new HashMap<>();

    SocketServerListener(Socket socket, ViewInterface view, SocketServerSpeaker speaker) {
        this.view = view;
        this.socket = socket;
        this.speaker = speaker;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        mapException();
        mapCommand();
    }

    /**
     * Maps exception with their error code to be printed
     */
    private void mapException() {
        exceptionMap.put(GameAlreadyStartedException.class.toString(), () -> dictionary.getMessage(GAME_ALREADY_STARTED_KEYWORD));
        exceptionMap.put(SamePlayerException.class.toString(), () -> dictionary.getMessage(SAME_PLAYER_LOGGED_KEYWORD));
        exceptionMap.put(TooManyPlayersException.class.toString(), () -> dictionary.getMessage(TOO_MANY_PLAYERS_KEYWORD));
    }

    /**
     * Maps protocol command with their realization
     */
    private void mapCommand() {
        Consumer<String> print = view::print;
        Consumer<String> login = string -> speaker.setLogged(parseException(string));
        Consumer<String> exception = string -> speaker.setLogged(parseException(string));

        commandMap.put(protocol.getMessage(PRINT_KEYWORD), print);
        commandMap.put(protocol.getMessage(LOGIN_SUCCESS_KEYWORD), login);
        commandMap.put(protocol.getMessage(EXCEPTION_KEYWORD), exception);
    }

    /**
     * Listening for incoming server messages
     */
    @Override
    public void run() {
        try {
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();
            socket.setSoTimeout(parser.getSoTimeout());     // 30 seconds

            while (true) {
                String command = socketIn.readLine();

                if (command.equals(protocol.getMessage(QUIT_KEYWORD)))
                    break;

                else if (commandMap.containsKey(command))       // if it's a known command
                    commandMap.get(command).accept((socketIn.readLine()));      // execute it
            }

        } catch (IOException e) {
            view.print(e.getMessage());
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
            speaker.interrupt();
        }
    }

    /**
     * Find if s is the print of an Exception.
     * @param s != null
     * @return true if it was a string coming from an Exception, false else
     */
    private boolean parseException(String s) {
        if (exceptionMap.containsKey(s)) {                  // if it's a known exception
            view.print(exceptionMap.get(s).get());          // print it's message
            return false;
        }
        else
            view.print(s);              // print anyway

        return true;
    }
}
