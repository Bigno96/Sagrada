package it.polimi.ingsw.server.network.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

public class ServerSocketListener {

    private ServerSocket serverSocket;

    public ServerSocketListener(int portSocket) {
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
                executor.submit(new SocketServerHandler(socket));
                }
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }
}
