package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;

import java.rmi.RemoteException;

import static java.lang.System.*;

public class ServerRemoteImpl implements ServerRemote {

    private static final String RMI_CONNECTION_KEYWORD = "CONNECTION_WITH_RMI";
    private static final String CONNECTION_SUCCESS_KEYWORD = "CONNECTION_SUCCESS";
    private static final String TURN_PASSED_KEYWORD = "TURN_PASSED";

    private final Lobby lobby;
    private final CommunicationParser protocol;
    private final ViewMessageParser dictionary;

    public ServerRemoteImpl(Lobby lobby) {
        this.protocol = (CommunicationParser) ParserManager.getCommunicationParser();
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.lobby = lobby;
    }

    /**
     * @param username != null
     * @param client instance of ClientRemote, permits server to talk back to client
     */
    @Override
    public synchronized void connect(String username, ClientRemote client) {
        try {
            out.println(client.getUsername() + protocol.getMessage(RMI_CONNECTION_KEYWORD));

            client.tell(dictionary.getMessage(CONNECTION_SUCCESS_KEYWORD));

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

    /**
     * @param username of Player that requested
     * @param cardName of card to be set
     */
    @Override
    public void setWindowCard(String username, String cardName) {
        lobby.getGameController().setWindowCard(username, cardName);
    }


    /**
     * @param usernameWanted = Player.getId() && Player.getWindowCard()
     * @param me             = Player.getId() of who requested
     */
    @Override
    public void getWindowCard(String usernameWanted, String me) {
        lobby.getSpeakers().get(me).printWindowCard(lobby.getPlayers().get(usernameWanted).getWindowCard());
    }

    /**
     * @param currentUser = Player.getId() of who requested
     */
    @Override
    public void getAllUsername(String currentUser) {
        for (String user : lobby.getPlayers().keySet())
            if (!user.equals(currentUser))
                lobby.getSpeakers().get(currentUser).tell(user);
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getDraft(String username) {
        lobby.getSpeakers().get(username).showDraft(lobby.getGame().getBoard().getDraft());
    }

    /**
     * @param username = Player.getId() of who ended turn
     */
    @Override
    public void endTurn(String username) {
        lobby.notifyAllPlayers(username + dictionary.getMessage(TURN_PASSED_KEYWORD));
        lobby.getRoundController().nextTurn();
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getPublicObj(String username) {
        lobby.getSpeakers().get(username).printPublicObj(lobby.getGame().getBoard().getPublicObj());
    }

    /**
     * @param username = Player.getId() of who requested
     */
    @Override
    public void getPrivateObj(String username) {
        lobby.getSpeakers().get(username).printPrivateObj(lobby.getPlayers().get(username).getPrivateObj());
    }

    /**
     * @param username of player moving the dice
     * @param index    in the draft of the dice
     * @param row      of the destination cell
     * @param col      of the destination cell
     */
    @Override
    public void placementDice(String username, int index, int row, int col) throws WrongDiceSelectionException, EmptyException,
            WrongPositionException, NotTurnException, NotEmptyException, IDNotFoundException, WrongCellSelectionException, PositionException {
        lobby.getActionController().placeDice(username, index, row, col);
    }

}
