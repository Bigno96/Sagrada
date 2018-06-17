package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Run when player is waiting for his turn
 */
public class WaitingMenuTask implements Runnable {

    private static final String MENU_WAITING_MESSAGE_KEYWORD = "MENU_WAITING_MESSAGE";

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

    private static final String INCORRECT_MESSAGE_KEYWORD = "INCORRECT_MESSAGE";

    private CliSystem cliSystem;
    private boolean waiting;

    private final HashMap<String, Consumer<String>> waitingAction;
    private final Scanner inKeyboard;
    private final ServerSpeaker serverSpeaker;
    private final ViewMessageParser dictionary;

    WaitingMenuTask(CliSystem cliSystem) {
        this.inKeyboard = new Scanner(System.in);
        this.waiting = true;
        this.serverSpeaker = cliSystem.getServerSpeaker();
        this.dictionary = cliSystem.getDictionary();

        this.cliSystem = cliSystem;
        this.waitingAction = new HashMap<>();
        mapWaiting();
    }

    /**
     * Ask and shows which actions user can make
     */
    @Override
    public void run() {
        while (waiting) {
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

            cliSystem.print("'" + dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD) + "' " +
                            dictionary.getMessage(TOOL_CARD_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD) + "' " +
                            dictionary.getMessage(FAVOR_POINT_MESSAGE_KEYWORD));

            String s = inKeyboard.nextLine();

            if (!waitingAction.containsKey(s))
                cliSystem.print(dictionary.getMessage(INCORRECT_MESSAGE_KEYWORD));
            else
                waitingAction.get(s).accept(cliSystem.getUserName());
        }
    }

    /**
     * Maps hash map for waiting choices
     */
    private void mapWaiting() {
        Consumer<String> window = username -> serverSpeaker.askWindowCard(username, username); //see personal window card

        Consumer<String> other = username -> {
            //see other player window card
            cliSystem.print(dictionary.getMessage(ASK_USER_KEYWORD));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username);
        };

        Consumer<String> draft = serverSpeaker::askDraft;               //see draft

        Consumer<String> publicObj = serverSpeaker::askPublicObj;       //see public objective

        Consumer<String> privateObj = serverSpeaker::askPrivateObj;     //see private objective

        Consumer<String> tool = serverSpeaker::askToolCards;            //see tool card

        Consumer<String> favor = serverSpeaker::askFavorPoints;         //see favor points

        waitingAction.put(dictionary.getMessage(OWN_WINDOW_CARD_ENTRY_KEYWORD), window);
        waitingAction.put(dictionary.getMessage(ANOTHER_WINDOW_CARD_ENTRY_KEYWORD), other);
        waitingAction.put(dictionary.getMessage(DRAFT_ENTRY_KEYWORD), draft);
        waitingAction.put(dictionary.getMessage(PUBLIC_OBJECTIVE_ENTRY_KEYWORD), publicObj);
        waitingAction.put(dictionary.getMessage(PRIVATE_OBJECTIVE_ENTRY_KEYWORD), privateObj);
        waitingAction.put(dictionary.getMessage(TOOL_CARD_ENTRY_KEYWORD), tool);
        waitingAction.put(dictionary.getMessage(FAVOR_POINT_ENTRY_KEYWORD), favor);
    }
}
