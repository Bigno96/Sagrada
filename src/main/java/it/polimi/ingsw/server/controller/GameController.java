package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.WindowParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.controller.observer.ChoiceWindowCardObserver;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class GameController {

    private static final String INVALID_WINDOW_CARD = "Card selected is not valid, please select a valid one: ";
    private static final String ERROR_ASSIGNING_CARD = "Error in assigning window card to player: ";

    private Lobby lobby;
    private Game game;
    private HashMap<String, List<WindowCard>> windowAlternatives;
    private HashMap<String, Player> players;
    private WindowParser windowParser;
    private static final Random random = new Random();

    public GameController(Lobby lobby, Map<String, Player> players) {
        this.lobby = lobby;
        this.game = lobby.getGame();
        this.windowAlternatives = new HashMap<>();
        this.players = (HashMap<String, Player>) players;
        this.windowParser = (WindowParser) ParserManager.getWindowCardParser();
    }

    public void startGame() {
        List<Integer> used = new ArrayList<>();

        game.startGame();
        setObserver();
        getWindow(used);
        chooseCard();
    }

    private List<Integer> createNRandom(int n, List<Integer> used, int minBoundInclusive, int maxBoundExclusive) {
        List<Integer> ret = new ArrayList<>();
        do {
            int rand = random.nextInt(maxBoundExclusive-minBoundInclusive)+minBoundInclusive;
            if (!ret.contains(rand) && !used.contains(rand))
                ret.add(rand);

        } while (ret.size()<n);

        return ret;
    }

    private void setObserver() {
        Consumer<Map.Entry<String, Player>> chooseCard = entry ->
                entry.getValue().addObserver(new ChoiceWindowCardObserver(lobby));

        players.entrySet().parallelStream().forEach(chooseCard);
    }

    private void chooseCard() {
        Consumer<Map.Entry<String, ClientSpeaker>> chooseCard = entry ->
            entry.getValue().chooseWindowCard(windowAlternatives.get(entry.getKey()));

        lobby.getSpeakers().entrySet().parallelStream().forEach(chooseCard);
    }

    private void getWindow(List<Integer> used) {
        BiConsumer<String, Player> getWindow = (name, player) -> {
            try{
                List<Integer> nRand = createNRandom(2, used, 1, 13);
                List<WindowCard> cards = new ArrayList<>(windowParser.getWindow(nRand.get(0), nRand.get(1)));
                windowAlternatives.put(name, cards);
                used.addAll(nRand);

            } catch (FileNotFoundException | IDNotFoundException | ValueException | PositionException e) {
                out.println(e.getMessage());
            }
        };

        players.forEach(getWindow);
    }

    private boolean isCardIn(String username, String cardName) {
        return windowAlternatives.get(username).stream().map(WindowCard::getName).collect(Collectors.toList()).contains(cardName);
    }

    public void setWindow(String username, String cardName) {
        if (!isCardIn(username, cardName)) {
            lobby.getSpeakers().get(username).tell(INVALID_WINDOW_CARD);
            lobby.getSpeakers().get(username).chooseWindowCard(windowAlternatives.get(username));
        }

        try {
            Optional<WindowCard> chosen = windowAlternatives.get(username).stream().filter(card -> card.getName().equals(cardName)).findAny();
            if (chosen.isPresent())
                game.findPlayer(username).setWindowCard(chosen.get());
            else
                out.println(ERROR_ASSIGNING_CARD + username);

        } catch (PlayerNotFoundException | EmptyException e) {
            out.println(e.getMessage());
        }
    }
}
