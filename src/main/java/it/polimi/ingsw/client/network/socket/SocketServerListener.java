package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.view.ViewInterface;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class SocketServerListener implements Runnable {

    private final ViewInterface view;
    private final Socket socket;
    private final SocketServerSpeaker speaker;
    private final Semaphore go;

    SocketServerListener(Socket socket, ViewInterface view, SocketServerSpeaker speaker, Semaphore go) {
        this.view = view;
        this.socket = socket;
        this.speaker = speaker;
        this.go = go;
    }
    
    @Override
    public void run() {
        try {
            Scanner socketIn = new Scanner(socket.getInputStream());

            while(socketIn.hasNextLine()) {
                    String command = socketIn.nextLine();

                    if (command.equals("ping")) {
                        speaker.pong();
                    } else if (command.equals("print")) {
                        view.print(socketIn.nextLine());
                        go.release();
                    } else if (command.equals("parseException")) {
                        speaker.setLogged(parseException(socketIn.nextLine()));
                        go.release();
                    }
                }

        } catch (IOException e) {
            view.print(e.getMessage());
        }
    }

    /**
     * Find if s is the print of an Exception.
     * @param s != null
     * @return true if it was a string coming from an Exception, false else
     */
    private boolean parseException(String s) {
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

        return true;
    }
}
