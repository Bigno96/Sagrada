package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Run during player's turn and waiting
 */
public class MenuTask implements Runnable {

    private static final String MENU_PLAYING_KEYWORD = "MENU_PLAYING_MESSAGE";
    private static final String MENU_WAITING_MESSAGE_KEYWORD = "MENU_WAITING_MESSAGE";

    private static final String PLACE_DICE_ENTRY_KEYWORD = "PLACE_DICE_ENTRY";
    private static final String PLACE_DICE_MESSAGE_KEYWORD = "PLACE_DICE_MESSAGE";

    private static final String USE_TOOL_CARD_ENTRY_KEYWORD = "USE_TOOL_CARD_ENTRY";
    private static final String USE_TOOL_CARD_MESSAGE_KEYWORD = "USE_TOOL_CARD_MESSAGE";

    private static final String PASS_TURN_ENTRY_KEYWORD = "PASS_TURN_ENTRY";
    private static final String PASS_TURN_MESSAGE_KEYWORD = "PASS_TURN_MESSAGE";

    private static final String INCORRECT_MESSAGE_KEYWORD = "INCORRECT_MESSAGE";
    private static final String ALREADY_DONE_KEYWORD = "ALREADY_DONE";
    private static final String PASSED_KEYWORD = "PASSED";

    private static final String OWN_WINDOW_CARD_ENTRY_KEYWORD = "OWN_WINDOW_CARD_ENTRY";
    private static final String OWN_WINDOW_CARD_MESSAGE_KEYWORD = "OWN_WINDOW_CARD_MESSAGE";

    private static final String ANOTHER_WINDOW_CARD_ENTRY_KEYWORD = "ANOTHER_WINDOW_CARD_ENTRY";
    private static final String ANOTHER_WINDOW_CARD_MESSAGE_KEYWORD = "ANOTHER_WINDOW_CARD_MESSAGE";
    private static final String ASK_USER_KEYWORD = "ASK_USER";

    private static final String DRAFT_ENTRY_KEYWORD = "DRAFT_ENTRY";
    private static final String DRAFT_MESSAGE_KEYWORD = "DRAFT_MESSAGE";

    private static final String PUBLIC_OBJECTIVE_ENTRY_KEYWORD = "PUBLIC_OBJECTIVE_ENTRY";
    private static final String PUBLIC_OBJECTIVE_MESSAGE_KEYWORD = "PUBLIC_OBJECTIVE_MESSAGE";

    private static final String PRIVATE_OBJECTIVE_ENTRY_KEYWORD = "PRIVATE_OBJECTIVE_ENTRY";
    private static final String PRIVATE_OBJECTIVE_MESSAGE_KEYWORD = "PRIVATE_OBJECTIVE_MESSAGE";

    private static final String ROUND_TRACK_ENTRY_KEYWORD = "ROUND_TRACK_ENTRY";
    private static final String ROUND_TRACK_MESSAGE_KEYWORD = "ROUND_TRACK_MESSAGE";

    private static final String TOOL_CARD_ENTRY_KEYWORD = "TOOL_CARD_ENTRY";
    private static final String TOOL_CARD_MESSAGE_KEYWORD = "TOOL_CARD_MESSAGE";

    private static final String FAVOR_POINT_ENTRY_KEYWORD = "FAVOR_POINT_ENTRY";
    private static final String FAVOR_POINT_MESSAGE_KEYWORD = "FAVOR_POINT_MESSAGE";

    private final HashMap<String, Consumer<String>> waitingAction;
    private final HashMap<String, Consumer<String>> playingAction;
    private HashMap<String, Consumer<String>> action;

    private final Scanner inKeyboard;
    private final CliSystem cliSystem;
    private ServerSpeaker serverSpeaker;
    private ViewMessageParser dictionary;

    public enum state { PLAYING, PASSING, MOVED, USED, EXIT }
    private List<state> currentState;

    MenuTask(CliSystem cliSystem) {
        this.currentState = new ArrayList<>();
        this.cliSystem = cliSystem;

        this.inKeyboard = new Scanner(System.in);
        this.serverSpeaker = cliSystem.getServerSpeaker();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        waitingAction = new HashMap<>();
        playingAction = new HashMap<>();
        action = new HashMap<>();

        mapWaitingAction();
        mapPlayingAction();
    }

    /**
     * Used to set if playing or not
     * @param playing to be set
     */
    void setPlaying(Boolean playing) {
        if (playing) {
            currentState.add(state.PLAYING);
            action = playingAction;
        }
        else {
            currentState.remove(state.PLAYING);
            action = waitingAction;
        }

        printMenu();
    }

    /**
     * Ask and shows which moves user can make
     */
    @Override
    public void run() {
        while (!currentState.contains(state.EXIT)) {

            synchronized (this) {
                String s = inKeyboard.nextLine();

                if (!action.containsKey(s)) {
                    cliSystem.print(dictionary.getMessage(INCORRECT_MESSAGE_KEYWORD));
                    cliSystem.drainPermits();
                }
                else {
                    action.get(s).accept(cliSystem.getUserName());

                    cliSystem.acquireSemaphore();                           // acquire before re printing menu, waiting for Action to happen
                    cliSystem.drainPermits();
                }

                if (!currentState.contains(state.PASSING))
                    printMenu();
            }
        }
    }

    /**
     * Print menu
     */
    private void printMenu() {
        if (currentState.contains(state.PLAYING)) {
            cliSystem.print(dictionary.getMessage(MENU_PLAYING_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(PLACE_DICE_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(USE_TOOL_CARD_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(PASS_TURN_MESSAGE_KEYWORD));

        } else
        cliSystem.print(dictionary.getMessage(MENU_WAITING_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(OWN_WINDOW_CARD_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(ANOTHER_WINDOW_CARD_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(DRAFT_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(DRAFT_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(PUBLIC_OBJECTIVE_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(PRIVATE_OBJECTIVE_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(ROUND_TRACK_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(ROUND_TRACK_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(TOOL_CARD_MESSAGE_KEYWORD));

        cliSystem.print("'" + dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD) + "' " +
                dictionary.getMessage(FAVOR_POINT_MESSAGE_KEYWORD));
    }

    /**
     * Maps hash map for playing choices
     */
    private void mapPlayingAction() {
        Consumer<String> move = string -> {       // place a dice
            if (!currentState.contains(state.MOVED)) {
                cliSystem.moveDice();
            } else {
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
                cliSystem.releaseSemaphore();            // releasing for menuTask action.accept()
            }
        };

        Consumer<String> use = string -> {         // use a tool card
            if (!currentState.contains(state.USED)) {
                cliSystem.useToolCard();
            } else {
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
                cliSystem.releaseSemaphore();            // releasing for menuTask action.accept()
            }
        };

        Consumer<String> pass = string -> {       // end turn
            currentState.add(state.PASSING);
            currentState.remove(state.MOVED);
            currentState.remove(state.USED);

            cliSystem.print(dictionary.getMessage(PASSED_KEYWORD));
            serverSpeaker.endTurn(cliSystem.getUserName());
        };

        Consumer<String> window = username ->                          //see personal window card
            serverSpeaker.askWindowCard(username, username);

        Consumer<String> other = username -> {                          //see other player window card
            cliSystem.print(dictionary.getMessage(ASK_USER_KEYWORD));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username);
        };

        //see draft
        Consumer<String> draft = serverSpeaker::askDraft;

        //see public objective
        Consumer<String> publicObj = serverSpeaker::askPublicObj;

        //see private objective
        Consumer<String> privateObj = serverSpeaker::askPrivateObj;

        //see round track
        Consumer<String> roundTrack = serverSpeaker::askRoundTrack;

        //see tool card
        Consumer<String> tool = serverSpeaker::askToolCards;

        //see favor points
        Consumer<String> favor = serverSpeaker::askFavorPoints;

        playingAction.put(dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD), move);
        playingAction.put(dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD), use);
        playingAction.put(dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD), pass);
        playingAction.put(dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD), window);
        playingAction.put(dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD), other);
        playingAction.put(dictionary.getMessage(DRAFT_ENTRY_KEYWORD), draft);
        playingAction.put(dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD), publicObj);
        playingAction.put(dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD), privateObj);
        playingAction.put(dictionary.getMessage(ROUND_TRACK_ENTRY_KEYWORD), roundTrack);
        playingAction.put(dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD), tool);
        playingAction.put(dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD), favor);
    }

    /**
     * Maps hash map for waiting choices
     */
    private void mapWaitingAction() {
        Consumer<String> window = username ->                          //see personal window card
            serverSpeaker.askWindowCard(username, username);

        Consumer<String> other = username -> {                          //see other player window card
            cliSystem.print(dictionary.getMessage(ASK_USER_KEYWORD));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username);
        };

        //see draft
        Consumer<String> draft = serverSpeaker::askDraft;

        //see public objective
        Consumer<String> publicObj = serverSpeaker::askPublicObj;

        //see private objective
        Consumer<String> privateObj = serverSpeaker::askPrivateObj;

        //see round track
        Consumer<String> roundTrack = serverSpeaker::askRoundTrack;

        //see tool card
        Consumer<String> tool = serverSpeaker::askToolCards;

        //see favor points
        Consumer<String> favor = serverSpeaker::askFavorPoints;

        waitingAction.put(dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD), window);
        waitingAction.put(dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD), other);
        waitingAction.put(dictionary.getMessage(DRAFT_ENTRY_KEYWORD), draft);
        waitingAction.put(dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD), publicObj);
        waitingAction.put(dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD), privateObj);
        waitingAction.put(dictionary.getMessage(ROUND_TRACK_ENTRY_KEYWORD), roundTrack);
        waitingAction.put(dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD), tool);
        waitingAction.put(dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD), favor);
    }

    /**
     * Used to set moved parameter
     */
    void setMoved() {
        currentState.add(state.MOVED);
    }

    /**
     * Used to set used parameter
     */
    void setUsed() {
        currentState.add(state.USED);
    }

    /**
     * Used to clear currentState
     */
    void clearCurrentState() {
        currentState.clear();
    }
}
