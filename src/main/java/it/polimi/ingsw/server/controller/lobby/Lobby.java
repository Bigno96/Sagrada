package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.RoundController;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class Lobby {
    enum gameState { WAITING, STARTING, STARTED }
    private gameState currentState;

    private static final String WELCOME_USER_KEYWORD = "WELCOME_USER";
    private static final String WELCOME_BACK_KEYWORD = "WELCOME_BACK";
    private static final String GAME_WILL_START_KEYWORD = "GAME_WILL_START";
    private static final String REMOVED_USER_KEYWORD = "REMOVED_USER";
    private static final String GAME_STARTED_KEYWORD = "GAME_STARTED";
    private static final String WIN_MSG_KEYWORD = "WIN_MSG";

    private static final String SAME_PLAYER_KEYWORD = "SAME_PLAYER_MSG";
    private static final String GAME_ALREADY_STARTED_KEYWORD = "GAME_ALREADY_STARTED_MSG";
    private static final String TOO_MANY_PLAYERS_KEYWORD = "TOO_MANY_PLAYERS_MSG";

    private final Object playersLock = new Object();
    private final Object speakersLock = new Object();
    private final Object checkerLock = new Object();

    private HashMap<String, Player> players;
    private HashMap<String, ClientSpeaker> speakers;
    private HashMap<String, CheckDisconnectionDaemon> checkerDisconnection;
    private HashMap<String, RemovePlayerDaemon> checkerRemoving;

    private Game game;
    private GameController gameController;
    private RoundController roundController;

    private final CommunicationParser protocol;
    private final GameSettingsParser settings;
    private final ViewMessageParser dictionary;

    public Lobby() {
        this.players = new HashMap<>();
        this.speakers = new HashMap<>();
        this.checkerDisconnection = new HashMap<>();
        this.checkerRemoving = new HashMap<>();
        this.game = new Game();
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    public void startLobby() {
        currentState = gameState.WAITING;

        Timer starting = new Timer();
        starting.scheduleAtFixedRate(new CheckStartGameDaemon(players, this), 0, settings.getDaemonFrequency());
    }

    /**
     * Add player to the lobby.
     * @param username to add.
     * @param speaker of the player to add.
     * @throws SamePlayerException when a Player with the same username already logged in.
     * @throws GameAlreadyStartedException when a Player tries to log while a game is already started.
     * @throws TooManyPlayersException when a Player tries to log in when already 4 Players are logged.
     */
    public void addPlayer(String username, ClientSpeaker speaker) throws SamePlayerException, GameAlreadyStartedException, TooManyPlayersException {
        synchronized (playersLock) {

            if (players.containsKey(username))
                if (players.get(username).isDisconnected())
                    reconnectPlayer(username);
                else
                    throw new SamePlayerException(dictionary.getMessage(SAME_PLAYER_KEYWORD));

            else if (currentState.equals(gameState.STARTED))
                throw new GameAlreadyStartedException(dictionary.getMessage(GAME_ALREADY_STARTED_KEYWORD));

            else if (players.size() >= settings.getMaxPlayer())
                throw new TooManyPlayersException(dictionary.getMessage(TOO_MANY_PLAYERS_KEYWORD));

            players.put(username, new Player(username));
            speakers.put(username, speaker);

            Timer disconnection = new Timer();
            CheckDisconnectionDaemon daemon = new CheckDisconnectionDaemon(speaker, this, username);
            disconnection.scheduleAtFixedRate(daemon, 0, settings.getDaemonFrequency());
            checkerDisconnection.put(username, daemon);

            speaker.loginSuccess(dictionary.getMessage(WELCOME_USER_KEYWORD) + username);
            speaker.tell(dictionary.getMessage(GAME_WILL_START_KEYWORD));
        }
    }

    /**
     * Disconnect player. Internal handling of different gameState cases.
     * @param username disconnected.
     */
    public void disconnectPlayer(String username) {
        if(currentState.equals(gameState.WAITING)) {            // if gameState is WAITING, launch timer before removing
            Timer removing = new Timer();
            RemovePlayerDaemon daemon = new RemovePlayerDaemon(username);
            removing.schedule(daemon, settings.getRemovingTimer());
            checkerRemoving.put(username, daemon);

        } else
            players.get(username).setDisconnected(true);        // else, just set Disconnected state on player
    }

    /**
     * Reconnect player disconnected when gameState is STARTING or STARTED.
     * @param username reconnected.
     */
    public void reconnectPlayer(String username) {
        players.get(username).setDisconnected(false);
        speakers.get(username).tell(dictionary.getMessage(WELCOME_BACK_KEYWORD) + username);
    }

    /**
     * Stops REMOVING_PLAYER_TIMER of a player that re-enter a lobby in WAITING state.
     * @param username reconnected.
     */
    public void purgeRemoving(String username) {
        synchronized (checkerLock) {
            if (checkerRemoving.containsKey(username))
                checkerRemoving.get(username).cancel();
        }
    }

    /**
     * Removes completely player from lobby and from game.
     * @param username to be removed from game.
     */
    public void removePlayer(String username) {
        synchronized (playersLock) {
            if (!currentState.equals(gameState.WAITING)) {      // if gameState is not WAITING, player is inside a game
                try {
                    game.rmPlayer(players.get(username));       // remove him
                } catch (EmptyException e) {
                    out.println(e.getMessage());
                }
            }
        }

        synchronized (checkerLock) {                            // remove from all lobby's maps
            checkerDisconnection.get(username).cancel();
            checkerDisconnection.remove(username);
        }
        speakers.remove(username);
        players.remove(username);

        out.println(protocol.getMessage(REMOVED_USER_KEYWORD) + username);
    }

    /**
     * Run when a player disconnects while game is still in gameState.WAITING, after REMOVING_PLAYER_TIMER seconds.
     * Removes his username from players in the lobby list.
     */
    public class RemovePlayerDaemon extends TimerTask {

        private String username;

        private RemovePlayerDaemon(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            removePlayer(username);
        }
    }

    /**
     * Sets up GAME_IS_STARTING_TIMER and add each player to the instance of game.
     */
    void startingGame() {
        synchronized (playersLock) {
            players.forEach((key, value) -> {
                try {
                    game.addPlayer(value);
                } catch (SamePlayerException e) {
                    out.println(e.getMessage());
                }
            });
        }

        currentState = gameState.STARTING;

        Timer start = new Timer();
        start.scheduleAtFixedRate(new StartGame(this), 0, settings.getGameNotifyInterval());
    }

    /**
     * Notify all players that game is started. Then invokes method startGame of game and sets up the check end game daemon.
     */
    void startGame() {
        notifyAllPlayers(dictionary.getMessage(GAME_STARTED_KEYWORD));
        currentState = gameState.STARTED;
        gameController = new GameController(this, players);
        gameController.startGame();

        Timer ending = new Timer();
        ending.scheduleAtFixedRate(new CheckEndGameDaemon(game, this), 0, settings.getDaemonFrequency());
    }

    public void startCountingRound() {
        roundController = new RoundController(game);
        game.getBoard().getDraft().rollDraft();
        roundController.nextTurn();
    }

    /**
     * @param s String to be printed to all player not disconnected
     */
    void notifyAllPlayers(String s) {
        synchronized (playersLock) {
            synchronized (speakersLock) {
                players.entrySet().stream()
                    .filter(entry -> !entry.getValue().isDisconnected())        // filter only connected player
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList())
                    .forEach(key -> speakers.get(key).tell(s));                 // tell them
            }
        }
    }

    /**
     * Ends the game notifying victory.
     */
    public void endGame() {
        if (game.getNPlayer() == 1)
            notifyAllPlayers(dictionary.getMessage(WIN_MSG_KEYWORD));
    }

    public Game getGame() {
        return game;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public RoundController getRoundController() {
        return this.roundController;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, ClientSpeaker> getSpeakers() {
        return speakers;
    }
}



