
<<<<<<< HEAD
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.toolcard.ToolCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToolCardTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(12)+1;
    private int idDice = random.nextInt(90);
    private Colors col = Colors.random();
    private int fp = random.nextInt(4)+3;
    private DiceBag diceBag = new DiceBag();
    private Draft draft = new Draft(diceBag, 9);
    private RoundTrack roundTrack = new RoundTrack(draft);
    private Board board = new Board(4);

    public ToolCardTest(String testName) throws IDNotFoundException {
        super(testName);
    }

    // filling a list with 20 random cells
    private List<Cell> myCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                cellList.add(new Cell(random.nextInt(7), Colors.random(), i, j));
        return cellList;
    }

    // filling a list with 20 random cells with no restriction
    private List<Cell> myEmptyCellList() throws ValueException, PositionException {
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                cellList.add(new Cell(0, Colors.NULL, i, j));
        return cellList;
    }

    // testing attribute
    public void testAttribute() {
        ToolCard tool = new ToolCard(id, "Test", col);

        assertSame(id, tool.getId());
        assertSame("Test", tool.getName());
        assertSame(col, tool.getDiceColor());

        tool.setFavorPoint(fp);
        assertSame(fp, tool.getFavorPoint());
    }

    // testing tool card 1
    public void testTool1() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, SameDiceException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myCellList());
        ToolCard tool = new ToolCard(1, "Tool1", col);
        Player p = new Player(id);
        p.setBoard(board);
        Colors colDice;
        Dice d = null;
        List<Dice> dices = new ArrayList<>();
        Cell c = winCard.getWindow().getCell(0,0);

        colDice = c.getColor();

        tool.setActor(winCard, null, null, null);
        assertTrue(tool.checkPreCondition(p));

        List<Object> obj = tool.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                d = new Dice(idDice, colDice);
                dices.add(d);
            }
        }

        assertFalse(tool.checkTool(dices, null));                 // the dice is not in the window card

        c.setDice(d);        // set it

        c.changeDiceValue(3);      // verify positive with value = 3
        assertTrue(tool.useTool(dices, true, null));
        assertSame(4, c.getDice().getValue());

        c.changeDiceValue(3);
        assertTrue(tool.useTool(dices, false, null));
        assertSame(2, c.getDice().getValue());

        c.changeDiceValue(1);      // verify negative when reducing value = 1
        assertFalse(tool.useTool(dices, false, null));
        assertSame(1, c.getDice().getValue());

        c.changeDiceValue(6);      // verify positive when adding value = 6
        assertFalse(tool.useTool(dices, true, null));
        assertSame(6, c.getDice().getValue());
    }

    // testing tool card 2 and 3
    public void testTool2_3() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, WrongPositionException, SameDiceException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myCellList());
        ToolCard tool2 = new ToolCard(2, "Tool2", col);
        ToolCard tool3 = new ToolCard(3, "Tool3", col);
        Player p = new Player(id);
        p.setBoard(board);
        Dice d = null;
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        Cell c = winCard.getWindow().getCell(0,0);
        Cell cNear = winCard.getWindow().getCell(0,1);

        Colors col = cNear.getColor();
        int val = cNear.getValue();

        tool2.setActor(winCard, null, null, null);
        tool3.setActor(winCard, null, null, null);
        assertTrue(tool2.checkPreCondition(p));
        assertTrue(tool3.checkPreCondition(p));

        List<Object> obj = tool2.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                d = new Dice(idDice, col);
                dices.add(d);
            }
            else if (o instanceof Cell) {
                cells.add(cNear);
            }
        }

        assertFalse(tool2.checkTool(dices, cells));
        assertFalse(tool3.checkTool(dices, cells));

        c.setIgnoreValue();
        cNear.setIgnoreValue();
        c.setDice(d);

        assertTrue(tool2.useTool(dices, null, cells));
        assertThrows(NotEmptyException.class, () -> cNear.setDice(new Dice(0, col)));

        c.setDice(new Dice(0, col));

        assertThrows(WrongPositionException.class, winCard::checkFirstDice);
        assertTrue(winCard.checkPlaceCond());

        c.freeCell();
        c.resetIgnoreValue();
        cNear.resetIgnoreValue();
        c.setIgnoreColor();
        cNear.setIgnoreColor();
        if (d!=null) d.changeValue(val);
        cells.remove(cNear);
        cells.add(c);

        assertTrue(tool3.useTool(dices, null, cells));
        assertTrue(winCard.checkFirstDice());
        assertThrows(NotEmptyException.class, () -> c.setDice(new Dice(0, col)));

        cNear.setDice(new Dice(0, col));
        cNear.changeDiceValue(val);

        assertThrows(WrongPositionException.class, winCard::checkFirstDice);
        assertTrue(winCard.checkPlaceCond());
    }

    public void testTool4() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, SameDiceException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList());
        ToolCard tool4 = new ToolCard(4, "Tool4", col);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int row = 0;
        int column = 0;

        tool4.setActor(winCard, null, null, null);
        assertTrue(tool4.checkPreCondition(p));

        List<Object> obj = tool4.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice(idDice, Colors.random(), random.nextInt(6)+1);
                winCard.getWindow().getCell(row, column).setDice(d);
                dices.add(d);
                row++;
                column++;
            } else if (o instanceof Cell) {
                cells.add(winCard.getWindow().getCell(row, column));
                row++;
                column++;
            }
        }

        assertTrue(tool4.checkTool(dices, cells));

        assertTrue(tool4.useTool(dices, null, cells));
        assertFalse(winCard.getWindow().getCell(0,0).isOccupied());
        assertFalse(winCard.getWindow().getCell(1,1).isOccupied());
        assertTrue(winCard.getWindow().getCell(2,2).isOccupied());
        assertTrue(winCard.getWindow().getCell(3,3).isOccupied());
    }

    public void testTool5() throws ValueException, PositionException, IDNotFoundException, SameDiceException, NotEmptyException, EmptyException {
        ToolCard tool5 = new ToolCard(4, "Tool5", col);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        int round = random.nextInt(10);

        tool5.setActor(null, roundTrack, draft, null);
        assertTrue(tool5.checkPreCondition(p));

        List<Object> obj = tool5.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice(idDice, Colors.random(), random.nextInt(6)+1);
                dices.add(d);
            }
        }

        draft.addDice(dices.get(0));
        roundTrack.addDice(dices.get(1), round);

        assertTrue(tool5.checkTool(dices, null));

        assertTrue(tool5.useTool(dices, null, null));
        assertThrows(IDNotFoundException.class, () -> draft.rmDice(dices.get(0)));
        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(dices.get(1).getID()));
        assertSame(draft.findDice(dices.get(1).getID()).getID(), idDice);
        assertSame(roundTrack.findDice(dices.get(0).getID()).getID(), idDice);
    }
}
=======
>>>>>>> 6cdd43f771e0ac2e34d58a277a39829d297ada56
