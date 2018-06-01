package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.GameAlreadyStartedException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.TooManyPlayersException;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.network.parser.CommunicationParser;

import java.rmi.RemoteException;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private Lobby lobby;
    private CommunicationParser communication;

    public ServerRemoteImpl(Lobby lobby) {
        this.communication = new CommunicationParser();
        this.lobby = lobby;
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) {
        try {
            out.println(client.getUsername() + communication.getMessage("CONNECTION_WITH_RMI"));

            client.tell(communication.getMessage("CONNECTION_SUCCESS"));

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
    public synchronized void login(String username, ClientRemote client) throws TooManyPlayersException, GameAlreadyStartedException, SamePlayerException {
        RmiClientSpeaker speaker = new RmiClientSpeaker(client);

        lobby.addPlayer(username, speaker);
    }

    @Override
    public void setWindowCard(String userName, String name) {

    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) {

    }


}
