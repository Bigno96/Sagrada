package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.*;

class CliAskConnection {

    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ServerSpeaker serverSpeaker;
    private Scanner inKeyboard;
    private HashMap<String, ServerSpeaker> connParam;

    CliAskConnection(){
        socketConnection = false;
        rmiConnection = false;
        inKeyboard = new Scanner(in);
        connParam = new HashMap<>();
    }

    HashMap<String, ServerSpeaker> startConnection(CliSystem cli) {

        out.println("Insert your user Name");           // ask name of the user
        String userName = inKeyboard.nextLine();

        askConnection();            // ask type of connection wanted
        String ip = requestIp();

        if (socketConnection) {
            serverSpeaker = new SocketServerSpeaker(ip, cli);       // delegate to a socket connection
        } else if (rmiConnection) {
            serverSpeaker = new RmiServerSpeaker(ip, userName, cli);             // delegate to a rmi connection
        }

        while (!serverSpeaker.connect(userName)) {
            out.println("\nNo server listening on given ip.\n Please insert new one");  // ip didn't connected
            ip = requestIp();
            serverSpeaker.setIp(ip);
        }

        Boolean exit;

        do {
            exit = serverSpeaker.login(userName);

            if (!exit) {
                out.println(" Please insert your user Name again");
                userName = inKeyboard.nextLine();
            }

        } while (!exit);

        connParam.put(userName, serverSpeaker);

        return connParam;
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
