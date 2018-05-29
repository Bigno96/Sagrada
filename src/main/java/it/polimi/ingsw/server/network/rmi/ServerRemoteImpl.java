package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.controller.CheckDisconnectionDaemon;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.RemoteException;
import java.util.Timer;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private Lobby lobby;

    public ServerRemoteImpl(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) {
        try {
            out.println("User " + client.getUsername() + " is connecting with RMI");

            client.tell("Connection established");

        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    /**
     * @param s to be printed
     */
    @Override
    public void tell(String s) {
        out.println(s);
    }

    /**
     * @param username   != null
     * @param client instance of ClientRemote
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    @Override
    public synchronized void addPlayer(String username, ClientRemote client) throws TooManyPlayersException, GameAlreadyStartedException, SamePlayerException {
        RmiClientSpeaker speaker = new RmiClientSpeaker(client);

        lobby.addPlayerLobby(username, speaker);

        Timer disconnection = new Timer();
        disconnection.scheduleAtFixedRate(new CheckDisconnectionDaemon(speaker, lobby, username), 0,1000);
    }

    @Override
    public void setWindowCard(String userName, String name) {
        lobby.setWindowCard(userName, name);
    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) throws RemoteException {
       lobby.moveDiceFromDraftToCard(username, index, row, col);
    }


}
