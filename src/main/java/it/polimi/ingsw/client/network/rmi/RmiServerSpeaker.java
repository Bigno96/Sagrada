package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.System.*;

public class RmiServerSpeaker implements ServerSpeaker {
    // realize the comm Client -> Server using rmi
    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server

    public RmiServerSpeaker(String ip, String username) {
        this.ip = ip;
        try {
            this.client = new ClientRemoteImpl(username);
        } catch (RemoteException e) {
            out.println(e.getMessage());
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
        out.println("Trying to connect to " + ip);

        try {
            Registry registry = LocateRegistry.getRegistry(ip, 4500);
            server = (ServerRemote) registry.lookup("Server_Interface");        // find remote interface

            server.connect(username, client);                                             // logs to server
            server.tell("User " + username + " successfully connected");            // tells logged successfully

            return true;

        } catch (RemoteException | NotBoundException e) {
            out.print(e.getMessage());
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
            out.println("Too many players in Lobby");
            return false;

        } catch (GameAlreadyStartedException e) {
            out.println("Game is already started");
            return false;

        } catch (RemoteException e) {
            out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public void setWindowCard(int id) {

    }

    @Override
    public void endTurn(String username) {

    }

    @Override
    public void moveDiceFromDraftToCard(int index, int row, int col) {

    }
}
