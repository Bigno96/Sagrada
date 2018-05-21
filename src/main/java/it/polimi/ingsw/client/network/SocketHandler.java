package it.polimi.ingsw.client.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.*;

public class SocketHandler implements Handler {

    private int port;
    private String ip;
    private Socket socket;
    private boolean logged;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Scanner inKeyboard;

    public SocketHandler(int port, String ip) {
        logged = false;
        this.port = port;
        this.ip = ip;
    }

    @Override
    public void connect(String user) {
        out.println("Trying to connect");

        try {
            socket = new Socket(ip, port);

            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());
            inKeyboard = new Scanner(in);

            socketOut.println(user + "is logging in");
            socketOut.flush();
            out.println("Connection established");

        } catch(IOException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void listen() {
        out.println("Welcome! \n [press 'x' to exit]");

        do {
            logged = socket.isConnected();

            out.println("Insert Command: ");

            String inputLine = inKeyboard.nextLine();

            if (inputLine.equals("x")) {
                out.println("Closing connection");
                logged = false;
            }
            else {
                socketOut.println(inputLine);
                socketOut.flush();
                String socketLine = socketIn.nextLine();
                out.println(socketLine);
            }
        } while (logged);
    }

    @Override
    public void disconnect(String user) {
        try {
            socketOut.println(user + "is logging out");
            socketOut.flush();
            socket.close();
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

}
