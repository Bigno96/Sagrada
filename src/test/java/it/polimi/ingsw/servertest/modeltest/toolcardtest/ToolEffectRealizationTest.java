package it.polimi.ingsw.servertest.modeltest.toolcardtest;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.toolcard.ToolEffectRealization;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToolEffectRealizationTest extends TestCase {

    private static final Random random = new Random();
    private int id = random.nextInt(12)+1;
    private Colors color = Colors.random();
    private int fp = random.nextInt(4)+3;
    private int val = random.nextInt(6)+1;

    private final GameSettingsParser gameSettings = (GameSettingsParser) ParserManager.getGameSettingsParser();

    public ToolEffectRealizationTest(String testName) {
        super(testName);
    }

    // filling a list with 20 random cells with no restriction
    private List<Cell> myEmptyCellList() throws ValueException, PositionException {

        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<4; i++)
            for (int j=0; j<5; j++)
                cellList.add(new Cell(0, Colors.WHITE, i, j, gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn()));
        return cellList;
    }


    public void testCheckTool12() throws IDNotFoundException, ValueException, PositionException, NotEmptyException, SameDiceException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        Cell c0 = winCard.getWindow().getCell(0,0);
        Cell c1 = winCard.getWindow().getCell(1,1);

        board.getRoundTrack().addDice(new Dice(id, Colors.GREEN), 1);

        assertTrue(strategy.checkTool12(dices, cells, null, null));

        Dice d0 = new Dice(id, Colors.GREEN);
        dices.add(d0);
        c0.setDice(d0);
        cells.add(c0);

        assertTrue(strategy.checkTool12(dices, cells, Colors.GREEN, winCard));

        Dice d1 = new Dice((id+1)%90, Colors.GREEN);
        dices.add(d1);
        c1.setDice(d1);
        cells.add(c1);

        assertTrue(strategy.checkTool12(dices, cells, Colors.GREEN, winCard));
    }

    public void testCheckDiceIn() throws ValueException, PositionException, IDNotFoundException, NotEmptyException, SameDiceException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        Cell c0 = winCard.getWindow().getCell(0,0);

        Dice d0 = new Dice(id, color);
        c0.setDice(d0);

        assertTrue(strategy.checkDiceWinCard(d0, winCard));

        board.getRoundTrack().addDice(d0, 1);

        assertTrue(strategy.checkDiceRoundTrack(d0));

        board.getDraft().addDice(d0);

        assertTrue(strategy.checkDiceDraft(d0));
    }

    public void testChangeValue() throws IDNotFoundException, ValueException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        Dice d = new Dice(id, color);

        d.changeValue(1);
        assertFalse(strategy.changeValue(d, false));

        d.changeValue(6);
        assertFalse(strategy.changeValue(d, true));

        d.changeValue(3);
        assertTrue(strategy.changeValue(d, false));
        assertSame(2, d.getValue());

        assertTrue(strategy.changeValue(d, true));
        assertSame(3, d.getValue());
    }

    public void testMoveOneDice() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        int row = 0;
        int col = 0;

        Dice d0 = new Dice(id, Colors.GREEN, val);
        Dice d1 = new Dice((id+1)%90, Colors.GREEN, (val+1)%6);
        winCard.getWindow().getCell(row, col).setDice(d0);
        winCard.getWindow().getCell(2,2).setDice(d1);
        Cell dest = winCard.getWindow().getCell(row, col+1);

        assertFalse(strategy.moveOneDice(d1, dest, "value", winCard));

        assertTrue(strategy.moveOneDice(d1, dest, "color", winCard));

        dest.freeCell();
        d1 = new Dice((id+1)%90, Colors.BLUE, val);
        winCard.getWindow().getCell(2,2).setDice(d1);
        assertFalse(strategy.moveOneDice(d1, dest, "color", winCard));
        assertTrue(strategy.moveOneDice(d1, dest, "value", winCard));

        dest = winCard.getWindow().getCell(row+1, col+1);
        assertTrue(strategy.moveOneDice(d1, dest, "value", winCard));
        assertTrue(strategy.moveOneDice(d1, dest, "color", winCard));
    }

    public void testMoveExTwoDice() throws IDNotFoundException, NotEmptyException, ValueException, PositionException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int row = 0;
        int col = 0;

        Dice d0 = new Dice(id, Colors.GREEN, val);
        Dice d1 = new Dice((id+1)%90, Colors.GREEN, val);
        Cell c0 = winCard.getWindow().getCell(row, col);
        Cell c1 = winCard.getWindow().getCell(row, col+1);

        dices.add(d0);
        dices.add(d1);
        cells.add(c0);
        cells.add(c1);

        winCard.getWindow().getCell(3,3).setDice(d0);
        winCard.getWindow().getCell(2,2).setDice(d1);

        assertFalse(strategy.moveExTwoDice(dices, cells, winCard));

        d0.changeValue((val+1)%6+1);
        assertFalse(strategy.moveExTwoDice(dices, cells, winCard));

        dices.remove(d1);
        winCard.getWindow().getCell(2,2).freeCell();
        d1 = new Dice((id+1)%90, Colors.BLUE, val);
        dices.add(d1);
        winCard.getWindow().getCell(2,2).setDice(d1);
        assertTrue(strategy.moveExTwoDice(dices, cells, winCard));
    }

    public void testMoveUpToTwoDice() throws ValueException, PositionException, IDNotFoundException, NotEmptyException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        List<Dice> dices = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        int row = 0;
        int col = 0;

        assertTrue(strategy.moveUpToTwoDice(dices, cells, winCard));

        Dice d0 = new Dice(id, Colors.GREEN, val);
        Cell c0 = winCard.getWindow().getCell(row, col);

        dices.add(d0);
        cells.add(c0);
        winCard.getWindow().getCell(3,3).setDice(d0);

        assertTrue(strategy.moveUpToTwoDice(dices, cells, winCard));

        Dice d1 = new Dice((id+1)%90, Colors.BLUE, val);
        Cell c1 = winCard.getWindow().getCell(row, col+1);

        cells.get(0).freeCell();
        winCard.getWindow().getCell(3,3).setDice(d0);
        dices.add(d1);
        cells.add(c1);
        winCard.getWindow().getCell(2,2).setDice(d1);

        assertFalse(strategy.moveUpToTwoDice(dices, cells, winCard));

        d1.changeValue((val+1)%6+1);
        assertTrue(strategy.moveUpToTwoDice(dices, cells, winCard));
    }

    public void testMoveFromDraftToRound() throws IDNotFoundException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        Dice d0 = new Dice(id, Colors.random(), val);
        Dice d1 = new Dice((id+1)%90, Colors.random(), val);
        List<Dice> dices = new ArrayList<>();

        dices.add(d0);
        dices.add(d1);

        assertThrows(IDNotFoundException.class, () -> strategy.moveFromDraftToRound(dices));
    }

    public void testMoveFromDraftToBagThanPlace() throws IDNotFoundException, NotEmptyException, ValueException, PositionException, EmptyException, SameDiceException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        Dice d0 = new Dice(id, Colors.BLUE, val);
        Dice d1 = new Dice((id+1)%90, Colors.GREEN, val);
        Cell c0 = winCard.getWindow().getCell(0, 0);
        Cell c1 = winCard.getWindow().getCell(0, 1);

        c0.setDice(d0);

        board.getDiceBag().rmDice(d1);
        board.getDraft().addDice(d1);

        assertFalse(strategy.moveFromDraftToBagThanPlace(d1, c1, val, winCard));
        if(strategy.moveFromDraftToBagThanPlace(d1, c1, (val+1)%6+1, winCard)) {
            assertTrue(c1.isOccupied() && !c1.getDice().getColor().equals(Colors.BLUE));
        } else
            assertFalse(c1.isOccupied());
    }

    public void testMoveFromDraftToCard() throws IDNotFoundException, NotEmptyException, ValueException, PositionException, SameDiceException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        Dice d0 = new Dice((id+2)%90, Colors.BLUE, val);
        Dice d1 = new Dice((id+3)%90, Colors.GREEN, val);
        Cell c0 = winCard.getWindow().getCell(0, 0);
        Cell c1 = winCard.getWindow().getCell(0, 1);

        c0.setDice(d0);

        assertFalse(strategy.moveFromDraftToCard(d1, c1, winCard));

        d1.changeValue((val+1)%6+1);

        assertTrue(strategy.moveFromDraftToCard(d1, c1, winCard));
    }

    public void testFindSetNearby() throws IDNotFoundException, NotEmptyException, ValueException, PositionException {
        Board board = new Board(4);
        ToolEffectRealization strategy =
                new ToolEffectRealization(board.getRoundTrack(), board.getDraft(), board.getDiceBag());

        WindowCard winCard = new WindowCard(id, "Test", fp, myEmptyCellList(),
                gameSettings.getWindowCardMaxRow(), gameSettings.getWindowCardMaxColumn());
        Dice d0 = new Dice(id, Colors.GREEN, val);
        Dice d1 = new Dice((id+4)%90, Colors.GREEN, val);
        Dice d2 = new Dice((id+5)%90, Colors.GREEN, val);
        Cell c0 = winCard.getWindow().getCell(0, 0);
        Cell c1 = winCard.getWindow().getCell(0, 1);
        Cell c2 = winCard.getWindow().getCell(1, 1);

        c0.setDice(d0);
        c1.setDice(d1);
        strategy.findSetNearby(c0, true, winCard);

        assertTrue(c0.isIgnoreNearby());
        assertTrue(c1.isIgnoreNearby());
        assertFalse(c2.isIgnoreNearby());

        strategy.findSetNearby(c1, false, winCard);
        assertFalse(c0.isIgnoreNearby());
        assertFalse(c1.isIgnoreNearby());
        assertFalse(c2.isIgnoreNearby());

        c2.setDice(d2);
        strategy.findSetNearby(c2, true, winCard);
        assertFalse(c0.isIgnoreNearby());
        assertFalse(c1.isIgnoreNearby());
        assertTrue(c2.isIgnoreNearby());

        strategy.findSetNearby(c2, false, winCard);
        assertFalse(c0.isIgnoreNearby());
        assertFalse(c1.isIgnoreNearby());
        assertFalse(c2.isIgnoreNearby());
    }
}
