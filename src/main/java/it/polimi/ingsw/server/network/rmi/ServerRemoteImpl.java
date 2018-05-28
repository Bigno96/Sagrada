package it.polimi.ingsw.server.network.rmi;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private Lobby lobby;

    public ServerRemoteImpl(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     * @throws SamePlayerException when trying to login same player twice
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) throws SamePlayerException {
        try {
            out.println("User " + client.getUsername() + " is connecting with RMI");

            if (!nameLookup(username))
                throw new SamePlayerException();

            client.tell("Connection established");

        } catch (RemoteException e) {
            out.println(e.getMessage());
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
    public void tell(String s) {
        out.println(s);
    }

    /**
     * @param username   != null
     * @param client instance of ClientRemote
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    @Override
    public synchronized void addPlayer(String username, ClientRemote client) throws TooManyPlayersException, GameAlreadyStartedException {
        ClientSpeaker speaker = new RmiClientSpeaker(client);
        lobby.addPlayerLobby(username, speaker);
    }

    @Override
    public void setWindowCard(WindowCard window, String userName) {
        /*List<Player> players = lobby.getGame().getPlayerList();
        for (Player p: players)
            if (p.getId().equals(userName))
                p.setWindowCard(window);
        */
        //notify other users
    }



}
