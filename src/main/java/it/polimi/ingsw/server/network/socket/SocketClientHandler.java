package it.polimi.ingsw.server.network.socket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.System.*;

public class SocketClientHandler implements Runnable, ClientSpeaker {

    private Socket socket;
    private PrintWriter socketOut;
    private Lobby lobby;
    private static String parse = "parseException";
    private Boolean pinged;

    SocketClientHandler(Socket socket, Lobby lobby) {
        this.lobby = lobby;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            while(socketIn.hasNextLine()) {
                    String command = socketIn.nextLine();

                    if (command.equals("quit")) {
                        break;
                    }
                    else if (command.equals("print")) {
                        out.println(socketIn.nextLine());
                    }
                    else if (command.equals("pong")) {
                        pinged=true;
                    }
                    else if (command.equals("connect")) {
                        String username = socketIn.nextLine();
                        connect(username);
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
     * @param username to be connected
     */
    private synchronized void connect(String username) {
        out.println(username + " is connecting with Socket");

        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            out.println(e.getMessage());
        }

        socketOut.println(parse);

        if (nameLookup(username)) {
            socketOut.println("Connection Established");            // notify client the connection
            socketOut.flush();
        }
        else {
            socketOut.println("SamePlayerException");
            socketOut.flush();
        }
    }

    /**
     * Find if same username is already logged
     * @param username != null
     * @return true if new username, false if already taken
     */
    private synchronized boolean nameLookup(String username) {
        String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PlayerLog.json";
        JsonParser parser = new JsonParser();
        JsonArray objArray;

        try {
            objArray = (JsonArray) parser.parse(new FileReader(infoPath));

            for (JsonElement o : objArray) {
                if (o.getAsString().equals(username))
                    return false;
            }

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return false;
        }

        try (JsonWriter writer = new JsonWriter(new FileWriter(infoPath))) {

            writer.beginArray();
            for (JsonElement o : objArray) {
                writer.value(o.getAsString());
            }
            writer.value(username);
            writer.endArray();

        } catch (IOException e) {
            out.println(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @param s to be printed
     */
    @Override
    public synchronized void tell(String s) {
        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            out.println(e.getMessage());
        }
        socketOut.println("print");
        socketOut.println(s);
        socketOut.flush();
    }

    @Override
    public boolean ping() {
        try {
            pinged = false;

            socketOut = new PrintWriter(socket.getOutputStream());
            socketOut.println("ping");
            socketOut.flush();

            synchronized (this) {
                wait(2000);
            }

            return pinged;

        } catch (IOException | NoSuchElementException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

    /**
     * Add player to the Lobby. Catch and flush exception messages.
     * @param username to be logged in
     */
    private synchronized void addPlayer(String username) {
        try {
            lobby.addPlayerLobby(username, this);

        } catch (GameAlreadyStartedException e) {
            socketOut.println(parse);
            socketOut.println("GameAlreadyStartedException");
            socketOut.flush();

        } catch (TooManyPlayersException e) {
            socketOut.println(parse);
            socketOut.println("TooManyPlayersException");
            socketOut.flush();
        }
    }

}
