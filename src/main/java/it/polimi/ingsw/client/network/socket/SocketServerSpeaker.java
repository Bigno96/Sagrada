package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.parser.ParserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerSpeaker implements ServerSpeaker{

    private String ip;
    private Socket socket;
    private static PrintWriter socketOut;
    private final ViewInterface view;
    private Boolean logged;
    private final CommunicationParser protocol;
    private final Object lock = new Object();

    public SocketServerSpeaker(String ip, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.logged = null;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
    }

    void interrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
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
        NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();

        view.print(protocol.getMessage("USER_CONNECTING") + ip);

        try {
            socket = new Socket(ip, parser.getSocketPort());
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(new SocketServerListener(socket, view, this));

            synchronized (lock) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println(protocol.getMessage("CONNECT"));       // asking for connection
                socketOut.println(username);                                    // username passed
                socketOut.flush();
            }

            return true;

        } catch(IOException e) {
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
            synchronized (lock) {
                socketOut = new PrintWriter(socket.getOutputStream());

                socketOut.println(protocol.getMessage("LOGIN"));             // ask for login
                socketOut.println(username);                                        // username passed
                socketOut.flush();
            }

            synchronized (this) {
                while (logged == null)              // while server hasn't responded
                    wait(100);
            }

            if (!logged)
                return false;

            synchronized (lock) {
                socketOut.println(protocol.getMessage("PRINT"));
                socketOut.println("User " + username + " " + protocol.getMessage("LOGIN_SUCCESS"));      // inform server login was successful
                socketOut.flush();
            }

            return true;

        } catch (IOException | InterruptedException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    @Override
    public void setWindowCard(String username, String name) {

    }

    @Override
    public void askWindowCard(String username) {

    }

    @Override
    public void askUsers(String currUser) {

    }

    @Override
    public void askDraft(String username) throws RemoteException, IDNotFoundException, SameDiceException {

    }

    @Override
    public void askPublObj(String username) throws RemoteException {

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
