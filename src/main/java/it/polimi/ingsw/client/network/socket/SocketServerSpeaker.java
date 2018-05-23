package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.ServerSpeaker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.*;

public class SocketServerSpeaker implements ServerSpeaker {

    private String ip;
    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;

    public SocketServerSpeaker(String ip) {
        this.ip = ip;
    }

    private void print() {
        while(socketIn.hasNextLine())
            out.println(socketIn.nextLine());
    }

    @Override
    public void setIp(String ip) {
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

            socketOut.println("login");
            socketOut.println(user);

            if(socket.isConnected()) {
                socketOut.println("print");
                socketOut.println("User " + user + " successfully logged");
                socketOut.flush();
                print();
                return true;
            }

            return false;

        } catch(IOException e) {
            out.println(e.getMessage());
            return false;
        }
    }

}
