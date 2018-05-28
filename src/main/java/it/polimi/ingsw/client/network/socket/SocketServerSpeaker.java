package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.SamePlayerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SocketServerSpeaker implements ServerSpeaker{

    private String ip;
    private Socket socket;
    private PrintWriter socketOut;
    private ViewInterface view;
    private final Semaphore go;
    private Boolean connected;

    public SocketServerSpeaker(String ip, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.connected = false;
        this.go = new Semaphore(0, true);
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    synchronized void pong() {
        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            view.print(e.getMessage());
        }
        socketOut.println("pong");
        socketOut.flush();
    }

    void setConnected(Boolean connected) {
        this.connected = connected;
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     * @throws SamePlayerException when trying to login same player twice
     */
    @Override
    public boolean connect(String username) throws SamePlayerException {
        view.print("Trying to connect to " + ip);

        try {
            socket = new Socket(ip, 5000);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(new SocketServerListener(socket, view, this, go));

            synchronized (this) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println("connect");       // asking for connection
                socketOut.println(username);        // username passed
                socketOut.flush();
            }

            go.acquire();

            if (!connected)
                throw new SamePlayerException();

            synchronized (this) {
                socketOut.println("print");
                socketOut.println("User " + username + " successfully connected");      // print on server the success
                socketOut.flush();
            }

            return true;

        } catch(IOException | InterruptedException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean login(String username) {
        try {
            synchronized (this) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println("addPlayer");                 // ask for login
                socketOut.println(username);                    // username passed
                socketOut.flush();
            }

            go.acquire();

            synchronized (this) {
                socketOut.println("print");
                socketOut.println("User " + username + " successfully logged in");      // inform server login was successful
                socketOut.flush();
            }

            return true;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void setWindowCard(String name) {

    }

    @Override
    public void endTurn(String username) {

    }

    @Override
    public void moveDiceFromDraftToCard(int index, int row, int col) {

    }

}
