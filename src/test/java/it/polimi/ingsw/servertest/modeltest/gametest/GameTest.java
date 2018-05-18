package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.game.Round;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest extends TestCase {

    private int nPlayer = 2;
    private Board board = new Board(nPlayer);
    private static final Random random = new Random();
    private int id = random.nextInt(20);

    public GameTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    public void testGetter() throws SamePlayerException, IDNotFoundException, FileNotFoundException {
        Game game = new Game();
        Player p = new Player(id);
        Player p1 = new Player(id+1);
        Player p2 = new Player(id+2);
        List<Player> list = new ArrayList<>();
        list.add(p);
        list.add(p1);
        list.add(p2);
        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        int nRound = game.getNRound();
        int nPlayer = game.getNPlayer();
        Board board = game.getBoard();
        Round round = game.getRound();

        assertEquals(nRound, game.getNRound());
        assertEquals(nPlayer, game.getNPlayer());
        assertEquals(board, game.getBoard());
        assertEquals(round, game.getRound());
        assertEquals(list, game.getPlayerList());

    }

    public void testStartGame() throws SamePlayerException, IDNotFoundException, FileNotFoundException {
        Game game = new Game();
        Player p = new Player(id);
        Player p1 = new Player(id+1);
        Player p2 = new Player(id+2);
        List<ObjectiveCard> list;

        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        assertNotNull(game.getRound());
        assertNotNull(game.getBoard());
        list = game.getBoard().getPublObj();
        assertEquals(list, game.getBoard().getPublObj());

        for (Player player: game.getPlayerList()){
            assertNotNull(player.getPrivObj());
        }

    }

    public void testAddPlayer() throws SamePlayerException, EmptyException, PlayerNotFoundException {
        Game game = new Game();
        Player p = new Player(id);

        assertTrue(game.addPlayer(p));
        assertTrue(game.findPlayer(p));
    }

    public void testSamePlayerException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id);

        assertTrue(game.addPlayer(p));
        assertThrows(SamePlayerException.class, () -> game.addPlayer(p));
    }

    public void testPlayerNotFoundException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id);
        Player pDiff = new Player(id+1);

        assertTrue(game.addPlayer(p));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(pDiff));
    }

    public void testCurrentPlayer() throws SamePlayerException, IDNotFoundException, FileNotFoundException {
        Game game = new Game();
        Player p = new Player(id);
        Player p1 = new Player(id+1);
        game.addPlayer(p);
        game.addPlayer(p1);

        game.startGame();

        assertEquals(p, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());
        assertEquals(p, game.currentPlayer());

        game.getRound().nextRound();

        assertEquals(p1, game.currentPlayer());
        assertEquals(p, game.currentPlayer());
        assertEquals(p, game.currentPlayer());
        assertEquals(p1, game.currentPlayer());

        game.setnRound(9);
        assertNull(game.currentPlayer());
    }

    public void testRmPlayer() throws SamePlayerException, EmptyException {
        Game game = new Game();
        Player p = new Player(id);
        Player pDiff = new Player(id+1);

        assertThrows(EmptyException.class, () -> game.rmPlayer(p));
        assertThrows(EmptyException.class, () -> game.findPlayer(p));
        assertTrue(game.addPlayer(p));
        assertTrue(game.rmPlayer(p));
        assertTrue(game.addPlayer(pDiff));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(p));
    }
}