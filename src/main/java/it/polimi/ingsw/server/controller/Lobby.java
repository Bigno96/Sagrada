package it.polimi.ingsw.server.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.*;

public class Lobby {

    private Game game;
    private static String infoPath = System.getProperty("user.dir") + "/src/main/java/resources/PlayerLog.json";
    private ChooseWindowCardObserver chooseWindowCardObserver;
    private MoveDiceObserver moveDiceObserver;
    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private int nPlayer;
    private boolean gameStarted;
    private Timer preGameTimer;
    private List<String> disconnectedPlayer;

    public Lobby() {
        game = new Game(chooseWindowCardObserver);
        chooseWindowCardObserver = new ChooseWindowCardObserver(this);
        moveDiceObserver = new MoveDiceObserver(this);
        players = new HashMap<>();
        speakers = new HashMap<>();
        nPlayer = 1;
        gameStarted = false;
        preGameTimer = new Timer();
        disconnectedPlayer = new ArrayList<>();
        startGameDaemon();
    }

    /**
     * Get speaker for player passed as parameter
     * @param p game.contains(p)
     * @return ClientSpeaker for player p
     */
    public ClientSpeaker getSpeaker(Player p) {
        return speakers.get(p.getId());
    }

    /**
     * Find if same username is already logged
     * @param username != null
     * @return true if new username, false if already taken
     */
    private synchronized boolean nameLookup(String username) {
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
     * Add player to the Lobby, first connection or reconnection
     * @param username != null
     * @param speaker instanceof ClientSpeaker
     * @throws GameAlreadyStartedException when trying to enter after game has started
     * @throws TooManyPlayersException when adding a player on full lobby, 4 player
     */
    public synchronized void addPlayerLobby(String username, ClientSpeaker speaker) throws GameAlreadyStartedException, TooManyPlayersException, SamePlayerException {
        if (disconnectedPlayer.contains(username)) {        // if player has been disconnected
            reconnectPlayer(username, speaker);
        }
        else {
            if (!nameLookup(username))
                throw new SamePlayerException();

            if (gameStarted)
                throw new GameAlreadyStartedException();

            if (nPlayer > 4)
                throw new TooManyPlayersException();

            Player p = new Player(username);
            players.put(username, p);
            speakers.put(username, speaker);
            nPlayer++;

            speaker.tell("Welcome " + username);
            speaker.tell("Game will start when enough player are connected");
        }
    }

    public void setDisconnectedPlayer(String username) {
        disconnectedPlayer.add(username);
    }

    synchronized void removePlayer(String username) throws EmptyException, PlayerNotFoundException {
        JsonParser parser = new JsonParser();
        JsonArray objArray;

        if (!players.containsKey(username))
            throw new PlayerNotFoundException();

        Player p = players.get(username);
        if (isGameStarted()) {
            //p.setDisconnected();
        }
        else{
            speakers.remove(p.getId());
            players.remove(username);
            disconnectedPlayer.remove(username);
            reduceNPlayer();                           // 1 player less in the lobby
            out.println("User " + username + " was removed");
        }

        try {
            objArray = (JsonArray) parser.parse(new FileReader(infoPath));

            JsonElement found = null;
            for (JsonElement o : objArray) {
                if (o.getAsString().equals(username)) {
                    found = o;
                    break;
                }
            }

            objArray.remove(found);

        } catch (FileNotFoundException e) {
            out.println(e.getMessage());
            return;
        }

        try (JsonWriter writer = new JsonWriter(new FileWriter(infoPath))) {

            writer.beginArray();
            for (JsonElement o : objArray) {
                writer.value(o.getAsString());
            }
            writer.endArray();

        } catch (IOException e) {
            out.println(e.getMessage());
        }

        checkCloseGame();
    }

    private synchronized void checkCloseGame() throws EmptyException {
        if (players.size() == 1) {    // if 1 or none player are left in the game, close it
            closeGame();
            for (Map.Entry<String, ClientSpeaker> entry : speakers.entrySet())
                entry.getValue().tell("Insufficient player remained to continue the game\n\nCongratulations! You won");
        } else if (players.size() == 0)
            closeGame();
    }

    /**
     * Remove all player from game
     * @throws EmptyException when trying to remove from an empty game
     */
    private synchronized void closeGame() throws EmptyException {
        for (Map.Entry<String,Player> entry : players.entrySet())
            game.rmPlayer(entry.getValue());
    }

    void setGameStarted() {
        gameStarted = true;
    }

    Boolean isGameStarted() {
        return this.gameStarted;
    }

    void reduceNPlayer() {
        nPlayer--;
    }

    private void startGameDaemon() {
        Timer daemonTimer = new Timer();
        daemonTimer.scheduleAtFixedRate(new CheckStartGameDaemon(players, disconnectedPlayer, this), 0, 100);
    }

    /**
     * Empty the lobby when errors occur while starting game
     */
    synchronized void emptyLobby() {
        players.clear();
        speakers.clear();
        nPlayer = 1;
        game = new Game(chooseWindowCardObserver);
        disconnectedPlayer.clear();
    }

    /**
     * Start timer for Game preparation, 3 minutes
     */
    void startPreGameTimer() {
        for (Map.Entry<String,ClientSpeaker> entry : speakers.entrySet()) {   // for every player in the lobby
            entry.getValue().tell("Game timer is on: 3 minutes before game starts");
        }
        preGameTimer.schedule(new StartGame(players, speakers, disconnectedPlayer, game, this), 60000);
    }

    /**
     * Reconnect to the lobby a previously connected player after his disconnection
     * @param username lobby.contains(username)
     */
    synchronized void reconnectPlayer(String username, ClientSpeaker newSpeaker) {
        speakers.replace(username, newSpeaker);
        disconnectedPlayer.remove(username);

        speakers.get(username).tell("Welcome back " + username);
        if (gameStarted)
            speakers.get(username).tell("Game is still going");
        else
            speakers.get(username).tell("No game going on");
    }

    public Lobby getLobby(){
        return this;
    }

    public void setWindowCard(String userName, String name){
        chooseWindowCardObserver.notify(userName, name);
    }

    public Map<String, Player> getPlayers () {
        return players;
    }

    public void moveDiceFromDraftToCard(String username, int index, int row, int col){
        List<Integer> listCoordinates = new ArrayList<>(); // 0: index, 1: row, 2: col
        listCoordinates.add(index);
        listCoordinates.add(row);
        listCoordinates.add(col);
        moveDiceObserver.update(players.get(username), listCoordinates);
    }

    public Game getGame() {
        return game;
    }
}
