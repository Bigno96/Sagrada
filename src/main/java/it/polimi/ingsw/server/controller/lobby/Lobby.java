package it.polimi.ingsw.server.controller.lobby;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.game.ActionController;
import it.polimi.ingsw.server.controller.game.GameController;
import it.polimi.ingsw.server.controller.game.RoundController;
import it.polimi.ingsw.server.controller.game.TimerTurnDaemon;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Lobby {
    public enum gameState { WAITING, STARTING, STARTED }
    private gameState currentState;

    private static final String WELCOME_USER_KEYWORD = "WELCOME_USER";
    private static final String WELCOME_BACK_KEYWORD = "WELCOME_BACK";
    private static final String USER_KEYWORD = "USER";
    private static final String CONNECTED_KEYWORD = "CONNECTED";
    private static final String RECONNECTED_KEYWORD = "RECONNECTED";
    private static final String DISCONNECTED_KEYWORD = "DISCONNECTED";
    private static final String REMOVED_KEYWORD = "REMOVED";
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

    private Game game;
    private GameController gameController;
    private RoundController roundController;
    private ActionController actionController;

    private final CommunicationParser protocol;
    private final GameSettingsParser settings;
    private final ViewMessageParser dictionary;

    public Lobby() {
        this.players = new HashMap<>();
        this.speakers = new HashMap<>();
        this.checkerDisconnection = new HashMap<>();
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
                    reconnectPlayer(username, speaker);
                else
                    throw new SamePlayerException(dictionary.getMessage(SAME_PLAYER_KEYWORD));

            else if (currentState.equals(gameState.STARTED))
                throw new GameAlreadyStartedException(dictionary.getMessage(GAME_ALREADY_STARTED_KEYWORD));

            else if (players.size() >= settings.getMaxPlayer())
                throw new TooManyPlayersException(dictionary.getMessage(TOO_MANY_PLAYERS_KEYWORD));

            else {
                Player p = new Player(username);
                players.put(username, p);
                speakers.put(username, speaker);

                Timer disconnection = new Timer();
                CheckDisconnectionDaemon daemon = new CheckDisconnectionDaemon(speaker, this, username);
                disconnection.scheduleAtFixedRate(daemon, 0, settings.getDaemonFrequency());
                checkerDisconnection.put(username, daemon);

                speaker.loginSuccess(dictionary.getMessage(WELCOME_USER_KEYWORD) + username);
                speaker.tell(dictionary.getMessage(GAME_WILL_START_KEYWORD));

                if (currentState.equals(gameState.STARTING)) {
                    game.addPlayer(p);
                }

                speakers.forEach((user, speak) -> {
                    if (!user.equals(username))
                        speak.tell(dictionary.getMessage(USER_KEYWORD) + username + dictionary.getMessage(CONNECTED_KEYWORD));
                });
            }
        }
    }

    /**
     * Disconnect player. Internal handling of different gameState cases.
     * @param username disconnected.
     */
    public void disconnectPlayer(String username) {
        synchronized (playersLock) {

            if (players.containsKey(username)) {

                speakers.forEach((user, speak) -> {
                    if (!user.equals(username))
                        speak.tell(dictionary.getMessage(USER_KEYWORD) + username + dictionary.getMessage(DISCONNECTED_KEYWORD));
                });

                if (currentState.equals(gameState.STARTED)) {
                    players.get(username).setDisconnected(true);        // just set Disconnected state on player

                } else
                    removePlayer(username);
            }
        }
    }

    /**
     * Reconnect player disconnected when gameState is STARTING or STARTED.
     * @param username reconnected.
     */
    public void reconnectPlayer(String username, ClientSpeaker speaker) {
        speakers.replace(username, speaker);
        players.get(username).setDisconnected(false);
        speakers.get(username).tell(dictionary.getMessage(WELCOME_BACK_KEYWORD) + username);

        speakers.forEach((user, speak) -> {
            if (!user.equals(username))
                speak.tell(dictionary.getMessage(USER_KEYWORD) + username + dictionary.getMessage(RECONNECTED_KEYWORD));
        });

        if (currentState.equals(gameState.STARTED))
            if (players.get(username).getWindowCard() == null)
                speakers.get(username).sendWindowCard(gameController.getWindowAlternatives().get(username));
            else
                speakers.get(username).nextTurn(game.getCurrentPlayer().getId());
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

        speakers.forEach((user, speak) ->
            speak.tell(dictionary.getMessage(USER_KEYWORD) + username + dictionary.getMessage(REMOVED_KEYWORD)));
    }

    /**
     * Sets up GAME_IS_STARTING_TIMER and add each player to the instance of game.
     */
    public void startingGame() {
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
    public void startGame() {
        notifyAllPlayers(dictionary.getMessage(GAME_STARTED_KEYWORD));
        currentState = gameState.STARTED;
        actionController = new ActionController(this, game);
        gameController = new GameController(this, players);
        gameController.startGame();

        Timer ending = new Timer();
        ending.scheduleAtFixedRate(new CheckEndGameDaemon(game, this), 0, settings.getDaemonFrequency());
    }

    /**
     * Start first round of the game
     */
    public void startCountingRound() {
        Timer timer = new Timer();
        TimerTurnDaemon timerTurn = new TimerTurnDaemon(this);
        roundController = new RoundController(this, game, timerTurn);

        try {
            game.getBoard().getDraft().fillDraft();
            game.getBoard().getDraft().rollDraft();

        } catch (EmptyException | IDNotFoundException e) {
            out.println(e.getMessage());
        }

        roundController.nextTurn();
        timer.scheduleAtFixedRate(timerTurn, 0, settings.getNotifyInterval());
    }

    /**
     * @param s String to be printed to all player not disconnected
     */
    public void notifyAllPlayers(String s) {
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
        if (game.getNumPlayer() == 1)
            notifyAllPlayers(dictionary.getMessage(WIN_MSG_KEYWORD));

        SortedMap<Integer, String> ranking = new TreeMap<>();
        players.values().forEach(player -> {
            try {
                ranking.put(player.rateScore(), player.getId());
            } catch (IDNotFoundException | PositionException e) {
                out.println(e.getMessage());
            }
        });

        speakers.values().forEach(speaker -> speaker.printRanking(ranking));
    }

    /**
     * @return game of this lobby
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return game Controller
     */
    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * @return round Controller
     */
    public RoundController getRoundController() {
        return this.roundController;
    }

    /**
     * @return action Controller
     */
    public ActionController getActionController() {
        return this.actionController;
    }

    /**
     * @return hash map of players
     */
    public Map<String, Player> getPlayers() {
        return players;
    }

    /**
     * @return hash map of client speakers
     */
    public Map<String, ClientSpeaker> getSpeakers() {
        return speakers;
    }

    /**
     * Set current state
     * @param state to be set
     */
    public void setState(gameState state) {
        currentState = state;
    }

    /**
     * @return current stat of the game
     */
    public gameState getState() {
        return currentState;
    }
}



