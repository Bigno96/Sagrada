package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.gamedataparser.PrivateObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.WindowParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.controller.observer.ChoiceWindowCardObserver;
import it.polimi.ingsw.server.controller.observer.SetObjectiveObserver;
import it.polimi.ingsw.server.controller.observer.TurnObserver;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
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
    private static final int MIN_BOUND_INCLUSIVE = 1;

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
        getListWindowCard(used);
        chooseCard();

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

        lobby.startCountingRound();
    }

    private Boolean allCardsAreSelected() {
        return lobby.getPlayers().entrySet().stream()
                .noneMatch(entry -> entry.getValue().getWindowCard() == null);
    }

    private List<Integer> createNRandom(int n, List<Integer> used, int maxBoundExclusive) {
        List<Integer> ret = new ArrayList<>();
        do {
            int rand = random.nextInt(maxBoundExclusive-MIN_BOUND_INCLUSIVE)+MIN_BOUND_INCLUSIVE;
            if (!ret.contains(rand) && !used.contains(rand))
                ret.add(rand);

        } while (ret.size()<n);

        return ret;
    }

    private void setObserver() {
        Consumer<Map.Entry<String, Player>> attachObserver = entry -> {
            entry.getValue().addObserver(new ChoiceWindowCardObserver(lobby));
            entry.getValue().addObserver(new SetObjectiveObserver(lobby));
        };

        players.entrySet().parallelStream().forEach(attachObserver);
        game.addObserver(new TurnObserver(lobby));
        game.getBoard().addObserver(new SetObjectiveObserver(lobby));
    }

    private void chooseCard() {
        Consumer<Map.Entry<String, ClientSpeaker>> chooseCard = entry ->
            entry.getValue().chooseWindowCard(windowAlternatives.get(entry.getKey()));

        lobby.getSpeakers().entrySet().parallelStream().forEach(chooseCard);
    }

    private void getListWindowCard(List<Integer> used) {
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

    private boolean isCardValidForPlayer(String username, String cardName) {
        return windowAlternatives.get(username).stream().map(WindowCard::getName).collect(Collectors.toList()).contains(cardName);
    }

    public void setWindowCard(String username, String cardName) {
        if (!isCardValidForPlayer(username, cardName)) {
            lobby.getSpeakers().get(username).tell(INVALID_WINDOW_CARD);
            lobby.getSpeakers().get(username).chooseWindowCard(windowAlternatives.get(username));
        }

        try {
            Optional<WindowCard> chosen = windowAlternatives.get(username).stream().filter(card -> card.getName().equals(cardName)).findAny();
            if (chosen.isPresent()) {
                synchronized (this) {
                    game.findPlayer(username).setWindowCard(chosen.get());
                    notifyAll();
                }
            }
            else
                out.println(ERROR_ASSIGNING_CARD + username);

        } catch (PlayerNotFoundException | EmptyException e) {
            out.println(e.getMessage());
        }
    }

    private void setPublicObjective(List<Integer> used) {
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

        game.getBoard().setPublObj(cards.get(0), cards.get(1), cards.get(2));
    }

    private void setPrivateObjective(List<Integer> used) {
        PrivateObjectiveCardParser cardParser = (PrivateObjectiveCardParser) ParserManager.getPrivateCardParser();

        List<Integer> nRand = createNRandom(game.getNPlayer(), used, 6);

        HashMap<Integer, String> mapPrivateCard = new HashMap<>();
        nRand.forEach(integer ->
                    lobby.getPlayers().keySet().stream()
                    .filter(user -> !mapPrivateCard.containsValue(user))
                    .findFirst()
                    .ifPresent(user -> mapPrivateCard.put(integer, user)));

        mapPrivateCard.forEach((integer, user) -> {
            try {
                game.findPlayer(user).setPrivObj(cardParser.makeObjectiveCard(integer));
            } catch (PlayerNotFoundException | EmptyException | FileNotFoundException | IDNotFoundException e) {
                out.println(e.getMessage());
            }
        });
    }
}
