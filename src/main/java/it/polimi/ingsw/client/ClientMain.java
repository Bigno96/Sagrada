package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;

import java.util.*;
import static java.lang.System.*;

public class ClientMain {

    private String userName;
    private String ip;                      // ip of the server
    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server

    private ClientMain(String userName) {
        this.userName = userName;
        socketConnection = false;
        rmiConnection = false;
    }

    public static void main(String[] args) {
        Scanner inKeyboard = new Scanner(in);
        out.println("Insert your user Name");           // ask name of the user

        ClientMain c = new ClientMain(inKeyboard.nextLine());
        c.startClient();
    }

    private void startClient() {
        out.println("Client is working");

        askConnection();            // ask type of connection wanted
        ip = requestIp();

        if (socketConnection) {
            serverSpeaker = new SocketServerSpeaker(ip);          // delegate to a socket connection
        }
        else if (rmiConnection) {
            serverSpeaker = new RmiServerSpeaker(ip, userName);             // delegate to a rmi connection
        }

        while(!serverSpeaker.connect(userName)) {   // connect to server
            out.println("\nNo server listening on given ip.\n Please insert new one\n");
            ip = requestIp();
            serverSpeaker.setIp(ip);
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
