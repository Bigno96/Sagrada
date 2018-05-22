package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ClientHandler;
import it.polimi.ingsw.client.network.RmiClientHandler;
import it.polimi.ingsw.client.network.SocketClientHandler;

import java.net.*;
import java.util.*;
import static java.lang.System.*;

public class ClientMain {
    private String userName;
    private String ip;
    private Boolean socketConnection;
    private Boolean rmiConnection;
    private ClientHandler clientHandler;

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
        requestIp();                // ip of serverSocket if Socket conn, ip of remote interface if rmi conn

        while(!clientHandler.connect(userName)) {          // connect to server
            out.println("\nNo server listening on given ip.\n Please insert new one\n");
            requestIp();
        }
        clientHandler.listen();                   // waiting for commands
        clientHandler.disconnect(userName);       // disconnection wanted when not listening anymore
    }

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

    private void requestIp() {
        Boolean ok = false;

        while(!ok) {
            out.println("Insert IP Address \n ['d' for default]");
            try {
                Scanner input = new Scanner(System.in);
                String choose = input.nextLine();

                if (choose.equals("d")) {                       // default ip -> localHost
                    ip = InetAddress.getLocalHost().getHostAddress();
                } else {
                    ip = choose;
                }

                ok = validIP(ip);                   // verify if ip is a valid address

                if(!ok)
                    out.println("Not a valid ip");

            } catch (UnknownHostException e) {
                out.println(e.getMessage());
            }
        }

        if (socketConnection) {
            clientHandler = new SocketClientHandler(ip);          // delegate to a socket connection
        }
        else if (rmiConnection) {
            clientHandler = new RmiClientHandler(ip);             // delegate to a rmi connection
        }
    }

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
