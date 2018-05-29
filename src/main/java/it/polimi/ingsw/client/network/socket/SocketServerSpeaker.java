package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.ValueException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SocketServerSpeaker implements ServerSpeaker{

    private String ip;
    private Socket socket;
    private PrintWriter socketOut;
    private ViewInterface view;
    private final Semaphore go;
    private Boolean logged;

    public SocketServerSpeaker(String ip, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.logged = true;
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

    void setLogged(Boolean logged) {
        this.logged = logged;
    }

    /**
     * @param username != null
     * @return true if connection was successful, false else
     */
    @Override
    public boolean connect(String username) {
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

            if (!logged) {
                return false;
            }

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
    public void setWindowCard(String username, String name) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

    @Override
    public void askWindowCard(String username) {

    }

    @Override
    public void askUsers(String currUser) {

    }

    @Override
    public void askDraft() {

    }

    @Override
    public void askPublObj() {

    }

    @Override
    public void askPrivObj(String username) {

    }

    @Override
    public void endTurn(String username) {

    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException {

    }

}
