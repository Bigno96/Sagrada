package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Player;
import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest extends TestCase {

    private final static String id1 = "Giovanni";
    private final static String id2 = "Federico";
    private final static String id3 = "Andrea";
    private final static String idDiff = " ";

    public GameTest(String testName) {
        super(testName);
    }

    /**
     * Testing getter of game class
     * @throws SamePlayerException when trying to add same player twice
     */
    public void testGetter() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);
        Player p2 = new Player(id3);

        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        assertEquals(0, game.getNumRound());
        assertEquals(3, game.getNumPlayer());

        assertNotNull(game.getBoard());
        assertSame(p.getBoard(), game.getBoard());
        assertSame(p1.getBoard(), game.getBoard());
        assertSame(p2.getBoard(), game.getBoard());

        assertNotNull(game.getRound());
    }

    /**
     * Testing a correct starting of the game and correct finding of the player
     * @throws SamePlayerException when trying to add same player twice
     */
    public void testStartGame() throws SamePlayerException, EmptyException, PlayerNotFoundException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);
        Player p2 = new Player(id3);

        assertThrows(EmptyException.class, () -> game.findPlayer(id1));

        game.addPlayer(p);
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.startGame();

        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(idDiff));

        assertSame(p, game.findPlayer(p.getId()));
        assertSame(p1, game.findPlayer(p1.getId()));
        assertSame(p2, game.findPlayer(p2.getId()));
    }

    /**
     * Testing the correct addition of a player
     * @throws SamePlayerException when trying to add same player twice
     * @throws EmptyException when trying to find a player in an empty game
     * @throws PlayerNotFoundException when player is not in game
     */
    public void testAddPlayer() throws SamePlayerException, EmptyException, PlayerNotFoundException {
        Game game = new Game();
        Player p = new Player(id1);

        assertTrue(game.addPlayer(p));
        assertSame(p, game.findPlayer(p.getId()));
    }

    /**
     * Test of behavior when adding while the same player is already in the game
     * @throws SamePlayerException when trying to add same player twice
     */
    public void testSamePlayerException() throws SamePlayerException {
        Game game = new Game();
        Player p = new Player(id1);

        assertTrue(game.addPlayer(p));
        assertThrows(SamePlayerException.class, () -> game.addPlayer(p));
    }

    /**
     * Test of behavior when finding a non-present player in the game
     * @throws SamePlayerException when trying to add same player twice
     * @throws EmptyException when trying to find a player in an empty game
     */
    public void testPlayerNotFoundException() throws SamePlayerException, EmptyException {
        Game game = new Game();
        Player p = new Player(id1);
        Player pDiff = new Player(id2);

        assertTrue(game.addPlayer(p));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(pDiff.getId()));

        assertTrue(game.rmPlayer(p));
        assertThrows(EmptyException.class, () -> game.findPlayer(p.getId()));

        assertTrue(game.addPlayer(pDiff));
        assertThrows(PlayerNotFoundException.class, () -> game.findPlayer(p.getId()));
    }

    /**
     * Testing the correct order of the turn
     * @throws SamePlayerException when trying to add same player twice
     * @throws EmptyException when finds an empty dice bag
     * @throws IDNotFoundException when internal error on adding dice occurs
     */
    public void testCurrentPlayer() throws SamePlayerException, SameDiceException, EmptyException, IDNotFoundException {
        Game game = new Game();
        Player p = new Player(id1);
        Player p1 = new Player(id2);

        game.addPlayer(p);
        game.addPlayer(p1);

        game.startGame();

        assertSame(0, game.getNumRound());

        assertEquals(p, game.nextPlayer());
        assertEquals(p1, game.nextPlayer());
        assertEquals(p1, game.nextPlayer());
        assertEquals(p, game.nextPlayer());

        assertEquals(p1, game.nextPlayer());
        assertEquals(p, game.nextPlayer());
        assertEquals(p, game.nextPlayer());
        assertEquals(p1, game.nextPlayer());

        assertSame(1, game.getNumRound());

        game.setNumRound(9);
        assertNull(game.nextPlayer());
    }

    /**
     * Testing the correct removal of a player
     * @throws SamePlayerException when trying to add same player twice
     * @throws EmptyException when trying to find a player in an empty game
     */
    public void testRmPlayer() throws SamePlayerException, EmptyException {
        Game game = new Game();
        Player p = new Player(id1);

        assertThrows(EmptyException.class, () -> game.rmPlayer(p));
        assertThrows(EmptyException.class, () -> game.findPlayer(p.getId()));

        assertTrue(game.addPlayer(p));
        assertTrue(game.rmPlayer(p));
    }
}