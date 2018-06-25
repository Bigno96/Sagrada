package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implementation of Rmi version of server speaker
 */
public class RmiServerSpeaker implements ServerSpeaker {

    private static final String USER_CONNECTING_KEYWORD = "USER_CONNECTING";
    private static final String SERVER_REMOTE_KEYWORD = "SERVER_REMOTE";
    private static final String LOGIN_SUCCESS_KEYWORD = "LOGIN_SUCCESS";
    private static final String SERVER_NOT_RESPONDING_KEYWORD = "SERVER_NOT_RESPONDING";

    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server
    private final ViewInterface view;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    public RmiServerSpeaker(String ip, String username, ViewInterface view) {
        this.view = view;
        this.ip = ip;
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

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
        view.print(dictionary.getMessage(USER_CONNECTING_KEYWORD) + ip);

        NetworkInfoParser parser = (NetworkInfoParser) ParserManager.getNetworkInfoParser();

        try {
            Registry registry = LocateRegistry.getRegistry(ip, parser.getRmiServerPort());
            server = (ServerRemote) registry.lookup(protocol.getMessage(SERVER_REMOTE_KEYWORD));        // find remote interface

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
            server.tell("User " + username + " " + protocol.getMessage(LOGIN_SUCCESS_KEYWORD));
            return true;

        } catch (SamePlayerException | TooManyPlayersException | GameAlreadyStartedException | RemoteException e) {
            view.print(e.getMessage());
            return false;
        }
    }

    /**
     * @param username = Player.getId()
     * @param cardName = Player.getWindowCard().getName()
     */
    @Override
    public void setWindowCard(String username, String cardName) {
        try {
            server.setWindowCard(username, cardName);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param usernameWanted = Player.getId()
    * @param me username of player that requested
     */
    @Override
    public void askWindowCard(String usernameWanted, String me) {
        try {
            server.getWindowCard(usernameWanted, me);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param currentUser username of player that requested
     */
    @Override
    public void getAllUsername(String currentUser) {
        try {
            server.getAllUsername(currentUser);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askDraft(String username) {
        try {
            server.getDraft(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username of player that requested
     */
    @Override
    public void askPublicObj(String username) {
        try {
            server.getPublicObj(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    /**
     * @param username = Player.getId() && Player.getPrivateObj()
     */
    @Override
    public void askPrivateObj(String username) {
        try {
            server.getPrivateObj(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    @Override
    public void askToolCards(String username) {

    }

    @Override
    public void askFavorPoints(String username) {

    }

    /**
     * @param username of Player that wants to end his turn
     */
    @Override
    public void endTurn(String username) {
        try {
            server.endTurn(username);
        } catch (RemoteException e) {
            view.print(dictionary.getMessage(SERVER_NOT_RESPONDING_KEYWORD));
        }
    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) {

    }
}


