package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.exception.SamePlayerException;

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

    /**
     * Find if s is the print of an Exception. Print s anyway.
     * @param s != null
     * @return true if it was a string coming from an Exception, false else
     */
    private boolean parseException(String s) {
        if (s.equals("GameAlreadyStartedException")) {
            out.println("Game is already started");
            return true;
        }
        else if (s.equals("SamePlayerException")) {
            out.println("An user with the same name already logged");
            return true;
        }
        else if (s.equals("TooManyPlayersException")) {
            out.println("Too many players in Lobby");
            return true;
        }
        else
            out.println(s);

        return false;
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * @param username != null
     * @return true if connection was successful, false else
     * @throws SamePlayerException when trying to login same player twice
     */
    @Override
    public boolean connect(String username) throws SamePlayerException {
        out.println("Trying to connect to " + ip);

        try{
            socket = new Socket(ip, 5000);

            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            socketOut.println("connect");       // asking for connection
            socketOut.println(username);        // username passed
            socketOut.flush();

            if (parseException(socketIn.nextLine()))   // waiting response "Connection Established" or "SamePlayerException"
                throw new SamePlayerException();

            if(socket.isConnected()) {          // if was successful
                socketOut.println("print");
                socketOut.println("User " + username + " successfully connected");      // print on server the success
                socketOut.flush();
                return true;
            }

            return false;

        } catch(IOException e) {
            out.println(e.getMessage());
            return false;
        }
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean login(String username) {
        try {
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            socketOut.println("addPlayer");                 // ask for login
            socketOut.println(username);                    // username passed
            socketOut.flush();

            if (parseException(socketIn.nextLine()))        // check if server sends an exception message. Else,
                return false;                               // printing "Welcome"

            out.println(socketIn.nextLine());               // printing "Game will start shortly"

            socketOut.println("User " + username + " successfully logged in");      // inform server login was successful
            socketOut.flush();

            out.println(socketIn.nextLine());           // waiting for "Game timer is on" notification
            out.println(socketIn.nextLine());           // waiting for "Game is starting" notification

            return true;

        } catch (IOException e) {
            return false;
        }
    }

}
