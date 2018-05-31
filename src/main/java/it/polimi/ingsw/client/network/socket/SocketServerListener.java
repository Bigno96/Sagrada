package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.view.ViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketServerListener implements Runnable {

    private final ViewInterface view;
    private final Socket socket;
    private final SocketServerSpeaker speaker;

    SocketServerListener(Socket socket, ViewInterface view, SocketServerSpeaker speaker) {
        this.view = view;
        this.socket = socket;
        this.speaker = speaker;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.setSoTimeout(10000);     // 10 seconds

            while(true) {
                    String command = socketIn.readLine();

                    if (command.equals("quit")) {
                        break;
                    } else if (command.equals("print")) {
                        view.print(socketIn.readLine());
                    } else if (command.equals("loginSuccess") || command.equals("parseException")) {
                        speaker.setLogged(parseException(socketIn.readLine()));
                    }
                }

        } catch (IOException e) {
            view.print(e.getMessage());
            view.print("Server is not responding");
            speaker.interrupt();
        }
    }

    /**
     * Find if s is the print of an Exception.
     * @param s != null
     * @return true if it was a string coming from an Exception, false else
     */
    private synchronized boolean parseException(String s) {
        if (s.equals("GameAlreadyStartedException")) {
            view.print("Game is already started");
            return false;
        }
        else if (s.equals("SamePlayerException")) {
            view.print("An user with the same name already logged");
            return false;
        }
        else if (s.equals("TooManyPlayersException")) {
            view.print("Too many players in Lobby");
            return false;
        }
        else
            view.print(s);

        return true;
    }
}
