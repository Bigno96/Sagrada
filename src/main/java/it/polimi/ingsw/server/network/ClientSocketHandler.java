package it.polimi.ingsw.server.network;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientSocketHandler implements Runnable {

    private int ID;
    private Socket socket;

    public ClientSocketHandler(Socket socket, int ID) {
        this.socket = socket;
        this.ID = ID;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Open connection");
            out.flush();

            while (true) {
                String line = in.nextLine();
                if (line.equals("q") && !(socket.isConnected())) {
                    if(!(socket.isConnected())){
                        socket.wait(300);
                        if(!(socket.isConnected())){
                            out.println("Close connection");
                            break;
                        }
                    }else {
                        out.println("Close connection");
                        break;
                    }
                }
            }
            ServerMain.disconnectionClient(ID);
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
