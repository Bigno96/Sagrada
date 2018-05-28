package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServerSpeaker implements ServerSpeaker {
    // realize the comm Client -> Server using rmi
    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server
    private ViewInterface view;

    public RmiServerSpeaker(String ip, String username, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        try {
            this.client = new ClientRemoteImpl(username, view);
        } catch (RemoteException e) {
            view.print(e.getMessage());
        }
    }

    /**
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    @Override
    public void setIp(String ip) {
        this.ip = ip;
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
            Registry registry = LocateRegistry.getRegistry(ip, 4500);
            server = (ServerRemote) registry.lookup("Server_Interface");        // find remote interface

            server.connect(username, client);                                             // logs to server
            server.tell("User " + username + " successfully connected");            // tells logged successfully

            return true;

        } catch (RemoteException | NotBoundException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username != null
     * @return true if login was successful, false else
     */
    @Override
    public boolean login(String username) {
        try {
            server.addPlayer(username, client);                             // add this player to a game Lobby
            server.tell("User " + username + " successfully logged in");
            return true;

        } catch (TooManyPlayersException e) {
            view.print("Too many players in Lobby");
            return false;

        } catch (GameAlreadyStartedException e) {
            view.print("Game is already started");
            return false;

        } catch (RemoteException e) {
            view.print(e.getMessage());
            return false;
        }

    }

    @Override
    public void setWindowCard(String name) {

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
    public void moveDiceFromDraftToCard(int index, int row, int col) {

    }
}
