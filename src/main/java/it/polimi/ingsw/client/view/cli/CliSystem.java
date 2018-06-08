package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CliSystem implements ViewInterface {

    private final CliAskConnection connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private final Scanner inKeyboard;
    private int numRound;
    private HashMap<String, ServerSpeaker> connParam;

    private Thread moveThread;
    private Thread waitingThread;

    private static final String INSERT_NUMBER = "Insert a number";

    private final ViewMessageParser dictionary;

    public CliSystem() {
        connection = new CliAskConnection();
        connParam = new HashMap<>();
        inKeyboard = new Scanner(System.in);
        numRound = 0;

        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    public void startGraphic() {
        connParam = connection.startConnection(this);

        userName = connParam.keySet().iterator().next();

        serverSpeaker = connParam.get(userName);

        MoveMenuTask taskMove = new MoveMenuTask(this);
        moveThread = new Thread(taskMove);

        WaitingMenuTask taskWaiting = new WaitingMenuTask(this);
        waitingThread = new Thread(taskWaiting);
    }

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

    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        print("These are the window cards selected for you:");

        cards.forEach(this::printWindowCard);

        ChooseWindowCardTask task = new ChooseWindowCardTask(this, cards);
        new Thread(task).start();
    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {
        if (user.equals(this.userName))
            print("\nYou chose this window card ");
        else
            print("\n" + user + " choose window card ");

        printWindowCard(card);
    }

    @Override
    public void printWindowCard(WindowCard window) {
        Cell c;

        out.println(window.getName());
        out.println("Favor Point: " + window.getNumFavPoint());

        for (int i=0; i<window.getWindow().getCols(); i++)
            out.print("\t" + i);
        print("");

        for (int i=0; i<window.getWindow().getRows(); i++) {
            out.print(i + "\t");

            for (int j = 0; j < window.getWindow().getCols(); j++) {
                c = window.getWindow().getCell(i, j);

                if (c.isOccupied()) {
                    try {
                        out.print(ansi().eraseScreen().bg(Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                    } catch (IDNotFoundException e) {
                        out.println(e.getMessage());
                    }
                }
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");

            }
            print("");
        }
        print("");
    }

    @Override
    public void printUsers(List<String> users) {
        users.forEach(this::print);
    }

    @Override
    public void printPrivObj(ObjectiveCard privObj) {
        print("Your private objective is: " + privObj.getDescr());
    }

    @Override
    public void printPublObj(List<ObjectiveCard> publObj) {
        print("Public objectives are:");
        publObj.forEach(p -> print("- " + p.getDescr() + "/ points: " + p.getPoint()));
    }

    @Override
    public void setRound() {
        numRound++;
        print("Round number: " + numRound);
    }

    @Override
    public void isTurn (String username) {
        if (userName.equals(username)) {
            print("It is your turn!");
            waitingThread.interrupt();
            moveThread.start();

        } else {
            print("It is the turn of: " + username);
            waitingThread.start();
        }
    }

    @Override
    public void showDraft(List<Dice> draft) {
        draft.forEach(dice ->
                print(ansi().eraseScreen().bg(Ansi.Color.valueOf(dice.getColor().toString())).fg(BLACK).a(dice.getValue()).reset() + "  "));
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {
        print("User: " + username + " set dice: " + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset() + " in cell: (" + dest.getRow() + "," + dest.getCol() + ") ");
    }

    void moveDice(){
        int index;
        int row;
        int col;

        //place a dice (show personal window card and draft to choose dice)

        print("This is the draft, choose the dice entering the number of the dice: ");
        serverSpeaker.askDraft(userName);
        try {
            index = Integer.parseInt(inKeyboard.nextLine());
            index--;
        } catch (NumberFormatException e) {
            print(INSERT_NUMBER);
            index = Integer.parseInt(inKeyboard.nextLine());
            index--;
        }

        print("This is your window card, choose the position where do you want to place the dice: ");
        serverSpeaker.askWindowCard(userName, userName);
        print("Row: ");
        try {
            row = Integer.parseInt(inKeyboard.nextLine());
        } catch (NumberFormatException e) {
            print(INSERT_NUMBER);
            row = Integer.parseInt(inKeyboard.nextLine());
        }
        print("Column: ");
        try {
            col = Integer.parseInt(inKeyboard.nextLine());
        } catch (NumberFormatException e) {
            print(INSERT_NUMBER);
            col = Integer.parseInt(inKeyboard.nextLine());
        }

        serverSpeaker.moveDiceFromDraftToCard(userName, index, row, col);

    }

    void useToolCard(){} //use tool card (show tool cards and choose which one use)

}
