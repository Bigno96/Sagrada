package it.polimi.ingsw.server.network.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;

public class SocketClientListener implements Runnable {

    private static String print = "print";
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

            while(true) {
                String command = socketIn.nextLine();

                if (command.equals("quit")) {
                    break;
                } else if (command.equals(print)) {
                    out.println(socketIn.nextLine());
                } else if (command.equals("connect")) {
                    String username = socketIn.nextLine();
                    speaker.connect(username);
                } else if (command.equals("login")) {
                    String user = socketIn.nextLine();
                    speaker.login(user);
                }
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
