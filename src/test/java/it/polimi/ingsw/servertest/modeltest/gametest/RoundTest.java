package it.polimi.ingsw.servertest.modeltest.gametest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.game.Round;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;

public class RoundTest extends TestCase {

    private int nPlayer = 2;
    private Board board = new Board(nPlayer);

    public RoundTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    private List<Player> myCellList() {
        List<Player> playerList = new ArrayList<>();
        for (int i=0; i<nPlayer; i++) {
            Player p = new Player("test"+i);
            p.setBoard(board);
            playerList.add(p);
        }
        return playerList;
    }

    public void testNextPlayer() throws PlayerNotFoundException {
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertNull(round.nextPlayer());
    }


    public void testNextRound() throws PlayerNotFoundException {
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertNull(round.nextPlayer());

        round.nextRound();

        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+0), round.nextPlayer());
        assertSame(round.getPlayer("test"+1), round.nextPlayer());
        assertNull(round.nextPlayer());

        assertNotSame(round.toString(), round.getPlayer("test"+0).toString());
    }

    public void testGetPlayer() throws PlayerNotFoundException{
        List<Player> list = myCellList();
        Round round = new Round(list);
        Player player = round.getPlayer("test"+0);
        assertEquals(player, round.getPlayer("test"+0));
    }

    public void testGetPlayerException(){
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertThrows(PlayerNotFoundException.class, () -> round.getPlayer("test"+10));
    }
}