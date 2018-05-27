package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
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

    private CliAskConnection connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private Scanner inKeyboard;
    private int numRound;
    private HashMap<String, ServerSpeaker> connParam;
    private boolean playing;

    public CliSystem() {
        connection = new CliAskConnection();
        connParam = new HashMap<>();
        inKeyboard = new Scanner(in);
        numRound = 0;
    }

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void chooseWindowCard(List<WindowCard> cards) throws IDNotFoundException {

        int pick;
        inKeyboard = new Scanner(in);

        for (WindowCard w: cards)
            printWindowCard(w);

        print("Choose your window card (choice between 1 and 4):");
        do{
            pick = inKeyboard.nextInt();
        }while(pick<0 || pick>4);

        pick--;

        serverSpeaker.setWindowCard(cards.get(pick).getName());

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) throws IDNotFoundException {
        print(user + " choose the window card " + card.getName());
        printWindowCard(card);
    }

    @Override
    public void printWindowCard(WindowCard window) throws IDNotFoundException {
        Cell c;
        for (int i=0; i<window.getWindow().getCols(); i++)
            out.print("\t" + i);
        print("");
        for (int i=0; i<window.getWindow().getRows(); i++) {
            out.print(i + "\t");
            for (int j = 0; j < window.getWindow().getCols(); j++) {
                c = window.getWindow().getCell(i, j);
                if (c.isOccupied())
                    out.print(ansi().eraseScreen().bg(Ansi.Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");
            }
            print("");
        }
        print("");
    }

    @Override
    public void printPrivObj(PrivateObjective privObj) {
        print("Your private objective is: " + privObj.getDescr());
    }

    @Override
    public void printPublObj(List<PublicObjective> publObj) {
        print("The public objective are:");
        for (PublicObjective p: publObj)
            print("- " + p.getDescr());
    }

    @Override
    public void setRound() {
        numRound++;
        print("Round number: " + numRound);
    }

    @Override
    public void isTurn (String username){
        if (userName.equals(username)) {
            print("It is your turn!");
            //askMove();
        }else {
            print("It is the turn of: " + username);
            //askWaiting();
        }
    }

    @Override
    public void showDraft(List<Dice> draft) {
        int i = 0;
        for (Dice d : draft) {
            i++;
            out.print("Dice n°" + i + ": " + ansi().eraseScreen().bg(Ansi.Color.valueOf(d.getColor().toString())).fg(BLACK).a(d.getValue()).reset() + "\n");
        }
    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {
        print("User: " + username + " set dice: " + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset() + " in cell: (" + dest.getRow() + "," + dest.getCol() + ") ");
    }

    /*public void askWaiting() { // action user can do while he is waiting

    }

    public void askMove(){ // action user can do while is playing

    }*/

    public void startGraphic() {

        connParam = connection.startConnection(this);

        userName = connParam.keySet().iterator().next();

        serverSpeaker = connParam.get(userName);

        playing = true;

        while (playing);

    }

    public void setPlaying(){
        playing = false;
    }

}

