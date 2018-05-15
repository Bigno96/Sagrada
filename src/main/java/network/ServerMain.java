package network;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ServerMain
{
    private static int nClient = 0;
    private int port;
    private boolean a = true;
    private int maxID = 0;

    public ServerMain() {
            this.port = 4912;
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

    public static void disconnectionClient(){
        nClient--;
    }

    public void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server ready");

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
            } catch(IOException e) {
                System.out.println(e);
            }
        }
}

    public static void main(String[] args) {
        ServerMain s=new ServerMain();
        s.startServer();
    }
}