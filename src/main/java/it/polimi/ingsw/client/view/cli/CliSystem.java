package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    private boolean waiting;
    private boolean played;

    private static final String INSERT_NUMBER = "Insert a number";

    private final ViewMessageParser dictionary;
    private final HashMap<String, Consumer<String>> connectionAction;

    public CliSystem() {
        connection = new CliAskConnection();
        connParam = new HashMap<>();
        inKeyboard = new Scanner(System.in);
        numRound = 0;

        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.connectionAction = new HashMap<>();
        mapConnection();
    }

    private void mapConnection() {
        Consumer<String> window = username -> serverSpeaker.askWindowCard(username); //see personal window card
        Consumer<String> other = username -> {
            print("Insert the name of the user whom you want to see the window card between these:");
            serverSpeaker.askUsers(username);
            String user = inKeyboard.nextLine();
            serverSpeaker.askWindowCard(user); //see window card other player
        };
        Consumer<String> draft = username -> serverSpeaker.askDraft(username); //see draft
        Consumer<String> publicObj = username -> serverSpeaker.askPublObj(username); //see public objective
        Consumer<String> privateObj = username -> serverSpeaker.askPrivObj(username); //see private objective
        Consumer<String> tool = username -> serverSpeaker.askToolCards(username); //see tool card
        Consumer<String> favor = username -> serverSpeaker.askFavorPoints(username); //see favor points

        connectionAction.put("w", window);
        connectionAction.put("o", other);
        connectionAction.put("d", draft);
        connectionAction.put("p", publicObj);
        connectionAction.put("q", privateObj);
        connectionAction.put("t", tool);
        connectionAction.put("f", favor);
    }

    public void startGraphic() {
        connParam = connection.startConnection(this);

        userName = connParam.keySet().iterator().next();

        serverSpeaker = connParam.get(userName);
    }

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) {

        int pick;

        print("These are the window cards selected for you:");

        cards.forEach(this::printWindowCard);

        Boolean exit;
        do {
            print("Choose your window card (choice between 1 and 4):");
            pick = inKeyboard.nextInt();
            exit = pick<1 || pick>4;

        } while(exit);

        pick--;

        serverSpeaker.setWindowCard(userName, cards.get(pick).getName());
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
        print("The public objective are:");
        publObj.forEach(p -> print("- " + p.getDescr()));
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
            waiting = false;
            played = false;
            askMove();
        } else {
            print("It is the turn of: " + username);
            waiting = true;
            askWaiting();
        }
    }

    @Override
    public void showDraft(List<Dice> draft) {
        int i = 0;

        for (Dice d : draft) {
            print("Dice n°" + i++ + ": " + ansi().eraseScreen().bg(Ansi.Color.valueOf(d.getColor().toString())).fg(BLACK).a(d.getValue()).reset() + "\n");
        }
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {
        print("User: " + username + " set dice: " + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset() + " in cell: (" + dest.getRow() + "," + dest.getCol() + ") ");
    }

    private void askWaiting() { // action user can do while he is waiting
        do {

            print(dictionary.getMessage("MENU_WAITING")); //menu waiting

            String s = inKeyboard.nextLine();

            if (!connectionAction.containsKey(s))
                print(dictionary.getMessage("INCORRECT_ENTRY"));
            else
                connectionAction.get(s).accept(userName);

        } while (waiting);

    }

    private void askMove() { // action user can do while is playing
        boolean moved = false;
        boolean used = false;

        do {
            print(dictionary.getMessage("MENU_PLAYING"));

            String s = inKeyboard.nextLine();

            if (s.equals("p")) {
                if (!moved) {
                    moveDice();
                    moved = true;
                }
            }else if (s.equals("t")){
                //use tool card (show tool cards and choose which one use)
                if (!used) {
                    useToolCard();
                    used = true;
                }
            }else if (s.equals("q")){
                print(dictionary.getMessage("PASSED"));
                played = true;
            } else {
                print(dictionary.getMessage("INCORRECT_ENTRY"));
            }

        } while (!played);

        serverSpeaker.endTurn(userName);
    }

    private void moveDice(){
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
        serverSpeaker.askWindowCard(userName);
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

    private void useToolCard(){}

}

