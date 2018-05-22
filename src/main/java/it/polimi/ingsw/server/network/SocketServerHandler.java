package it.polimi.ingsw.server.network;

import java.net.*;
import java.io.*;
import java.util.*;

import static java.lang.System.*;

public class SocketServerHandler implements Runnable, ServerHandler {

    private int id;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;

    public SocketServerHandler(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            while(socket.isConnected() && socketIn.hasNextLine()) {
                synchronized (this) {
                    String command = socketIn.nextLine();

                    if (command.equals("login")) {
                        String user = socketIn.nextLine();
                        login(user);
                    }
                    else if (command.equals("logout")) {
                        String user = socketIn.nextLine();
                        logout(user);
                    }
                }
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void login(String user) {
        out.println(user + " is logging in");
        socketOut.println("Connection Established \n Welcome!");
        socketOut.flush();
        out.println(socketIn.nextLine());
    }

    @Override
    public void logout(String user) {
        out.println(user + " logged out");
    }
}
