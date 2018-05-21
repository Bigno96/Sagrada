package it.polimi.ingsw.server.network;

import java.net.*;
import java.io.*;
import java.util.*;

import static java.lang.System.*;

public class ClientSocketHandler implements Runnable {

    private int id;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;

    public ClientSocketHandler(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public synchronized void run() {
        try {
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            socketOut.println("Server is connected");
            socketOut.flush();

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

}
