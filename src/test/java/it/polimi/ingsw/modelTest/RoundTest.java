package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import junit.framework.TestCase;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;

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
        for (int i=0; i<nPlayer; i++)
                playerList.add(new Player(i, board));
        return playerList;
    }

    public void testNextPlayer() throws PlayerNotFoundException {
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertSame(round.getPlayer(0), round.nextPlayer());
        assertSame(round.getPlayer(1), round.nextPlayer());
        assertSame(round.getPlayer(1), round.nextPlayer());
        assertSame(round.getPlayer(0), round.nextPlayer());
        assertNull(round.nextPlayer());
    }


    public void testNextRound() throws PlayerNotFoundException {
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertSame(round.getPlayer(0), round.nextPlayer());
        assertSame(round.getPlayer(1), round.nextPlayer());
        assertSame(round.getPlayer(1), round.nextPlayer());
        assertSame(round.getPlayer(0), round.nextPlayer());
        assertNull(round.nextPlayer());

        round.nextRound();

        assertSame(round.getPlayer(1), round.nextPlayer());
        assertSame(round.getPlayer(0), round.nextPlayer());
        assertSame(round.getPlayer(0), round.nextPlayer());
        assertSame(round.getPlayer(1), round.nextPlayer());
        assertNull(round.nextPlayer());

        assertNotSame(round.toString(), round.getPlayer(0).toString());
    }

    public void testGetPlayer() throws PlayerNotFoundException{
        List<Player> list = myCellList();
        Round round = new Round(list);
        Player player = round.getPlayer(0);
        assertEquals(player, round.getPlayer(0));
    }

    public void testGetPlayerException(){
        List<Player> list = myCellList();
        Round round = new Round(list);

        assertThrows(PlayerNotFoundException.class, () -> round.getPlayer(10));
    }
}