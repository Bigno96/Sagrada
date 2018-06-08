package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;
import java.util.Scanner;

public class ChooseWindowCardTask implements Runnable {

    private CliSystem cliSystem;
    private final Scanner inKeyboard;
    private List<WindowCard> cards;

    ChooseWindowCardTask(CliSystem cliSystem, List<WindowCard> cards) {
        this.cliSystem = cliSystem;
        this.inKeyboard = new Scanner(System.in);
        this.cards = cards;
    }

    @Override
    public void run() {
        int pick;

        Boolean exit;
        do {
            cliSystem.print("Choose your window card (choice between 1 and 4):");
            pick = inKeyboard.nextInt();
            exit = pick<1 || pick>4;

        } while(exit);

        pick--;

        cliSystem.getServerSpeaker().setWindowCard(cliSystem.getUserName(), cards.get(pick).getName());
    }
}
