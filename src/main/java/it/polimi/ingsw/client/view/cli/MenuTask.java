package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.HashMap;
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

    private static final String TOOL_CARD_ENTRY_KEYWORD = "TOOL_CARD_ENTRY";
    private static final String TOOL_CARD_MESSAGE_KEYWORD = "TOOL_CARD_MESSAGE";

    private static final String FAVOR_POINT_ENTRY_KEYWORD = "FAVOR_POINT_ENTRY";
    private static final String FAVOR_POINT_MESSAGE_KEYWORD = "FAVOR_POINT_MESSAGE";

    private Boolean playing;

    private final HashMap<String, Consumer<String>> waitingAction;
    private final HashMap<String, Consumer<String>> playingAction;
    private final Scanner inKeyboard;
    private ServerSpeaker serverSpeaker;
    private ViewMessageParser dictionary;

    private CliSystem cliSystem;
    private boolean played;
    private boolean moved;
    private boolean used;

    MenuTask(CliSystem cliSystem) {
        this.cliSystem = cliSystem;
        this.inKeyboard = new Scanner(System.in);
        this.serverSpeaker = cliSystem.getServerSpeaker();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        waitingAction = new HashMap<>();
        playingAction = new HashMap<>();
        mapWaitingAction();
        mapPlayingAction();
    }

    /**
     * Used to set if playing or not
     * @param playing to be set
     */
    void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    /**
     * Ask and shows which moves user can make
     */
    @Override
    public void run() {
        played = false;
        moved = false;
        used = false;

        HashMap<String, Consumer<String>> action;
        if (playing)
            action = playingAction;
        else
            action = waitingAction;

        do {
            if (playing)
                cliSystem.print(dictionary.getMessage(MENU_PLAYING_KEYWORD));
            else
                cliSystem.print(dictionary.getMessage(MENU_WAITING_MESSAGE_KEYWORD));

            if (playing) {
                cliSystem.print("'" + dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD) + "' " +
                        dictionary.getMessage(PLACE_DICE_MESSAGE_KEYWORD));

                cliSystem.print("'" + dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD) + "' " +
                        dictionary.getMessage(USE_TOOL_CARD_MESSAGE_KEYWORD));

                cliSystem.print("'" + dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD) + "' " +
                        dictionary.getMessage(PASS_TURN_MESSAGE_KEYWORD));
            }

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

            cliSystem.print("'" + dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(TOOL_CARD_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(FAVOR_POINT_MESSAGE_KEYWORD));

            String s = inKeyboard.nextLine();

            if (!action.containsKey(s))
                cliSystem.print(dictionary.getMessage(INCORRECT_MESSAGE_KEYWORD));
            else {
                action.get(s).accept(cliSystem.getUserName());
                //cliSystem.acquireSemaphore();                           // acquire before re printing menu, waiting for waitingAction to happen
            }

        } while (!played);

        serverSpeaker.endTurn(cliSystem.getUserName());
    }

    /**
     * Maps hash map for playing choices
     */
    private void mapPlayingAction() {
        Consumer<String> move = string -> {       // place a dice
            if (!moved) {
                cliSystem.moveDice();
                cliSystem.releaseSemaphore();           // release for playingAction.accept
            } else
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
        };

        Consumer<String> use = string -> {         // use a tool card
            if (!used) {
                cliSystem.useToolCard();
                cliSystem.releaseSemaphore();           // release for playingAction.accept
            } else
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
        };

        Consumer<String> pass = string -> {       // end turn
            played = true;
            cliSystem.print(dictionary.getMessage(PASSED_KEYWORD));
            cliSystem.releaseSemaphore();               // release for playingAction.accept
        };

        Consumer<String> window = username -> {                         //see personal window card
            serverSpeaker.askWindowCard(username, username);
            cliSystem.acquireSemaphore();           // acquire for print window card
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> other = username -> {                          //see other player window card
            cliSystem.print(dictionary.getMessage(ASK_USER_KEYWORD));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username);
            cliSystem.acquireSemaphore();           // acquire for print window card
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> draft = username -> {                            //see draft
            serverSpeaker.askDraft(username);
            cliSystem.acquireSemaphore();           // acquire for show draft
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> publicObj = username -> {              //see public objective
            serverSpeaker.askPublicObj(username);
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> privateObj = username -> {             //see private objective
            serverSpeaker.askPrivateObj(username);
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> tool = username -> {                   //see tool card
            serverSpeaker.askToolCards(username);
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        Consumer<String> favor = username -> {                  //see favor points
            serverSpeaker.askFavorPoints(username);
            cliSystem.releaseSemaphore();           // release for playingAction.accept
        };

        playingAction.put(dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD), move);
        playingAction.put(dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD), use);
        playingAction.put(dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD), pass);
        playingAction.put(dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD), window);
        playingAction.put(dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD), other);
        playingAction.put(dictionary.getMessage(DRAFT_ENTRY_KEYWORD), draft);
        playingAction.put(dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD), publicObj);
        playingAction.put(dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD), privateObj);
        playingAction.put(dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD), tool);
        playingAction.put(dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD), favor);
    }

    /**
     * Maps hash map for waiting choices
     */
    private void mapWaitingAction() {
        Consumer<String> window = username -> {                         //see personal window card
            serverSpeaker.askWindowCard(username, username);
            cliSystem.acquireSemaphore();           // acquire for print window card
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> other = username -> {                          //see other player window card
            cliSystem.print(dictionary.getMessage(ASK_USER_KEYWORD));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username);
            cliSystem.acquireSemaphore();           // acquire for print window card
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> draft = username -> {                            //see draft
            serverSpeaker.askDraft(username);
            cliSystem.acquireSemaphore();           // acquire for show draft
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> publicObj = username -> {              //see public objective
            serverSpeaker.askPublicObj(username);
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> privateObj = username -> {             //see private objective
            serverSpeaker.askPrivateObj(username);
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> tool = username -> {                   //see tool card
            serverSpeaker.askToolCards(username);
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        Consumer<String> favor = username -> {                  //see favor points
            serverSpeaker.askFavorPoints(username);
            cliSystem.releaseSemaphore();           // release for waitingAction.accept
        };

        waitingAction.put(dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD), window);
        waitingAction.put(dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD), other);
        waitingAction.put(dictionary.getMessage(DRAFT_ENTRY_KEYWORD), draft);
        waitingAction.put(dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD), publicObj);
        waitingAction.put(dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD), privateObj);
        waitingAction.put(dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD), tool);
        waitingAction.put(dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD), favor);
    }

    /**
     * Used to set moved parameter
     */
    void setMoved() {
        this.moved = true;
    }
}