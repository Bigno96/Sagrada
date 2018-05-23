package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.server.network.ServerHandler;

import java.net.*;
import java.io.*;
import java.util.*;

import static java.lang.System.*;

public class SocketServerHandler implements Runnable, ServerHandler {

    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;

    public SocketServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            while(socket.isConnected() && socketIn.hasNextLine()) {
                    String command = socketIn.nextLine();

                    if (command.equals("print")) {
                        print();
                    }
                    else if (command.equals("login")) {
                        String user = socketIn.nextLine();
                        login(user);
                    }
                }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    private void print() {
        while(socketIn.hasNextLine())
            out.println(socketIn.nextLine());
    }

    private void login(String user) {
        out.println(user + " is logging in with Socket");
        socketOut.println("Connection Established \nWelcome " + user);
        socketOut.flush();
    }
}
