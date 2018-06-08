package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class WaitingMenuTask implements Runnable {

    private final HashMap<String, Consumer<String>> waitingAction;
    private final Scanner inKeyboard;
    private ServerSpeaker serverSpeaker;
    private ViewMessageParser dictionary;

    private CliSystem cliSystem;
    private boolean waiting;


    WaitingMenuTask(CliSystem cliSystem) {
        this.inKeyboard = new Scanner(System.in);
        this.waiting = true;
        this.serverSpeaker = cliSystem.getServerSpeaker();
        this.dictionary = cliSystem.getDictionary();

        this.cliSystem = cliSystem;
        this.waitingAction = new HashMap<>();
        mapWaiting();
    }

    @Override
    public void run() {
        while (waiting) {
            cliSystem.print(dictionary.getMessage("MENU_WAITING")); //menu waiting

            String s = inKeyboard.nextLine();

            if (!waitingAction.containsKey(s))
                cliSystem.print(dictionary.getMessage("INCORRECT_ENTRY"));
            else
                waitingAction.get(s).accept(cliSystem.getUserName());
        }
    }


    private void mapWaiting() {
        Consumer<String> window = username -> serverSpeaker.askWindowCard(username, username); //see personal window card
        Consumer<String> other = username -> {
            cliSystem.print(dictionary.getMessage("ASK_USER"));
            serverSpeaker.getAllUsername(username);
            String userWanted = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(userWanted, username); //see window card other player
        };
        Consumer<String> draft = username -> serverSpeaker.askDraft(username); //see draft
        Consumer<String> publicObj = username -> serverSpeaker.askPublObj(username); //see public objective
        Consumer<String> privateObj = username -> serverSpeaker.askPrivObj(username); //see private objective
        Consumer<String> tool = username -> serverSpeaker.askToolCards(username); //see tool card
        Consumer<String> favor = username -> serverSpeaker.askFavorPoints(username); //see favor points

        waitingAction.put("w", window);
        waitingAction.put("o", other);
        waitingAction.put("d", draft);
        waitingAction.put("p", publicObj);
        waitingAction.put("q", privateObj);
        waitingAction.put("t", tool);
        waitingAction.put("f", favor);
    }
}
