package it.polimi.ingsw.client.network;

import java.net.*;
import java.io.*;
import java.util.*;
import static java.lang.System.*;
import java.net.Socket;

public class ClientMain {
    private int port = 4912;
    private Socket socket;
    private String ip;
    private Boolean socketConnection;

    private ClientMain() {
    }

    private void startClient() {
        out.println("ClientMain on");

        try {
            askConnection();
            requestIp();
            connect();
            //exe();
        }catch(Exception e) {
            out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    private void askConnection(){
        out.println("Choose your connection \n -r for RMI \n -s for Socket");
        while(true) {
            Scanner input = new Scanner(System.in);
            String s = input.nextLine();
            if ("s".equals(s)) {
                socketConnection = true;
                out.println("Try to connect with Socket connection");
                break;
            } else if ("r".equals(s)) {
                out.println("Next Update");
                break;
            } else {
                out.println("Incorrect connection \n Choose your connection \n -r for RMI \n -s for Socket ");
            }
        }
    }

    private void requestIp(){
        out.println("Insert IP Address or p for default");
        try {
            Scanner input = new Scanner(System.in);
            String choose = input.nextLine();
            if ("p".equals(choose)) {
                out.println("Your IP Address is" +InetAddress.getLocalHost().getHostAddress());
                ip = InetAddress.getLocalHost().getHostAddress();
                out.println(ip);
            } else {
                out.println("Your Address is " + choose);
                ip = choose;
            }
        } catch (Exception e) {
            out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    private void connect() {
        out.println("ClientMain try to connect");

        try (Socket socket = new Socket(ip, port)) {
                System.out.println("Connection established");
                Scanner socketIn = new Scanner(socket.getInputStream());
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
                Scanner stdin = new Scanner(System.in);
            try {
                while (true) {
                    System.out.println("Welcome!");
                    System.out.println("q --> exit");

                    String inputLine = stdin.nextLine();
                    if (inputLine.equals("q")) {
                        socketOut.println(inputLine);
                        System.out.println("EndSocket");
                        break;
                    }
                    socketOut.println(inputLine);
                    socketOut.flush();
                    String socketLine = socketIn.nextLine();
                    System.out.println(socketLine);
                }
            }
            catch(NoSuchElementException e) {
                System.out.println("Connection closed");
            }
            finally {
                stdin.close();
                socketIn.close();
                socketOut.close();
                socket.close();
            }

        }catch(Exception e) {
                out.println("Exception: " + e);
                e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientMain c = new ClientMain();
        c.startClient();
    }

    public Boolean getSocketConnection() {
        return socketConnection;
    }

    public void setSocketConnection(Boolean socketConnection) {
        this.socketConnection = socketConnection;
    }
}
