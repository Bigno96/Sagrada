package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.game.Game;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.game.Round;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RoundTest extends TestCase {

    private int nPlayer = 2;
    private Board board = new Board(nPlayer);

    public RoundTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    /**
     * @return List of nPlayer different Player
     */
    private List<Player> myPlayerList() {
        List<Player> playerList = new ArrayList<>();

        IntStream.range(0, nPlayer).forEach(i -> {
            Player p = new Player("test"+i);
            p.setBoard(board);
            playerList.add(p);
        });

        return playerList;
    }

    /**
     * Testing the correct player who has to play
     * @throws PlayerNotFoundException thrown by getPlayer when player is not found
     */
    public void testNextPlayer() throws PlayerNotFoundException {
        List<Player> list = myPlayerList();
        Round round = new Round(list, new Game());

        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertNull(round.nextPlayer());
    }

    /**
     * Testing the correct round sequence
     * @throws PlayerNotFoundException thrown by getPlayer when player is not found
     * @throws SameDiceException when move Draft finds a dice already in round Track
     * @throws EmptyException when finds an empty dice bag
     * @throws IDNotFoundException when internal error on adding dice occurs
     */
    public void testNextRound() throws PlayerNotFoundException, SameDiceException, EmptyException, IDNotFoundException {
        List<Player> list = myPlayerList();
        Game game = new Game();
        game.startGame();
        Round round = new Round(list, game);

        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertNull(round.nextPlayer());

        game.setNumRound(1);
        round.nextRound();

        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertNull(round.nextPlayer());
    }

    /**
     * Testing getter for player
     * @throws PlayerNotFoundException thrown by getPlayer when player is not found
     */
    public void testGetPlayer() throws PlayerNotFoundException {
        Player player = new Player("test");
        List<Player> list = new ArrayList<>();
        list.add(player);
        Round round = new Round(list, new Game());

        assertEquals(player, round.getPlayer("test"));
    }

    /**
     * Testing throws by getPlayer when player is not found
     */
    public void testGetPlayerException() {
        List<Player> list = myPlayerList();
        Round round = new Round(list, new Game());

        assertThrows(PlayerNotFoundException.class, () -> round.getPlayer("test"+10));
    }
}