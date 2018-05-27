package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.TimerTask;

public class DisconnectionDaemon extends TimerTask {

    private Socket socket;
    private ViewInterface view;

    DisconnectionDaemon(Socket socket, ViewInterface view) {
        this.socket = socket;
        this.view = view;
    }

    @Override
    public synchronized void run() {
        try {
            Scanner socketIn = new Scanner(socket.getInputStream());
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

            if (socketIn.nextLine().equals("ping")) {
                socketOut.println("true");
                socketOut.flush();
            }

        } catch (IOException e) {
            view.print(e.getMessage());
        }
    }
}
