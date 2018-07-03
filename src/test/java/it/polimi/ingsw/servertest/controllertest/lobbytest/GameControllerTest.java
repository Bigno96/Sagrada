package it.polimi.ingsw.servertest.controllertest.lobbytest;

import it.polimi.ingsw.client.network.rmi.ClientRemote;
import it.polimi.ingsw.client.network.rmi.ClientRemoteImpl;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.controller.game.GameController;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.network.ClientSpeaker;
import it.polimi.ingsw.server.network.rmi.RmiClientSpeaker;
import junit.framework.TestCase;

import java.rmi.RemoteException;
import java.util.*;

public class GameControllerTest extends TestCase {

    private static final String NAME = "Test";
    private static final String USERNAME1 = "Test1";
    private static final String USERNAME2 = "Test2";

    private static final ViewInterface view = new CliSystem();

    private static final Random random = new Random();
    private final int id = random.nextInt(20);
    private final int fp = random.nextInt(4)+3;

    private final GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    private final int max_row = gameSettings.getWindowCardMaxRow();
    private final int max_col = gameSettings.getWindowCardMaxColumn();
    private static final int MIN = 0;

    public GameControllerTest(String testName) {
        super(testName);
    }

    /**
     * Fills a list of cell with 20 random cells
     * @return List<Cell> list, list.size() = 20
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=MIN; i<max_row; i++)
            for (int j=MIN; j<max_col; j++)
                cellList.add(new Cell(random.nextInt(7), Colors.random(), i, j, max_row, max_col));

        return cellList;
    }

    /**
     * Testing checking that all players have selected their window card
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     */
    public void testAllCardsAreSelected() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException, ValueException, PositionException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());

        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        assertFalse(gameController.allCardsAreSelected());

        lobby.getPlayers().get(USERNAME1).setWindowCard(new WindowCard(id, NAME, fp, myCellList(), max_row, max_col));
        lobby.getPlayers().get(USERNAME2).setWindowCard(new WindowCard(id, NAME, fp, myCellList(), max_row, max_col));

        assertTrue(gameController.allCardsAreSelected());
    }

    /**
     * Testing creating a list of N random numbers
     */
    public void testCreateNRandom() {
        Lobby lobby = new Lobby();
        HashMap<String, Player> players = new HashMap<>();
        GameController gameController = new GameController(lobby, players);

        List<Integer> used = Arrays.asList(0, 2, 4, 5);
        int bound = 10;
        int n = random.nextInt(bound)+1;
        int max = random.nextInt(bound) + (bound * bound);

        List<Integer> rand = gameController.createNRandom(n, used, max);

        assertSame(n, rand.size());
        assertTrue(!rand.containsAll(used));
    }

    /**
     * Testing attaching observer
     */
    public void testSetObserver() {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());

        lobby.getGame().startGame();
        gameController.setObserver();

        lobby.getPlayers().forEach((user, player) -> assertSame(2, player.countObservers()));
        assertSame(1, lobby.getGame().countObservers());
        assertSame(2, lobby.getGame().getBoard().countObservers());
        assertSame(1, lobby.getGame().getBoard().getRoundTrack().countObservers());
        assertSame(1, lobby.getGame().getBoard().getDraft().countObservers());
    }

    /**
     * Testing getting window alternatives for each player
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testGetListWindowCard() throws TooManyPlayersException, SamePlayerException, GameAlreadyStartedException, RemoteException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());
        List<Integer> used = new ArrayList<>();

        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        lobby.getGame().startGame();
        gameController.getListWindowCard(used);

        Map<String, List<WindowCard>> windowAlternatives = gameController.getWindowAlternatives();

        assertSame(2, windowAlternatives.size());
        windowAlternatives.forEach((name, listCards) -> {
            assertSame(4, listCards.size());
            listCards.forEach(card -> gameController.isCardValidForPlayer(name, card.getName()));
        });
    }

    /**
     * Testing setting a window card for a player
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     * @throws ValueException thrown when passing wrong values to cell
     * @throws PositionException thrown when passing wrong coordinates to cell
     * @throws EmptyException when trying to find player in an empty game
     * @throws PlayerNotFoundException when player is not found in the lobby
     */
    public void testSetWindowCard() throws TooManyPlayersException, SamePlayerException, GameAlreadyStartedException, RemoteException, ValueException, PositionException, EmptyException, PlayerNotFoundException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());
        List<Integer> used = new ArrayList<>();
        WindowCard wrongCard = new WindowCard(id, NAME, fp, myCellList(), max_row, max_col);

        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        lobby.startingGame();
        lobby.getGame().startGame();
        gameController.getListWindowCard(used);

        gameController.setWindowCard(USERNAME1, wrongCard.getName());
        assertNull(lobby.getPlayers().get(USERNAME1).getWindowCard());

        gameController.setWindowCard(USERNAME2, wrongCard.getName());
        assertNull(lobby.getPlayers().get(USERNAME2).getWindowCard());

        int pick = random.nextInt(4);

        gameController.setWindowCard(USERNAME1, gameController.getWindowAlternatives().get(USERNAME1).get(pick).getName());
        assertSame(gameController.getWindowAlternatives().get(USERNAME1).get(pick).getName(),
                lobby.getGame().findPlayer(USERNAME1).getWindowCard().getName());

        gameController.setWindowCard(USERNAME2, gameController.getWindowAlternatives().get(USERNAME2).get(pick).getName());
        assertSame(gameController.getWindowAlternatives().get(USERNAME2).get(pick).getName().trim(),
                lobby.getGame().findPlayer(USERNAME2).getWindowCard().getName().trim());
    }

    /**
     * Testing setting public objective
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testSetPublicObjective() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());
        List<Integer> used = new ArrayList<>();

        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        int wrongId = random.nextInt(20) + 20;
        PublicObjective wrongObj = new PublicObjective(wrongId, "Test", wrongId);

        lobby.startingGame();
        lobby.getGame().startGame();
        gameController.setPublicObjective(used);

        assertFalse(lobby.getGame().getBoard().getPublicObj().contains(wrongObj));
        assertSame(3, lobby.getGame().getBoard().getPublicObj().size());
    }

    /**
     * Testing setting private objective for each player
     * @throws RemoteException by remote client
     * @throws TooManyPlayersException when lobby player limit is exceeded
     * @throws SamePlayerException when trying to add a player with the same id
     * @throws GameAlreadyStartedException when trying to add a player to an already started game
     */
    public void testSetPrivateObjective() throws RemoteException, TooManyPlayersException, SamePlayerException, GameAlreadyStartedException {
        Lobby lobby = new Lobby();
        lobby.startLobby();
        GameController gameController = new GameController(lobby, lobby.getPlayers());
        List<Integer> used = new ArrayList<>();

        int wrongId = random.nextInt(20) + 20;

        ClientRemote clientRemote = new ClientRemoteImpl(USERNAME1, view);
        ClientSpeaker rmiSpeaker1 = new RmiClientSpeaker(clientRemote);
        ClientSpeaker rmiSpeaker2 = new RmiClientSpeaker(clientRemote);

        lobby.addPlayer(USERNAME1, rmiSpeaker1);
        lobby.addPlayer(USERNAME2, rmiSpeaker2);

        lobby.startingGame();
        lobby.getGame().startGame();
        gameController.setPrivateObjective(used);

        lobby.getPlayers().forEach((user, player) -> {
            assertNotSame(wrongId, player.getPrivateObj().getId());
            assertNotNull(player.getPrivateObj());
        });
    }
}
