package it.polimi.ingsw.server;

import it.polimi.ingsw.server.network.RmiServerHandler;
import it.polimi.ingsw.server.network.SocketServerHandler;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import static java.lang.System.*;

public class ServerMain {
    private int id;
    private int portSocket = 5000;
    private ServerSocket serverSocket;
    private RmiServerHandler rmiStub;

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        server.startServer();
    }

    private ServerMain() {
        this.id = 0;
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(portSocket);
            rmiStub = new RmiServerHandler();

            out.println("Server ready");

        } catch (IOException e) {
            out.println(e.getMessage());
        }

        serverListening();
    }

    private void serverListening() {
        Boolean exit = true;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            while (exit) {
                synchronized(this) {
                    Socket socket = serverSocket.accept();
                    id++;
                    executor.submit(new SocketServerHandler(socket, id));
                }
            }

        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

}