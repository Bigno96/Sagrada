package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
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
    private boolean waiting;
    private boolean played;

    public CliSystem() {
        connection = new CliAskConnection();
        connParam = new HashMap<>();
        inKeyboard = new Scanner(in);
        numRound = 0;
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
    public void chooseWindowCard(List<WindowCard> cards) throws IDNotFoundException, FileNotFoundException, PositionException, ValueException, RemoteException {

        int pick;

        cards.forEach(this::printWindowCard);

        print("Choose your window card (choice between 1 and 4):");
        do{
            pick = inKeyboard.nextInt();
        }while(pick<1 || pick>4);

        pick--;

        serverSpeaker.setWindowCard(userName, cards.get(pick).getName());

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {
        print(user + " choose window card " + card.getName());
        printWindowCard(card);
    }

    @Override
    public void printWindowCard(WindowCard window) {
        Cell c;

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
        users.forEach(user -> print(user + "\t"));
    }

    @Override
    public void printPrivObj(PrivateObjective privObj) {
        print("Your private objective is: " + privObj.getDescr());
    }

    @Override
    public void printPublObj(List<PublicObjective> publObj) {
        print("The public objective are:");
        publObj.forEach(p -> print("- " + p.getDescr()));
    }

    @Override
    public void setRound() {
        numRound++;
        print("Round number: " + numRound);
    }

    @Override
    public void isTurn (String username) throws RemoteException {
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
            print("You are waiting, what you want to do:");
            print("w - see your window card");
            print("o - see window card of another player");
            print("d - see draft");
            print("p - see public objectives");
            print("q - see your private objective");
            /*print("t - see tool cards");
            print("f - see how many favor points you have left");*/

            Scanner input = new Scanner(System.in);
            String s = input.nextLine();

            if (s.equals("w")){
                serverSpeaker.askWindowCard(userName); //see personal window card
            }else if (s.equals("o")){
                print("Insert the name of the user whom you want to see the window card between these:");
                serverSpeaker.askUsers(userName);
                String user = input.nextLine();
                serverSpeaker.askWindowCard(user); //see window card other player
            }else if (s.equals("d")){
                serverSpeaker.askDraft(); //see draft
            }else if (s.equals("p")){
                serverSpeaker.askPublObj(); //see public objective
            }else if (s.equals("q")){
                serverSpeaker.askPrivObj(userName); //see private objective
            }/*else if (s.equals("t")){
                serverSpeaker.askToolCards(); //see tool card
            }else if (s.equals("f")){
                serverSpeaker.askFavorPoints(); //see favor points
            }*/else{
                print("Incorrect choice!");
            }

        }while (waiting);
    }

    private void askMove() throws RemoteException { // action user can do while is playing
        print("What move do you want to make:");
        print("p - place a dice from the draft");
        //print("t - use a tool card");

        Scanner input = new Scanner(System.in);
        String s;
        do{
            s = input.nextLine();
            if (s.equals("p")){

                int index;
                int row;
                int col;

                //place a dice (show personal window card and draft to choose dice)

                print("This is the draft, choose the dice entering the number of the dice: ");
                serverSpeaker.askDraft();
                try{
                    index = Integer.parseInt(input.nextLine());
                    index--;
                }catch (NumberFormatException e){
                    print("Insert a number!");
                    index = Integer.parseInt(input.nextLine());
                    index--;
                }

                print("This is your window card, choose the position where do you want to place the dice: ");
                serverSpeaker.askWindowCard(userName);
                print("Row: ");
                try{
                    row = Integer.parseInt(input.nextLine());
                }catch (NumberFormatException e){
                    print("Insert a number!");
                    row = Integer.parseInt(input.nextLine());
                }
                print("Column: ");
                try{
                    col = Integer.parseInt(input.nextLine());
                }catch (NumberFormatException e){
                    print("Insert a number!");
                    col = Integer.parseInt(input.nextLine());
                }

                serverSpeaker.moveDiceFromDraftToCard(userName, index, row, col);

                played = true;
            }/*else if (s.equals("t")){
                //use tool card (show tool cards and choose which one use)
                played = true;
            }*/else{
                print("Incorrect choice!");
            }
        }while (!played);

        serverSpeaker.endTurn(userName);

    }

}

