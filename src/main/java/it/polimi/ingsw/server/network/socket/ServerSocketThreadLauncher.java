package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

public class ServerSocketThreadLauncher {

    private ServerSocket serverSocket;
    private Lobby lobby;

    public ServerSocketThreadLauncher(int portSocket, Lobby lobby) {
        this.lobby = lobby;
        try {
            this.serverSocket = new ServerSocket(portSocket);
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

    public void serverListening() {
        Boolean exit = true;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            while (exit) {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketClientHandler(socket, lobby));
                }
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
