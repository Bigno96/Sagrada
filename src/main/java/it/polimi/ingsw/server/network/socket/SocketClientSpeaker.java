package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.net.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.System.*;

public class SocketClientSpeaker implements Runnable, ClientSpeaker {

    private Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Lobby lobby;

    SocketClientSpeaker(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            while(socketIn.hasNextLine()) {
                String command = socketIn.nextLine();

                if (command.equals("quit")) {
                    break;
                }
                else if (command.equals("print")) {
                    out.println(socketIn.nextLine());
                }
                else if (command.equals("connect")) {
                    String user = socketIn.nextLine();
                    connect(user);
                }
                else if (command.equals("addPlayer")) {
                    String user = socketIn.nextLine();
                    addPlayer(user);
                }
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Used to connect player to the Server.
     * @param user to be connected
     */
    private synchronized void connect(String user) {
        out.println(user + " is connecting with Socket");
        socketOut.println("Connection Established");            // notify client the connection
        socketOut.flush();
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        socketOut.println(s);
        socketOut.flush();
    }

    /**
     * Add player to the Lobby. Catch and flush exception messages.
     * @param user to be logged in
     */
    private synchronized void addPlayer(String user) {
        try {
            lobby.addPlayerLobby(user, this);
            out.println(socketIn.nextLine());                   // waiting for success response from client

        } catch (GameAlreadyStartedException e) {
            socketOut.println("GameAlreadyStartedException");
            socketOut.flush();

        } catch (SamePlayerException e) {
            socketOut.println("SamePlayerException");
            socketOut.flush();

        } catch (TooManyPlayersException e) {
            socketOut.println("TooManyPlayersException");
            socketOut.flush();
        }
    }
}
