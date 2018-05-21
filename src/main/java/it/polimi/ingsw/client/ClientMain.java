package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.Handler;
import it.polimi.ingsw.client.network.RmiHandler;
import it.polimi.ingsw.client.network.SocketHandler;

import java.net.*;
import java.util.*;
import static java.lang.System.*;

public class ClientMain {
    private String userName;
    private int port;
    private String ip;
    private Boolean socketConnection;
    private Boolean rmiConnection;
    private Handler handler;

    private ClientMain(int port, String userName) {
        this.userName = userName;
        socketConnection = false;
        rmiConnection = false;
        this.port = port;
    }

    public static void main(String[] args) {
        String userName = "Bob";
        ClientMain c = new ClientMain(4192, userName);
        c.startClient();
    }

    private void startClient() {
        out.println("Client is working");

        askConnection();
        requestIp();

        if (socketConnection)
            handler = new SocketHandler(port, ip);
        else if (rmiConnection)
            handler = new RmiHandler(4500, ip);

        handler.connect(userName);
        handler.listen();
        handler.disconnect(userName);
    }

    private void askConnection(){
        do {
            out.println("Choose your connection \n 'r' for RMI \n 's' for Socket \n 'd' for Default");

            Scanner input = new Scanner(System.in);
            String s = input.nextLine();

            if (s.equals("s") || s.equals("d")) {
                socketConnection = true;
                out.println("Socket connection chosen");
            } else if (s.equals("r")) {
                rmiConnection = true;
                out.println("RMI connection chosen");
            } else {
                out.println("Incorrect entry");
            }

        } while (!socketConnection && !rmiConnection);
    }

    private void requestIp(){
        Boolean ok = false;

        while(!ok) {
            out.println("Insert IP Address ['d' for default]");
            try {
                Scanner input = new Scanner(System.in);
                String choose = input.nextLine();

                if (choose.equals("d")) {
                    ip = InetAddress.getLocalHost().getHostAddress();
                    ok = true;
                } else {
                    ip = choose;
                    ok = true;
                }

                /*ok = IPAddressUtil.isIPv4LiteralAddress(ip) || IPAddressUtil.isIPv6LiteralAddress(ip);

                if(!ok)
                    out.println("Not a valid ip");
                    */

            } catch (UnknownHostException e) {
                out.println(e.getMessage());
            }
        }
    }

}
