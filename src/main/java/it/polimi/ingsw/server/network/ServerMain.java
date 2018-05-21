package it.polimi.ingsw.server.network;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ServerMain {
    private static int nClient = 0;
    private int port;
    private boolean a = true;
    private int maxID = 0;

    public ServerMain(int port) {
            this.port = port;
    }

    public static int getnClient() {
        return nClient;
    }

    public static void setnClient(int nClient) {
        ServerMain.nClient = nClient;
    }

    public int getMaxID() {
        return maxID;
    }

    public static void disconnectionClient(int id){
        nClient--;
        System.out.println("Client" +id +"go out");
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready");
        while (a) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New Client");
                executor.submit(new ClientSocketHandler(socket,maxID));
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
/*
    public void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server ready");
            System.out.flush();

            serverListening(serverSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serverListening(ServerSocket serverSocket) {
    ExecutorService executor = Executors.newCachedThreadPool();
        while (a) {
            try {
                Socket socket = serverSocket.accept();
                maxID++;
                executor.submit(new ClientSocketHandler(socket,maxID));
                System.out.println("New Client");
                System.out.flush();
            } catch(IOException e) {
                System.out.println(e);
                System.out.flush();
            }
        }*/

    public static void main(String[] args) {
        ServerMain s=new ServerMain(4912);
        s.startServer();
    }


}