package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implementation of the remote methods of the client
 */
public class RmiServerSpeaker implements ServerSpeaker {

    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server
    private final ViewInterface view;
    private final CommunicationParser protocol;

    public RmiServerSpeaker(String ip, String username, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();

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
     */
    @Override
    public boolean connect(String username) {
        view.print(protocol.getMessage("USER_CONNECTING") + ip);

        NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();

        try {
            Registry registry = LocateRegistry.getRegistry(ip, parser.getRmiServerPort());
            server = (ServerRemote) registry.lookup(protocol.getMessage("SERVER_REMOTE"));        // find remote interface

            server.connect(username, client);

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
            server.login(username, client);                             // add this player to a game Lobby
            server.tell("User " + username + " " + protocol.getMessage("USER_LOGGED"));
            return true;

        } catch (SamePlayerException | TooManyPlayersException | GameAlreadyStartedException | RemoteException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    @Override
    public void setWindowCard(String username, String name) {
        try {
            server.setWindowCard(username, name);
        } catch (RemoteException e) {
            view.print("Server is not responding");
        }
    }

    @Override
    public void askWindowCard(String username) throws RemoteException, IDNotFoundException {
        server.askWindowCard(username);
    }

    @Override
    public void askUsers(String currUser) throws RemoteException {
        server.askUsers(currUser);
    }

    @Override
    public void askDraft(String username) throws RemoteException, IDNotFoundException, SameDiceException {
        server.askDraft(username);
    }

    @Override
    public void askPublObj(String username) throws RemoteException {
        server.askPublObj(username);
    }

    @Override
    public void askPrivObj(String username) throws RemoteException {
        server.askPrivObj(username);
    }

    @Override
    public void endTurn(String username) {

    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException {
        server.moveDiceFromDraftToCard(username, index, row, col);
    }
}


