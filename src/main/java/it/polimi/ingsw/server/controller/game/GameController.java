package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PrivateObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.ToolCardParser;
import it.polimi.ingsw.parser.gamedataparser.WindowParser;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.controller.observer.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * Controls game aspects
 */
public class GameController {

    private static final String INVALID_WINDOW_CARD = "Card selected is not valid, please select a valid one: ";
    private static final String ERROR_ASSIGNING_CARD = "Error in assigning window card to player: ";
    private static final int MIN_BOUND_INCLUSIVE = 1;

    private Lobby lobby;
    private Game game;
    private HashMap<String, List<WindowCard>> windowAlternatives;
    private HashMap<String, Player> players;

    private final GameSettingsParser gameSettings;
    private final WindowParser windowParser;
    private static final Random random = new Random();

    public GameController(Lobby lobby, Map<String, Player> players) {
        this.lobby = lobby;
        this.game = lobby.getGame();
        this.windowAlternatives = new HashMap<>();
        this.players = (HashMap<String, Player>) players;

        this.gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
        this.windowParser = (WindowParser) ParserManager.getWindowCardParser();
    }

    /**
     * Starts game setting up window cards, objective cards and tool cards
     */
    public void startGame() {
        List<Integer> used = new ArrayList<>();

        game.startGame();
        setObserver();
        getListWindowCard(used);
        chooseCard();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new AllCardSelectedDaemon(lobby, this), 0, gameSettings.getNotifyInterval());

        try {
            while (!allCardsAreSelected())
                synchronized (this) {
                    wait();
                }

        } catch (InterruptedException e) {
            out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }

        used.clear();
        setPublicObjective(used);

        used.clear();
        setPrivateObjective(used);

        used.clear();
        setToolCard(used);

        lobby.startCountingRound();
    }

    /**
     * @return true if all player have selected their window card, false else
     */
    public Boolean allCardsAreSelected() {
        return lobby.getPlayers().entrySet().stream()
                .noneMatch(entry -> entry.getValue().getWindowCard() == null);
    }

    /**
     * Used to generate a random int pool of various length n and with a determined range, excluding already used member
     * @param n length of int pool to be returned
     * @param used collections of integer to exclude from returning pool
     * @param maxBoundExclusive upper limit to random generation pool. Lower limit always set to 1 included.
     * @return List<Integer> of random in with specified parameter. All member different.
     */
    public List<Integer> createNRandom(int n, List<Integer> used, int maxBoundExclusive) {
        List<Integer> ret = new ArrayList<>();
        do {
            int rand = random.nextInt(maxBoundExclusive-MIN_BOUND_INCLUSIVE)+MIN_BOUND_INCLUSIVE;
            if (!ret.contains(rand) && !used.contains(rand))
                ret.add(rand);

        } while (ret.size()<n);

        return ret;
    }

    /**
     * Attach observers where needed
     */
    public void setObserver() {
        Consumer<Map.Entry<String, Player>> attachObserver = entry -> {
            entry.getValue().addObserver(new ChoiceWindowCardObserver(lobby));
            entry.getValue().addObserver(new SetObjectiveObserver(lobby));
        };

        players.entrySet().parallelStream().forEach(attachObserver);
        game.addObserver(new TurnObserver(lobby));
        game.getBoard().addObserver(new SetObjectiveObserver(lobby));
        game.getBoard().addObserver(new SetToolCardObserver(lobby));
        game.getBoard().getRoundTrack().addObserver(new RoundTrackObserver(lobby));
        game.getBoard().getDraft().addObserver(new DraftObserver(lobby));
    }

    /**
     * Send each player a pool of 4 window cards selected for him
     */
    private void chooseCard() {
        Consumer<Map.Entry<String, ClientSpeaker>> chooseCard = entry ->
            entry.getValue().sendWindowCard(windowAlternatives.get(entry.getKey()));

        lobby.getSpeakers().entrySet().parallelStream().forEach(chooseCard);
    }

    /**
     * Generate pool of 4 different window card for each player. No cards repetition.
     * @param used list of cards id already used to avoid duplicating
     */
    public void getListWindowCard(List<Integer> used) {
        BiConsumer<String, Player> getWindows = (name, player) -> {
            try{
                List<Integer> nRand = createNRandom(2, used,  13);
                List<WindowCard> cards = new ArrayList<>(windowParser.getWindow(nRand.get(0), nRand.get(1)));
                windowAlternatives.put(name, cards);
                used.addAll(nRand);

            } catch (FileNotFoundException | IDNotFoundException | ValueException | PositionException e) {
                out.println(e.getMessage());
            }
        };

        players.forEach(getWindows);
    }

    /**
     * @return window alternatives for each player
     */
    public Map<String, List<WindowCard>> getWindowAlternatives() {
        return this.windowAlternatives;
    }

    /**
     * Checks if window card selected by player was from his selection's pool
     * @param username = Player.getId()
     * @param cardName of the card chosen by player
     * @return true if windowAlternatives.get(username).contains(cardName), false else
     */
    public boolean isCardValidForPlayer(String username, String cardName) {
        return windowAlternatives.get(username).stream().map(WindowCard::getName).collect(Collectors.toList()).contains(cardName);
    }

    /**
     * Set window card of a specific player
     * @param username of player who wants to set his window card
     * @param cardName of card to be set
     */
    public void setWindowCard(String username, String cardName) {
        if (!isCardValidForPlayer(username, cardName)) {
            lobby.getSpeakers().get(username).tell(INVALID_WINDOW_CARD);
            lobby.getSpeakers().get(username).sendWindowCard(windowAlternatives.get(username));
        }

        try {
            Optional<WindowCard> chosen = windowAlternatives.get(username).stream().filter(card -> card.getName().equals(cardName)).findAny();
            if (chosen.isPresent())
                synchronized (this) {
                    chosen.get().addObserver(new SetDiceObserver(lobby));
                    game.findPlayer(username).setWindowCard(chosen.get());
                    notifyAll();
                }
            else
                out.println(ERROR_ASSIGNING_CARD + username);

        } catch (PlayerNotFoundException | EmptyException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * Set game's public objective generating 3 random id and informs all players
     * @param used list of integer to avoid duplicating
     */
    public void setPublicObjective(List<Integer> used) {
        PublicObjectiveCardParser cardParser = (PublicObjectiveCardParser) ParserManager.getPublicCardParser();

        List<Integer> nRand = createNRandom(3, used, 11);

        List<ObjectiveCard> cards = new ArrayList<>();

        nRand.forEach(integer -> {
            try {
                cards.add(cardParser.makeObjectiveCard(integer));
            } catch (FileNotFoundException | IDNotFoundException e) {
                out.println(e.getMessage());
            }
        });

        game.getBoard().setPublicObj(cards.get(0), cards.get(1), cards.get(2));
    }

    /**
     * Set each player's private objective and inform his owner.
     * @param used list of integer to avoid duplicating
     */
    public void setPrivateObjective(List<Integer> used) {
        PrivateObjectiveCardParser cardParser = (PrivateObjectiveCardParser) ParserManager.getPrivateCardParser();

        List<Integer> nRand = createNRandom(game.getNumPlayer(), used, 6);

        HashMap<Integer, String> mapPrivateCard = new HashMap<>();
        nRand.forEach(integer ->
                    lobby.getPlayers().keySet().stream()
                    .filter(user -> !mapPrivateCard.containsValue(user))
                    .findFirst()
                    .ifPresent(user -> mapPrivateCard.put(integer, user)));

        mapPrivateCard.forEach((integer, user) -> {
            try {
                game.findPlayer(user).setPrivateObj(cardParser.makeObjectiveCard(integer));
            } catch (PlayerNotFoundException | EmptyException | FileNotFoundException | IDNotFoundException e) {
                out.println(e.getMessage());
            }
        });
    }

    /**
     * Set game's tool card and informs all players
     * @param used list of integer to avoid duplicating
     */
    public void setToolCard(List<Integer> used) {
        ToolCardParser cardParser = (ToolCardParser) ParserManager.getToolCardParser();

        List<Integer> nRand = createNRandom(3, used, 13);

        List<ToolCard> tools = new ArrayList<>();

        nRand.forEach(integer -> {
            try {
                tools.add(cardParser.makeToolCard(integer, game));
            } catch (FileNotFoundException e) {
                out.println(e.getMessage());
            }
        });

        tools.forEach(toolCard -> toolCard.addObserver(new UseToolObserver(lobby)));
        game.getBoard().setToolCard(tools.get(0), tools.get(1), tools.get(2));
    }
}
