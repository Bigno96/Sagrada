package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;

import java.rmi.RemoteException;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private Lobby lobby;
    private CommunicationParser protocol;

    public ServerRemoteImpl(Lobby lobby) {
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.lobby = lobby;
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) {
        try {
            out.println(client.getUsername() + protocol.getMessage("CONNECTION_WITH_RMI"));

            client.tell(protocol.getMessage("CONNECTION_SUCCESS"));

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
    public void setWindowCard(String userName, String cardName) {
        lobby.getGameController().setWindow(userName, cardName);
    }

    @Override
    public void askWindowCard(String userName) {
        lobby.getSpeakers().get(userName).printWindowCard(lobby.getPlayers().get(userName).getWindowCard());
    }

    @Override
    public void askUsers(String currUser) {
        for (String u : lobby.getPlayers().keySet())
            if (!u.equals(currUser))
                lobby.getSpeakers().get(currUser).tell(u);
    }

    @Override
    public void askDraft(String username) {
        lobby.getSpeakers().get(username).showDraft(lobby.getGame().getBoard().getDraft());
    }

    @Override
    public void askPublObj(String username) {
        lobby.getSpeakers().get(username).printPublObj(lobby.getGame().getBoard().getPublObj());
    }

    @Override
    public void askPrivObj(String username) {
        lobby.getSpeakers().get(username).printPrivObj(lobby.getPlayers().get(username).getPrivObj());
    }

    @Override
    public void moveDiceFromDraftToCard(String username, int index, int row, int col) {

    }

}
