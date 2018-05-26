package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;


public class CliSystem implements viewInterface {

    private WindowFactory winFact = new WindowFactory();
    private String ip;                      // ip of the server
    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private static Scanner inKeyboard;

    public CliSystem(String userName){
        this.userName = userName;
        socketConnection = false;
        rmiConnection = false;
        inKeyboard = new Scanner(in);
    }

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void chooseWindowCard(int[] ids) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

        List<WindowCard> windows = winFact.getWindow(ids[0], ids[1]);

        //WindowCard winCard = windows.get();

    }

    @Override
    public void sendCardPlayers(int[] ids) {

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

    /*public void printWindowCard(Player player, WindowCard window) throws IDNotFoundException { //param id to choose windowCard in json
        out.println(player.getId());
        Cell c;
        for (int i=0; i<window.getWindow().getRows(); i++) {
            for (int j = 0; j < window.getWindow().getCols(); j++) {
                c = window.getWindow().getCell(i, j);
                if (c.isOccupied())
                    out.print(ansi().eraseScreen().bg(Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");
            }
            out.println("\n");
        }
    }*/

    @Override
    public void isTurn (String username){

    }

    @Override
    public void printDraft(List<Integer> draftValue, List<String> draftColor) {

    }

    @Override
    public void placementDice(String username, int row, int col, String color, int value) {

    }

    public void askParameter(){

    }

    public void listenCli(){

    }

    public void startGraphic() {

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

