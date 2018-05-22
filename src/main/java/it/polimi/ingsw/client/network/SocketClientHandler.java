package it.polimi.ingsw.client.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.*;

public class SocketClientHandler implements ClientHandler {

    private String ip;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Scanner inKeyboard;

    public SocketClientHandler(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean connect(String user) {
        out.println("Trying to connect");
        out.println(ip);

        try {
            socket = new Socket(ip, 5000);

            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());
            inKeyboard = new Scanner(in);

            socketOut.println("login");
            socketOut.println(user);
            socketOut.flush();

            out.println(socketIn.nextLine());

            if(socket.isConnected()) {
                socketOut.println(user + " successfully logged");
                socketOut.flush();
                return true;
            }
            else {
                socketOut.println(user + " failed to log");
                socketOut.flush();
                return false;
            }


        } catch(IOException e) {
            out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void listen() {
        while(socket.isConnected()) {
            out.println("\nInsert Command: \n [press 'x' to exit]");

            String inputLine = inKeyboard.nextLine();

            if (inputLine.equals("x")) {
                out.println("Closing connection");
                break;
            } else {
                socketOut.println(inputLine);
                socketOut.flush();
                String socketLine = socketIn.nextLine();
                out.println(socketLine);
            }
        }
    }


    @Override
    public void disconnect(String user) {
        try {
            socketOut.println("logout");
            socketOut.println(user);
            socketOut.flush();
            socket.close();

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

}
