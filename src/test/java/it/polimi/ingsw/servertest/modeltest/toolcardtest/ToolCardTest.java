package it.polimi.ingsw.servertest.modeltest.toolcardtest;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolStrategy;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.toolcard.ToolCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToolCardTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(12)+1;
    private int idDice = random.nextInt(80)+1;
    private Colors col = Colors.random();
    private int fp = random.nextInt(4)+3;
    private DiceBag diceBag = new DiceBag();
    private Draft draft = new Draft(diceBag, 9);
    private RoundTrack roundTrack = new RoundTrack(draft);
    private Board board = new Board(4);
    private ToolStrategy strat = new ToolStrategy(roundTrack, draft, diceBag);

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
        ToolCard tool = new ToolCard(id, "Test", col, strat);

        assertSame(id, tool.getId());
        assertSame("Test", tool.getName());
        assertSame(col, tool.getColor());

        tool.setFavorPoint(fp);
        assertSame(fp, tool.getFavorPoint());
    }

    // testing obtaining elements (round Track, dice Bag, Draft) involved in the tool card
    public void testGetActor() {
        ToolCard tool = new ToolCard(id, "Test", col, strat);
        RoundTrack testTrack = null;
        Draft testDraft = null;
        DiceBag testBag = null;

        tool.setActor(null, draft, diceBag);
        List<Object> obj = tool.getActor();

        for (Object o : obj) {      // if it's not used, it's null at the end
            if (o instanceof RoundTrack)
                testTrack = (RoundTrack) o;
            else if (o instanceof Draft)
                testDraft = (Draft) o;
            else if (o instanceof DiceBag)
                testBag = (DiceBag) o;
        }

        assertNull(testTrack);
        assertSame(testDraft, draft);
        assertSame(testBag, diceBag);
    }

    // testing tool card 1
    public void testTool1() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, SameDiceException {
        ToolCard tool1 = new ToolCard(1, "Tool1", col, strat);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();

        tool1.setActor(null, draft, null);
        assertTrue(tool1.checkPreCondition(p, null));

        List<Object> obj = tool1.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, Colors.random());
                dices.add(d);
            }
        }

        assertFalse(tool1.checkTool(dices, null, 0, null));    // the dice is not in the draft

        Dice d = dices.get(0);
        draft.addDice(d);        // set it

        d.changeValue(3);      // verify positive with value = 3
        assertTrue(tool1.useTool(dices, true, null));
        assertSame(4, d.getValue());

        d.changeValue(3);
        assertTrue(tool1.useTool(dices, false, null));
        assertSame(2, d.getValue());

        d.changeValue(1);      // verify negative when reducing value = 1
        assertFalse(tool1.useTool(dices, false, null));
        assertSame(1, d.getValue());

        d.changeValue(6);      // verify positive when adding value = 6
        assertFalse(tool1.useTool(dices, true, null));
        assertSame(6, d.getValue());
    }

    // testing tool card 2 and 3
    public void testTool2_3() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, WrongPositionException, SameDiceException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myCellList());
        ToolCard tool2 = new ToolCard(2, "Tool2", col, strat);
        ToolCard tool3 = new ToolCard(3, "Tool3", col, strat);
        Player p = new Player(id);
        p.setWindowCard(winCard);
        p.setBoard(board);
        Dice d = null;
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        Cell c = winCard.getWindow().getCell(0,0);
        Cell cNear = winCard.getWindow().getCell(0,1);

        Colors colNear = cNear.getColor();
        int valNear = cNear.getValue();
        Colors col = c.getColor();
        int val = c.getValue();

        tool2.setActor(null, null, null);
        tool3.setActor(null, null, null);
        assertTrue(tool2.checkPreCondition(p, p.getWindowCard()));
        assertTrue(tool3.checkPreCondition(p, p.getWindowCard()));

        List<Object> obj = tool2.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                d = new Dice(idDice, colNear);
                dices.add(d);
            }
            else if (o instanceof Cell) {
                cells.add(cNear);
            }
        }

        assertFalse(tool2.checkTool(dices, cells, 0, null));
        assertFalse(tool3.checkTool(dices, cells, 0, null));

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

        cNear.setDice(new Dice(0, colNear));
        cNear.changeDiceValue(valNear);

        assertThrows(WrongPositionException.class, winCard::checkFirstDice);
        assertTrue(winCard.checkPlaceCond());
    }

    public void testTool4() throws IDNotFoundException, ValueException, NotEmptyException, PositionException, EmptyException, SameDiceException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList());
        ToolCard tool4 = new ToolCard(4, "Tool4", col, strat);
        Player p = new Player(id);
        p.setBoard(board);
        p.setWindowCard(winCard);
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int row = 0;
        int column = 0;

        tool4.setActor(null, null, null);
        assertTrue(tool4.checkPreCondition(p, p.getWindowCard()));

        List<Object> obj = tool4.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, Colors.random(), random.nextInt(6)+1);
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

        assertTrue(tool4.checkTool(dices, cells, 0, null));

        assertTrue(tool4.useTool(dices, null, cells));
        assertFalse(winCard.getWindow().getCell(0,0).isOccupied());
        assertFalse(winCard.getWindow().getCell(1,1).isOccupied());
        assertTrue(winCard.getWindow().getCell(2,2).isOccupied());
        assertTrue(winCard.getWindow().getCell(3,3).isOccupied());
    }

    public void testTool5() throws ValueException, PositionException, IDNotFoundException, SameDiceException, NotEmptyException, EmptyException {
        ToolCard tool5 = new ToolCard(5, "Tool5", col, strat);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        int round = random.nextInt(10);

        tool5.setActor(roundTrack, draft, null);
        assertTrue(tool5.checkPreCondition(p, p.getWindowCard()));

        List<Object> obj = tool5.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, Colors.random(), random.nextInt(6)+1);
                dices.add(d);
            }
        }

        draft.addDice(dices.get(0));
        roundTrack.addDice(dices.get(1), round);

        assertTrue(tool5.checkTool(dices, null, 0, null));

        assertTrue(tool5.useTool(dices, null, null));
        assertThrows(IDNotFoundException.class, () -> draft.rmDice(dices.get(0)));
        assertThrows(IDNotFoundException.class, () -> roundTrack.findDice(dices.get(1).getID()));
        assertSame(roundTrack.findDice(dices.get(0).getID()).getID(), idDice-2);
        assertSame(draft.findDice(dices.get(1).getID()).getID(), idDice-1);
    }

    public void testTool6_7() throws IDNotFoundException, ValueException, PositionException, SameDiceException, NotEmptyException, EmptyException {
        ToolCard tool6 = new ToolCard(6, "Tool6", col, strat);
        ToolCard tool7 = new ToolCard(7, "Tool7", col, strat);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();

        tool6.setActor(null, draft, null);
        tool7.setActor( null, draft, null);

        assertTrue(tool6.checkPreCondition(p, p.getWindowCard()));
        assertFalse(tool7.checkPreCondition(p, p.getWindowCard()));

        p.endFirstTurn();
        p.endSecondTurn();

        assertEquals(new ArrayList<>(), tool7.askParameter());

        List <Object> obj = tool6.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, Colors.random(), random.nextInt(6)+1);
                dices.add(d);
            }
        }

        draft.addDice(dices.get(0));

        assertTrue(tool6.checkTool(dices, null, 0, null));
        assertTrue(tool7.checkTool(null, null, 0, null));

        assertTrue(tool6.useTool(dices, null, null));
        assertTrue(tool7.useTool(null, null, null));

        p.resetFirstTurn();
        p.resetSecondTurn();
    }

    public void testTool8() throws IDNotFoundException, ValueException, PositionException, SameDiceException, NotEmptyException, EmptyException {
        ToolCard tool8 = new ToolCard(8, "Tool8", col, strat);
        Player p = new Player(id);
        p.setBoard(board);

        tool8.setActor(null, null, null);

        assertFalse(tool8.checkPreCondition(p, p.getWindowCard()));

        p.playDice();
        assertFalse(tool8.checkPreCondition(p, p.getWindowCard()));

        p.endFirstTurn();
        assertTrue(tool8.checkPreCondition(p, p.getWindowCard()));

        assertEquals(new ArrayList<>(), tool8.askParameter());

        assertTrue(tool8.checkTool(null, null, 0, null));

        assertTrue(tool8.useTool(null, null,null));

        p.resetFirstTurn();
        p.resetSecondTurn();
    }

    public void testTool9() throws ValueException, PositionException, IDNotFoundException, SameDiceException, NotEmptyException, EmptyException, WrongPositionException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList());
        ToolCard tool9 = new ToolCard(9, "Tool9", col, strat);
        Player p = new Player(id);
        p.setWindowCard(winCard);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        Cell dest = winCard.getWindow().getCell(0,0);
        int val = dest.getValue();
        Colors col = dest.getColor();

        tool9.setActor(null, draft, null);

        assertTrue(tool9.checkPreCondition(p, p.getWindowCard()));

        List <Object> obj = tool9.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, col, val);
                dices.add(d);
                draft.addDice(d);
            }
        }

        List<Cell> cells = new ArrayList<>();
        cells.add(dest);

        assertTrue(tool9.checkTool(dices, cells, 0, null));

        assertTrue(tool9.useTool(dices, null, cells));
        assertTrue(dest.isOccupied());
        assertTrue(winCard.checkFirstDice());
        assertNull(draft.findDice(dices.get(0).getID()));
        assertTrue(winCard.getWindow().containsDice(dices.get(0)));
    }

    public void testTool10() throws IDNotFoundException, SameDiceException, ValueException, PositionException, NotEmptyException, EmptyException {
        ToolCard tool10 = new ToolCard(10, "Tool10", col, strat);
        Player p = new Player(id);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();

        tool10.setActor(null, draft, null);

        assertTrue(tool10.checkPreCondition(p, p.getWindowCard()));

        List <Object> obj = tool10.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, Colors.random(), random.nextInt(6)+1);
                dices.add(d);
                draft.addDice(d);
            }
        }

        assertTrue(tool10.checkTool(dices, null, 0, null));

        int old = dices.get(0).getValue();

        assertTrue(tool10.useTool(dices, null, null));
        assertSame(7-old, draft.findDice(dices.get(0).getID()).getValue());
    }

    public void testTool11() throws IDNotFoundException, SameDiceException, ValueException, PositionException, NotEmptyException, EmptyException, WrongPositionException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList());
        ToolCard tool11 = new ToolCard(11, "Tool11", col, strat);
        Player p = new Player(id);
        p.setWindowCard(winCard);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        Cell dest = winCard.getWindow().getCell(0,0);
        int val = dest.getValue();
        Colors col = dest.getColor();

        tool11.setActor(null, draft, diceBag);

        assertTrue(tool11.checkPreCondition(p, p.getWindowCard()));

        List <Object> obj = tool11.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, col, val);
                diceBag.rmDice(d);
                dices.add(d);
                draft.addDice(d);
            }
            if (o instanceof Cell) {
                cells.add(dest);
            }
        }

        assertFalse(tool11.checkTool(dices, cells, 0, null));
        assertTrue(tool11.checkTool(dices, cells, 3, null));

        assertTrue(tool11.useTool(dices, null, cells));
        assertTrue(winCard.checkFirstDice());
        assertSame(winCard.getWindow().getCell(0,0).getDice().getValue(), 3);
    }

    public void testTool12() throws IDNotFoundException, SameDiceException, ValueException, PositionException, NotEmptyException, EmptyException, WrongPositionException {
        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList());
        ToolCard tool12 = new ToolCard(12, "Tool12", col, strat);
        Player p = new Player(id);
        p.setWindowCard(winCard);
        p.setBoard(board);
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int val = random.nextInt(6)+1;
        Colors color = Colors.GREEN;
        int row = 0;
        int col = 0;

        tool12.setActor(roundTrack, null, null);

        assertTrue(tool12.checkPreCondition(p, p.getWindowCard()));

        List <Object> obj = tool12.askParameter();
        for (Object o : obj) {
            if (o instanceof Dice) {
                Dice d = new Dice((idDice++)%90, color, val);
                dices.add(d);
            }
            if (o instanceof Cell) {
                cells.add(new Cell(0, Colors.NULL, row, col));
                row++;
                col++;
            }
        }

        assertFalse(tool12.checkTool(dices, cells, 0, color));

        winCard.getWindow().getCell(3,4).setDice(dices.get(0));
        winCard.getWindow().getCell(2,3).setDice(dices.get(1));
        roundTrack.addDice(new Dice(10, color), 1);

        assertTrue(tool12.checkTool(dices, cells, 0, color));

        assertTrue(tool12.useTool(dices, null, cells));
    }
}

