package network;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientSocketHandler implements Runnable {

    private final ThreadLocal<Integer> ID = ThreadLocal.withInitial(() -> 0);
    private Socket socket;

    public ClientSocketHandler(Socket socket, int ID) {
        this.socket = socket;
        this.ID.set(ID);
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while (true) {
                String line = in.nextLine();
                out.println("Hello");
                out.flush();
                if (line.equals("quit") && !(socket.isConnected())) {
                    if(!(socket.isConnected())){
                        socket.wait(300);
                        if(!(socket.isConnected())){
                            break;
                        }
                    }else {
                        break;
                    }
                }
            }
            ServerMain.disconnectionClient();
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
        return ID.get();
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }
}
