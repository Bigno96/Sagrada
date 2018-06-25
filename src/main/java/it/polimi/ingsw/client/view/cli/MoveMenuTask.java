package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Run during player's turn
 */
public class MoveMenuTask implements Runnable {

    private static final String MENU_PLAYING_KEYWORD = "MENU_PLAYING_MESSAGE";

    private static final String PLACE_DICE_ENTRY_KEYWORD = "PLACE_DICE_ENTRY";
    private static final String PLACE_DICE_MESSAGE_KEYWORD = "PLACE_DICE_MESSAGE";

    private static final String USE_TOOL_CARD_ENTRY_KEYWORD = "USE_TOOL_CARD_ENTRY";
    private static final String USE_TOOL_CARD_MESSAGE_KEYWORD = "USE_TOOL_CARD_MESSAGE";

    private static final String PASS_TURN_ENTRY_KEYWORD = "PASS_TURN_ENTRY";
    private static final String PASS_TURN_MESSAGE_KEYWORD = "PASS_TURN_MESSAGE";

    private static final String INCORRECT_MESSAGE_KEYWORD = "INCORRECT_MESSAGE";
    private static final String ALREADY_DONE_KEYWORD = "ALREADY_DONE";
    private static final String PASSED_KEYWORD = "PASSED";


    private final HashMap<String, Consumer<Boolean>> playingAction;
    private final Scanner inKeyboard;
    private ServerSpeaker serverSpeaker;
    private ViewMessageParser dictionary;

    private CliSystem cliSystem;
    private boolean played;
    private boolean moved;
    private boolean used;

    MoveMenuTask(CliSystem cliSystem) {
        this.cliSystem = cliSystem;
        this.inKeyboard = new Scanner(System.in);
        this.serverSpeaker = cliSystem.getServerSpeaker();
        this.dictionary = cliSystem.getDictionary();

        this.played = false;

        playingAction = new HashMap<>();
        mapPlaying();
    }

    /**
     * Ask and shows which moves user can make
     */
    @Override
    public void run() {
        moved = false;
        used = false;

        do {
            cliSystem.print(dictionary.getMessage(MENU_PLAYING_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(PLACE_DICE_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(USE_TOOL_CARD_MESSAGE_KEYWORD));

            cliSystem.print("'" + dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD) + "' " +
                    dictionary.getMessage(PASS_TURN_MESSAGE_KEYWORD));

            String s = inKeyboard.nextLine();

            if (!playingAction.containsKey(s)){
                cliSystem.print(dictionary.getMessage(INCORRECT_MESSAGE_KEYWORD));
            } else
                playingAction.get(s).accept(!played);

        } while (!played);

        serverSpeaker.endTurn(cliSystem.getUserName());
    }

    /**
     * Maps hash map for playing choices
     */
    private void mapPlaying(){
        Consumer<Boolean> move = playing -> {       // place a dice
            if (playing && !moved) {
                moved = true;
                cliSystem.moveDice();
            } else
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
        };

        Consumer<Boolean> use = playing-> {         // use a tool card
            if (playing && !used) {
                cliSystem.useToolCard();
                used = true;
            } else
                cliSystem.print(dictionary.getMessage(ALREADY_DONE_KEYWORD));
        };

        Consumer<Boolean> pass = playing -> {       // end turn
            cliSystem.print(dictionary.getMessage(PASSED_KEYWORD));
            played = true;
        };

        playingAction.put(dictionary.getMessage(PLACE_DICE_ENTRY_KEYWORD), move);
        playingAction.put(dictionary.getMessage(USE_TOOL_CARD_ENTRY_KEYWORD), use);
        playingAction.put(dictionary.getMessage(PASS_TURN_ENTRY_KEYWORD), pass);
    }
}
