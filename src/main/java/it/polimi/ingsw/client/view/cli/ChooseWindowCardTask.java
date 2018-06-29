package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;
import java.util.Scanner;

/**
 * Run to execute the choice of window card
 */
public class ChooseWindowCardTask implements Runnable {

    private static final String WINDOW_CARD_CHOICE_ENTRY_KEYWORD = "WINDOW_CARD_CHOICE_ENTRY";

    private CliSystem cliSystem;
    private final Scanner inKeyboard;
    private List<WindowCard> cards;

    private final ViewMessageParser dictionary;

    ChooseWindowCardTask(CliSystem cliSystem, List<WindowCard> cards) {
        this.cliSystem = cliSystem;
        this.inKeyboard = new Scanner(System.in);
        this.cards = cards;
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    @Override
    public void run() {
        int pick;

        Boolean wrong;
        do {
            cliSystem.print(dictionary.getMessage(WINDOW_CARD_CHOICE_ENTRY_KEYWORD));
            pick = inKeyboard.nextInt();
            wrong = pick<1 || pick>4;

        } while(wrong);

        pick--;

        cliSystem.getServerSpeaker().setWindowCard(cliSystem.getUserName(), cards.get(pick).getName());
    }
}
