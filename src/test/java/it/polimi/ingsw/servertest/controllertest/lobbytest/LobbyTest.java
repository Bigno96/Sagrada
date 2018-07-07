package it.polimi.ingsw.servertest.controllertest.lobbytest;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.client.network.rmi.ClientRemoteImpl;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.controller.game.RoundController;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.server.network.rmi.RmiClientSpeaker;
import junit.framework.TestCase;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LobbyTest extends TestCase {

    private static final String USERNAME1 = "Test1";
    private static final String USERNAME2 = "Test2";
    private static final String USERNAME3 = "Test3";
    private static final String USERNAME4 = "Test4";
    private static final String USERNAME5 = "Test5";

    private static final ViewInterface view = new CliSystem();

    public LobbyTest(String testName) {
        super(testName);
    }

    /**
     * Testing addition of players into the lobby and their errors
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testAddPlayer() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker3 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker4 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);
        lobby.addPlayer(USERNAME3, rmiSpeaker3);
        lobby.addPlayer(USERNAME4, rmiSpeaker4);

        assertSame(USERNAME1, lobby.getPlayers().get(USERNAME1).getId());
        assertSame(rmiSpeaker1, lobby.getSpeakers().get(USERNAME1));
        assertSame(USERNAME2, lobby.getPlayers().get(USERNAME2).getId());
        assertSame(rmiSpeaker2, lobby.getSpeakers().get(USERNAME2));
        assertSame(USERNAME3, lobby.getPlayers().get(USERNAME3).getId());
        assertSame(rmiSpeaker3, lobby.getSpeakers().get(USERNAME3));
        assertSame(USERNAME4, lobby.getPlayers().get(USERNAME4).getId());
        assertSame(rmiSpeaker4, lobby.getSpeakers().get(USERNAME4));

        assertThrows(SamePlayerException.class, () -> lobby.addPlayer(USERNAME1, rmiSpeaker1));
        assertThrows(TooManyPlayersException.class, () -> lobby.addPlayer(USERNAME5, rmiSpeaker1));

        lobby.setState(Lobby.gameState.STARTED);

        assertThrows(GameAlreadyStartedException.class, () -> lobby.addPlayer(USERNAME5, rmiSpeaker1));
    }

    /**
     * Testing disconnection of players from the lobby
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testDisconnectPlayer() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.disconnectPlayer(USERNAME1);

        assertNull(lobby.getPlayers().get(USERNAME1));

        lobby.addPlayer(USERNAME2, rmiSpeaker1);
        lobby.startingGame();
        lobby.disconnectPlayer(USERNAME2);

        assertNull(lobby.getPlayers().get(USERNAME2));
    }

    /**
     * Testing reconnection of a player who has disconnected while game was starting or started
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testReconnectPlayer() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.startingGame();
        lobby.setState(Lobby.gameState.STARTING);
        lobby.disconnectPlayer(USERNAME1);

        assertNull(lobby.getPlayers().get(USERNAME1));

        lobby.addPlayer(USERNAME1, rmiSpeaker1);

        assertFalse(lobby.getPlayers().get(USERNAME1).isDisconnected());
    }

    /**
     * Testing removing a player
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testRemovePlayer() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);
        lobby.startingGame();
        lobby.removePlayer(USERNAME1);

        assertThrows(PlayerNotFoundException.class, () -> lobby.getGame().findPlayer(USERNAME1));
        assertNull(lobby.getPlayers().get(USERNAME1));
        assertNull(lobby.getSpeakers().get(USERNAME1));
    }

    /**
     * Testing starting game
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     * @throws EmptyException when trying to find player in an empty game
     * @throws PlayerNotFoundException when player is not found in the lobby
     */
    public void testStartingGame() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException, EmptyException, PlayerNotFoundException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);
        lobby.startingGame();

        assertSame(Lobby.gameState.STARTING, lobby.getState());
        assertSame(USERNAME1, lobby.getGame().findPlayer(USERNAME1).getId());
        assertSame(USERNAME2, lobby.getGame().findPlayer(USERNAME2).getId());
    }

    /**
     * Testing starting round counting and roll first draft
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testStartCountingRound() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);
        lobby.startingGame();
        lobby.getGame().startGame();

        lobby.startCountingRound();

        lobby.getGame().getBoard().getDraft().getDraftList().forEach(dice ->
            assertTrue(dice.getValue() > 0 && dice.getValue() < 7)
        );
        assertSame(USERNAME1, lobby.getGame().getCurrentPlayer().getId());
    }

    /**
     * Testing various getter of lobby
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testGetter() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        lobby.startingGame();
        lobby.getGame().startGame();
        lobby.startCountingRound();

        assertSame(Game.class, lobby.getGame().getClass());
        assertSame(RoundController.class, lobby.getRoundController().getClass());

        assertTrue(lobby.getPlayers().containsKey(USERNAME1));
        assertTrue(lobby.getPlayers().containsKey(USERNAME2));
        assertSame(USERNAME1, lobby.getPlayers().get(USERNAME1).getId());
        assertSame(USERNAME2, lobby.getPlayers().get(USERNAME2).getId());

        assertTrue(lobby.getSpeakers().containsKey(USERNAME1));
        assertTrue(lobby.getSpeakers().containsKey(USERNAME2));
        assertSame(rmiSpeaker1, lobby.getSpeakers().get(USERNAME1));
        assertSame(rmiSpeaker2, lobby.getSpeakers().get(USERNAME2));

        lobby.setState(Lobby.gameState.WAITING);
        assertSame(Lobby.gameState.WAITING, lobby.getState());

        lobby.setState(Lobby.gameState.STARTING);
        assertSame(Lobby.gameState.STARTING, lobby.getState());

        lobby.setState(Lobby.gameState.STARTED);
        assertSame(Lobby.gameState.STARTED, lobby.getState());
    }
}
