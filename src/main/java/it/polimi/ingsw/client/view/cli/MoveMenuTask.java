package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class MoveMenuTask implements Runnable {

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

    @Override
    public void run() {
        moved = false;
        used = false;

        do {
            cliSystem.print(dictionary.getMessage("MENU_PLAYING"));

            String s = inKeyboard.nextLine();

            if (!playingAction.containsKey(s)){
                cliSystem.print(dictionary.getMessage("INCORRECT_ENTRY"));
            } else
                playingAction.get(s).accept(!played);

        } while (!played);

        serverSpeaker.endTurn(cliSystem.getUserName());
    }

    private void mapPlaying(){
        Consumer<Boolean> move = playing -> {
            if (playing && !moved) {
                moved = true;
                cliSystem.moveDice();
            } else
                cliSystem.print(dictionary.getMessage("ALREADY_DONE"));
        };

        Consumer<Boolean> use = playing-> {
            if (playing && !used) {
                cliSystem.useToolCard();
                used = true;
            } else
                cliSystem.print(dictionary.getMessage("ALREADY_DONE"));
        };

        Consumer<Boolean> pass = playing -> {
            cliSystem.print(dictionary.getMessage("PASSED"));
            played = true;
        };

        playingAction.put("p", move);
        playingAction.put("t", use);
        playingAction.put("q", pass);
    }
}
