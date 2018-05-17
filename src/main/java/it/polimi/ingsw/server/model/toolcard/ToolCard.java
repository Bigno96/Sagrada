package it.polimi.ingsw.server.model.toolcard;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.ArrayList;
import java.util.List;

public class ToolCard {

    private String name;
    private int id;
    private Colors diceColor;
    private int favorPoint;
    private WindowCard windowCard;
    private RoundTrack roundTrack;
    private Draft draft;
    private DiceBag diceBag;

    public ToolCard(int id, String name, Colors diceColor) {
        this.name = name;
        this.id = id;
        this.diceColor = diceColor;
        this.favorPoint = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Colors getDiceColor() {
        return diceColor;
    }       // single player dice color

    public int getFavorPoint() {
        return favorPoint;
    }

    public void setFavorPoint(int favorPoint) {
        this.favorPoint = favorPoint;
    }

    public void setActor(WindowCard windowCard, RoundTrack roundTrack, Draft draft, DiceBag diceBag) {
        this.windowCard = windowCard;
        this.draft = draft;
        this.diceBag = diceBag;
        this.roundTrack = roundTrack;
    }

    public List<Object> getParameter() {
        List<Object> ret = new ArrayList<>();

        if (windowCard != null)
            ret.add(windowCard);
        if (roundTrack != null)
            ret.add(roundTrack);
        if (diceBag != null)
            ret.add(diceBag);
        if (draft != null)
            ret.add(draft);

        return ret;
    }

    public boolean checkPreCondition(Player player) {
        if (id == 7 && (player.isFirstTurn() || player.isSecondTurn()))
            return false;
        return id != 8 || (!player.isFirstTurn() && player.isSecondTurn());
    }

    public List<Object> askParameter() throws IDNotFoundException, ValueException, PositionException {
        List<Object> list = new ArrayList<>();

        if (id == 1) {
            list.add(new Dice(0, Colors.NULL));
        }
        else if (id == 2 || id == 3) {
            list.add(new Dice(0, Colors.NULL));
            list.add(new Cell(0, Colors.NULL, 0, 0));
        }
        else if (id == 4) {
            list.add(new Dice(0, Colors.NULL));
            list.add(new Dice(0, Colors.NULL));
            list.add(new Cell(0, Colors.NULL, 0, 0));
            list.add(new Cell(0, Colors.NULL, 0, 1));
        }
        else if (id == 5) {
            list.add(new Dice(0, Colors.NULL));
            list.add(new Dice(0, Colors.NULL));
        }
        else if (id == 6) {
            list.add(new Dice(0, Colors.NULL));
        }

        return list;
    }

    public boolean checkTool(List<Dice> dices, List<Cell> cells) throws IDNotFoundException {
        if (id == 1) {
            return dices.size()==1 && checkDiceWinCard(dices.get(0));
        }
        else if (id == 2 || id ==3) {
            return dices.size()==1 && cells.size()==1 && checkDiceWinCard(dices.get(0));
        }
        else if (id == 4) {
            return dices.size()==2 && cells.size()==2 && checkDiceWinCard(dices.get(0)) && checkDiceWinCard(dices.get(1));
        }
        else if (id == 5) {
            return dices.size()==2 && checkDiceDraft(dices.get(0)) && checkDiceRoundTrack(dices.get(1));  // first dice is in draft, second in round track
        }
        else if (id == 6) {

        }

        return true;
    }

    private boolean checkDiceWinCard(Dice d) throws IDNotFoundException {
        return windowCard.getWindow().containsDice(d);
    }

    private boolean checkDiceRoundTrack(Dice d) {
        try {
            roundTrack.findDice(d.getID());
        } catch (IDNotFoundException e) {
            return false;
        }

        return true;
    }

    private boolean checkDiceDraft(Dice d) {
        try {
            draft.findDice(d.getID());
        } catch (IDNotFoundException e) {
            return false;
        }

        return true;
    }

    public boolean useTool(List<Dice> dices, Boolean up, List<Cell> cells) throws ValueException, IDNotFoundException, NotEmptyException, EmptyException, SameDiceException {
        if (id == 1)
            return changeValue(dices.get(0), up);
        else if (id == 2)
            return moveOneDice(dices.get(0), cells.get(0), "color");
        else if (id == 3)
            return moveOneDice(dices.get(0), cells.get(0), "value");
        else if (id == 4)
            return moveExTwoDice(dices, cells);
        else if (id == 5)
            return moveDraftToRound(dices, null);
        return true;
    }

    private boolean changeValue(Dice d, boolean up) throws ValueException {
        if ((up && d.getValue() == 6) || (!up && d.getValue() == 1))
            return false;

        if (up)
            d.changeValue(d.getValue()+1);
        else
            d.changeValue(d.getValue()-1);

        return true;
    }

    private boolean moveOneDice(Dice d, Cell dest, String restrictionIgnored) throws IDNotFoundException, NotEmptyException {
        Cell c;

        c = windowCard.getWindow().getCell(d);
        c.freeCell();
        dest.setDice(d);
        try {
            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();
            if (restrictionIgnored.equals("color"))
                dest.setIgnoreColor();
            else // restrictionIgnored is "value"
                dest.setIgnoreValue();
        } catch (WrongPositionException | PositionException | EmptyException e) {
            c.setDice(d);
            dest.freeCell();
            return false;
        }

        return true;
    }

    private boolean moveExTwoDice(List<Dice> dices, List<Cell> dest) throws IDNotFoundException, NotEmptyException {
        Cell c;

        c = windowCard.getWindow().getCell(dices.get(0));
        c.freeCell();
        dest.get(0).setDice(dices.get(0));
        try {
            windowCard.checkPlaceCond();
        } catch (WrongPositionException | PositionException e) {
            c.setDice(dices.get(0));
            dest.get(0).freeCell();
            return false;
        }

        c =  windowCard.getWindow().getCell(dices.get(1));
        c.freeCell();
        dest.get(1).setDice(dices.get(1));
        try {
            windowCard.checkPlaceCond();
        } catch (WrongPositionException | PositionException e) {
            c.setDice(dices.get(0));
            dest.get(1).freeCell();
            return false;
        }

        return true;
    }

    private boolean moveDraftToRound(List<Dice> dices, List<Cell> cells) throws IDNotFoundException, SameDiceException, EmptyException {
        if (id == 5) {
            int round = roundTrack.getRound(dices.get(1));
            draft.addDice(dices.get(1).copyDice());
            roundTrack.addDice(dices.get(0).copyDice(), round);
            draft.rmDice(dices.get(0));
            roundTrack.rmDice(dices.get(1), round);
        }

        return true;
    }
}
