package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.network.ClientSpeaker;

import java.rmi.RemoteException;

import static java.lang.System.out;

public class ServerRemoteImpl implements ServerRemote {

    private Lobby lobby;

    public ServerRemoteImpl(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * @param user   != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public void connect(String user, ClientRemote client) {
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
     * @param user   != null
     * @param client instance of ClientRemote
     * @throws TooManyPlayersException when trying to login more than 4 player together
     * @throws SamePlayerException when trying to login same player twice
     * @throws GameAlreadyStartedException when trying to login after game already started
     */
    @Override
    public void addPlayer(String user, ClientRemote client) throws TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        ClientSpeaker speaker = new RmiClientSpeaker(client);
        lobby.addPlayerLobby(user, speaker);
    }

}
