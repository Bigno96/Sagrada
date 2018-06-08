package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.controller.lobby.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

/**
 * Class that listens on server socket for connection
 */
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
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketClientSpeaker(socket, lobby));
            }
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
