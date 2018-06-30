package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.stream.IntStream;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CliSystem implements ViewInterface {

    private static final String INSERT_NUMBER_KEYWORD = "INSERT_NUMBER";
    private static final String WINDOW_CARD_SELECTED_KEYWORD = "WINDOW_CARD_SELECTED";
    private static final String WINDOW_CARD_CHOSEN_KEYWORD = "WINDOW_CARD_CHOSEN";
    private static final String PLAYER_CARD_CHOSEN_KEYWORD = "PLAYER_CARD_CHOSEN";
    private static final String FAVOR_POINT_KEYWORD = "FAVOR_POINT";

    private static final String PRIVATE_OBJECTIVE_KEYWORD = "PRIVATE_OBJECTIVE_CHOSEN";
    private static final String PUBLIC_OBJECTIVE_KEYWORD = "PUBLIC_OBJECTIVE";
    private static final String OBJECTIVE_POINT_KEYWORD = "OBJECTIVE_POINT";

    private static final String OTHER_PLAYER_TURN_KEYWORD = "OTHER_PLAYER_TURN";
    private static final String YOUR_TURN_KEYWORD = "YOUR_TURN";

    private static final String SHOW_DRAFT_KEYWORD = "SHOW_DRAFT";
    private static final String SHOW_YOUR_WINDOW_CARD_TO_CHOOSE_KEYWORD = "SHOW_YOUR_WINDOW_CARD_TO_CHOOSE";
    private static final String ASK_ROW_KEYWORD = "ASK_ROW";
    private static final String ASK_COLUMN_KEYWORD = "ASK_COLUMN";

    private final CliAskConnection connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private final Scanner inKeyboard;
    private String userName;
    private HashMap<String, ServerSpeaker> connParam;

    private Thread moveThread;
    private Thread waitingThread;

    private final ViewMessageParser dictionary;

    public CliSystem() {
        connection = new CliAskConnection();
        connParam = new HashMap<>();
        inKeyboard = new Scanner(System.in);

        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    /**
     * Starts the graphic setting up connection parameters
     */
    @Override
    public void startGraphic() {
        connParam = connection.startConnection(this);

        userName = connParam.keySet().iterator().next();

        serverSpeaker = connParam.get(userName);

        MoveMenuTask taskMove = new MoveMenuTask(this);
        moveThread = new Thread(taskMove);

        WaitingMenuTask taskWaiting = new WaitingMenuTask(this);
        waitingThread = new Thread(taskWaiting);
    }

    /**
     * @param s to be printed
     */
    @Override
    public void print(String s) {
        out.println(s);
    }

    String getUserName() {
        return this.userName;
    }

    ServerSpeaker getServerSpeaker() {
        return this.serverSpeaker;
    }

    ViewMessageParser getDictionary() {
        return this.dictionary;
    }

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {

        print(dictionary.getMessage(WINDOW_CARD_SELECTED_KEYWORD));

        cards.forEach(this::printWindowCard);

        ChooseWindowCardTask task = new ChooseWindowCardTask(this, cards);
        new Thread(task).start();
    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard().getName()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {
        if (user.equals(this.userName))
            print(dictionary.getMessage(WINDOW_CARD_CHOSEN_KEYWORD));
        else
            print("\n" + user + dictionary.getMessage(PLAYER_CARD_CHOSEN_KEYWORD));

        printWindowCard(card);
    }

    /**
     * @param window window card to be printed
     */
    @Override
    public void printWindowCard(WindowCard window) {
        Cell c;

        out.println(window.getName());
        out.println(dictionary.getMessage(FAVOR_POINT_KEYWORD) + window.getNumFavPoint());

        for (int i = 0; i<window.getWindow().getMaxCol(); i++)
            out.print("\t" + i);
        print("");

        for (int i = 0; i<window.getWindow().getMaxRow(); i++) {
            out.print(i + "\t");

            for (int j = 0; j < window.getWindow().getMaxCol(); j++) {
                c = window.getWindow().getCell(i, j);

                if (c.isOccupied()) {
                    out.print(ansi().eraseScreen().bg(Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                }
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");

            }
            print("");
        }
        print("");
    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        print(dictionary.getMessage(PRIVATE_OBJECTIVE_KEYWORD));
        print(privateObj.getDescription());
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printPublicObj(List<ObjectiveCard> publicObj) {
        print(dictionary.getMessage(PUBLIC_OBJECTIVE_KEYWORD));
        publicObj.forEach(p -> print("- " + p.getDescription() + dictionary.getMessage(OBJECTIVE_POINT_KEYWORD) + p.getPoint()));
    }

    /**
     * @param username = game.getCurrentPlayer().getId()
     */
    @Override
    public void isTurn (String username) {
        if (userName.equals(username)) {
            print(dictionary.getMessage(YOUR_TURN_KEYWORD));
            waitingThread.interrupt();
            moveThread.start();

        } else {
            print(dictionary.getMessage(OTHER_PLAYER_TURN_KEYWORD) + username);
            waitingThread.start();
        }
    }

    /**
     * @param draft = game.getBoard().getDraft()
     */
    @Override
    public void showDraft(List<Dice> draft) {
        draft.forEach(dice ->
                out.print(ansi().eraseScreen().bg(Ansi.Color.valueOf(dice.getColor().toString())).fg(BLACK).a(dice.getValue()).reset() + "  "));
    }

    /**
     * @param username of player moving the dice
     * @param dest     cell where the dice is being moved
     * @param moved    dice being moved
     */
    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        print("\nUtente " + username + " ha piazzato il dado: " + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset() + " nella cella: (" + dest.getRow() + "," + dest.getCol() + ") ");
    }


    /**
     * Used when wrong placement is tried
     */
    @Override
    public void wrongPlacementDice() {
        moveDice();
    }

    /**
     * @param ranking sorted map of player username and their points through the game
     */
    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {
        int sizeRanking = ranking.size();

        IntStream.range(1, sizeRanking+1).forEach(integer -> {
            int firstPointTmp = ranking.lastKey();
            String firstUserTmp = ranking.get(firstPointTmp);
            print("Posizione " + integer + ": " + firstUserTmp + " - punteggio: " + firstPointTmp);
            ranking.remove(firstPointTmp, firstUserTmp);
        });
    }

    /**
     * Used to get what dice to move and where it wants to move
     */
    void moveDice() {
        int index;
        int row;
        int col;

        //place a dice (show personal window card and draft to choose dice)

        print(dictionary.getMessage(SHOW_YOUR_WINDOW_CARD_TO_CHOOSE_KEYWORD));
        serverSpeaker.askWindowCard(userName, userName);

        print(dictionary.getMessage(SHOW_DRAFT_KEYWORD));
        serverSpeaker.askDraft(userName);

        do {
            print(dictionary.getMessage(INSERT_NUMBER_KEYWORD));
            try {
                index = Integer.parseInt(inKeyboard.nextLine());
                index--;
            } catch (NumberFormatException e) {
                print(e.getMessage());
                index = -1;
            }
        } while (index < 0);

        do {
            print(dictionary.getMessage(ASK_ROW_KEYWORD));
            try {
                row = Integer.parseInt(inKeyboard.nextLine());
            } catch (NumberFormatException e) {
                print(e.getMessage());
                row = -1;
            }
        } while (row < 0);

        do {
            print(dictionary.getMessage(ASK_COLUMN_KEYWORD));
            try {
                col = Integer.parseInt(inKeyboard.nextLine());
            } catch (NumberFormatException e) {
                print(e.getMessage());
                col = -1;
            }
        } while (col < 0);

        serverSpeaker.placementDice(userName, index, row, col);
    }

    void useToolCard(){

    } //use tool card (show tool cards and choose which one use)

}