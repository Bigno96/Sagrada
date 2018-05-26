package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;
import org.fusesource.jansi.Ansi;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;


public class CliSystem implements viewInterface {

    private WindowFactory winFact = new WindowFactory();
    private String ip;                      // ip of the server
    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private static Scanner inKeyboard;
    private WindowCard winCard;
    private WindowCard winTmp;
    private HashMap<String, WindowCard> windPlayers = new HashMap<>();
    private PrivateObjective objPriv;
    private List<PublicObjective> listPubObj;
    private Draft draft;
    private Dice dTmp;

    public CliSystem(){
        socketConnection = false;
        rmiConnection = false;
        inKeyboard = new Scanner(in);
    }

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void chooseWindowCard(int id1, int id2) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

        int pick;

        List<WindowCard> windows = winFact.getWindow(id1, id2);

        for (WindowCard w: windows)
            printWindowCard(w);

        print("Choose your window card (choice between 1 and 4):");
        do{
            pick = inKeyboard.nextInt();
        }while(pick<0 || pick>4);

        winCard = windows.get(pick);

        windPlayers.put(userName, winCard);

        serverSpeaker.setWindowCard(winCard.getName());

    }

    @Override
    public void sendCardPlayer(String user, String name) throws IDNotFoundException, FileNotFoundException, PositionException, ValueException {
        winTmp = winFact.getWindow(name);
        windPlayers.put(user, winTmp);
        print(user + " choose the window card " + name);
        printWindowCard(winTmp);
    }

    @Override
    public void printPrivObj(int id) {


    }

    @Override
    public void printPublObj(int[] ids) {

    }

    @Override
    public void setRound() {

    }

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
    public void isTurn (String username){
        if (userName.equals(username))
            print("It is your turn!");
        else
            print("It is the turn of: " + username);
    }

    @Override
    public void showDraft(List<Integer> draftId, List<Integer> draftValue, List<String> draftColor) throws IDNotFoundException, SameDiceException {
        int i = 0;
        for (Integer id: draftId){
            dTmp = new Dice(id, Colors.parseColor(draftColor.get(i)), draftValue.get(i));
            draft.addDice(dTmp);
            i++;
        }
        printDraft();
    }

    public void printDraft(){
        int i = 0;
        for (Iterator<Dice> it = draft.itrDraft(); it.hasNext(); ) {
            i++;
            dTmp = it.next();
            out.print("Dice n°" + i + ": " + ansi().eraseScreen().bg(Ansi.Color.valueOf(dTmp.getColor().toString())).fg(BLACK).a(dTmp.getValue()).reset() + "\n");
        }
    }

    @Override
    public void placementDice(String username, int row, int col, String color, int value) {

    }

    public void askParameter() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        chooseWindowCard(1,2);
    }

    public void startGraphic() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

        inKeyboard = new Scanner(in);
        out.println("Insert your user Name");           // ask name of the user

        userName = inKeyboard.nextLine();

        askConnection();            // ask type of connection wanted
        ip = requestIp();

        if (socketConnection) {
            serverSpeaker = new SocketServerSpeaker(ip);          // delegate to a socket connection
        } else if (rmiConnection) {
            serverSpeaker = new RmiServerSpeaker(ip, userName);             // delegate to a rmi connection
        }

        Boolean exit;

        do {
            try {
                exit = serverSpeaker.connect(userName);                   // connect to server

                if (!exit) {                                    // ip didn't connected
                    out.println("\nNo server listening on given ip.\n Please insert new one");
                    ip = requestIp();
                    serverSpeaker.setIp(ip);
                }

            } catch (SamePlayerException e) {               // player with the same name already logged
                out.println(" Please insert new one");
                this.userName = inKeyboard.nextLine();
                exit = false;
            }

        } while (!exit);

        while (!serverSpeaker.login(userName)) {                         // login to the lobby
            out.println("\nInsert your user Name");
            this.userName = inKeyboard.nextLine();
        }

        askParameter();
    }

    /**
     * Ask for type of Connection to use
     */
    private void askConnection() {
        do {
            out.println("Choose your connection \n 'r' for RMI \n 's' for Socket \n 'd' for Default");

            Scanner input = new Scanner(System.in);
            String s = input.nextLine();

            if (s.equals("s") || s.equals("d")) {       // Socket (or default) connection chosen
                socketConnection = true;
                out.println("Socket connection chosen");
            } else if (s.equals("r")) {                 // Rmi connection chosen
                rmiConnection = true;
                out.println("RMI connection chosen");
            } else {                                    // wrong typing
                out.println("Incorrect entry");
            }

        } while (!socketConnection && !rmiConnection);
    }

    /**
     * Ask for ip
     * @return String read from cmd as input
     */
    private String requestIp() {
        Boolean ok = false;
        String ret = "";

        while(!ok) {
            out.println("Insert IP Address");
            Scanner input = new Scanner(System.in);
            ret = input.nextLine();

            ok = validIP(ret);                   // verify if ip is a valid address

            if(!ok)
                out.println("Not a valid ip");
        }

        return ret;
    }

    /**
     * Verify if passed String represents a valid IPv4 or IPv6 address
     * @param ip != null
     * @return true if valid ip address is passed, else false
     */
    private boolean validIP(String ip) {
        if (ip.isEmpty())
            return false;

        String[] parts = ip.split("\\.");

        if (parts.length != 4 && parts.length != 6)
            return false;

        for (String s : parts) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255)
                return false;
        }

        return !ip.endsWith(".");
    }

}

