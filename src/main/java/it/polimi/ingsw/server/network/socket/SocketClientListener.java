package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.ParserManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.*;

/**
 * Listen on socket messages from client
 */
public class SocketClientListener implements Runnable {

    private static final String QUIT_KEYWORD = "QUIT";
    private static final String PRINT_KEYWORD = "PRINT";
    private static final String CONNECT_KEYWORD = "CONNECT";
    private static final String LOGIN_KEYWORD = "LOGIN";

    private Socket socket;
    private SocketClientSpeaker speaker;

    SocketClientListener(Socket socket, SocketClientSpeaker speaker) {
        this.socket = socket;
        this.speaker = speaker;
    }

    @Override
    public void run() {
        try {
            Scanner socketIn = new Scanner(new InputStreamReader(socket.getInputStream()));
            CommunicationParser communication = (CommunicationParser) ParserManager.getCommunicationParser();

            while(true) {
                String command = socketIn.nextLine();

                if (command.equals(communication.getMessage(QUIT_KEYWORD))) {
                    break;
                } else if (command.equals(communication.getMessage(PRINT_KEYWORD))) {
                    out.println(socketIn.nextLine());
                } else if (command.equals(communication.getMessage(CONNECT_KEYWORD))) {
                    String username = socketIn.nextLine();
                    speaker.connect(username);
                } else if (command.equals(communication.getMessage(LOGIN_KEYWORD))) {
                    String user = socketIn.nextLine();
                    speaker.login(user);
                }
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
