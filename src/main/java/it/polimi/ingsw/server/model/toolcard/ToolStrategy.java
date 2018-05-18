package it.polimi.ingsw.server.model.toolcard;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.Iterator;
import java.util.List;

public class ToolStrategy {

    private RoundTrack roundTrack;
    private Draft draft;
    private DiceBag diceBag;

    public ToolStrategy(RoundTrack roundTrack, Draft draft, DiceBag diceBag) {
        this.diceBag = diceBag;
        this.roundTrack = roundTrack;
        this.draft = draft;
    }

    public boolean checkDiceWinCard(Dice d, WindowCard windowCard) throws IDNotFoundException {
        return windowCard.getWindow().containsDice(d);
    }

    public boolean checkDiceRoundTrack(Dice d) {
        try {
            roundTrack.findDice(d.getID());
        } catch (IDNotFoundException e) {
            return false;
        }

        return true;
    }

    public boolean checkDiceDraft(Dice d) {
        try {
            draft.findDice(d.getID());
        } catch (IDNotFoundException e) {
            return false;
        }

        return true;
    }

    public boolean checkTool12(List<Dice> dices, List<Cell> cells, Colors diceColor, WindowCard windowCard) throws IDNotFoundException {
        boolean bool = true;
        if (dices.isEmpty())
            return true;
        if (dices.size() == 2 && cells.size() == 2) {
            bool = checkDiceWinCard(dices.get(1), windowCard) && dices.get(1).getColor().equals(diceColor);
        }

        return dices.size()== cells.size() && dices.size()<3 && checkDiceWinCard(dices.get(0), windowCard) && bool &&
                roundTrack.findColor(diceColor) && dices.get(0).getColor().equals(diceColor) && !diceColor.equals(Colors.NULL);
    }

    public boolean changeValue(Dice d, boolean up) throws ValueException {
        if ((up && d.getValue() == 6) || (!up && d.getValue() == 1))
            return false;

        if (up)
            d.changeValue(d.getValue()+1);
        else
            d.changeValue(d.getValue()-1);

        return true;
    }

    public boolean moveOneDice(Dice d, Cell dest, String restrictionIgnored, WindowCard windowCard) throws IDNotFoundException, NotEmptyException {
        Cell c;
        boolean colorBool = restrictionIgnored.equals("color");
        boolean valueBool = restrictionIgnored.equals("value");

        c = windowCard.getWindow().getCell(d);
        c.freeCell();
        dest.setDice(d);
        try {
            if (colorBool)
                dest.setIgnoreColor();
            else if (valueBool)
                dest.setIgnoreValue();

            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else {
                windowCard.checkPlaceCond();
            }

            return true;
        } catch (WrongPositionException | PositionException | EmptyException e) {
            if (colorBool)
                dest.resetIgnoreColor();
            else if (valueBool)
                dest.resetIgnoreValue();

            c.setDice(d);
            dest.freeCell();

            return false;
        }
    }

    public boolean moveExTwoDice(List<Dice> dices, List<Cell> dest, WindowCard windowCard) throws IDNotFoundException, NotEmptyException {
        Cell c0 = windowCard.getWindow().getCell(dices.get(0));
        Cell c1 = windowCard.getWindow().getCell(dices.get(1));

        c0.freeCell();
        dest.get(0).setDice(dices.get(0));

        c1.freeCell();
        dest.get(1).setDice(dices.get(1));

        try {
            windowCard.checkPlaceCond();
            return true;
        } catch (WrongPositionException | PositionException e) {
            c1.setDice(dices.get(1));
            dest.get(1).freeCell();
            c0.setDice(dices.get(0));
            dest.get(0).freeCell();
            return false;
        }

    }

    public boolean moveUpToTwoDice(List<Dice> dices, List<Cell> dest, WindowCard windowCard) throws IDNotFoundException, NotEmptyException {
        if (dices.isEmpty())
            return true;

        boolean size2 = dices.size()==2;
        Cell from0 = windowCard.getWindow().getCell(dices.get(0));
        Cell from1 = null;

        if (size2) {
            from1 = windowCard.getWindow().getCell(dices.get(1));
            dest.get(1).setDice(dices.get(1).copyDice());
            from1.freeCell();
        }

        dest.get(0).setDice(dices.get(0).copyDice());
        from0.freeCell();

        try {
            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();

            return true;
        } catch (PositionException | EmptyException | WrongPositionException e) {
            if (size2) {
                from1.setDice(dices.get(1));
                dest.get(1).freeCell();
            }
            from0.setDice(dices.get(0));
            dest.get(0).freeCell();
            return false;
        }

    }

    public boolean moveFromDraftToRound(List<Dice> dices) throws IDNotFoundException, SameDiceException, EmptyException {
        int round = roundTrack.getRound(dices.get(1));
        draft.addDice(dices.get(1).copyDice());
        roundTrack.addDice(dices.get(0).copyDice(), round);
        draft.rmDice(dices.get(0));
        roundTrack.rmDice(dices.get(1), round);

        return true;
    }

    public boolean moveFromDraftToCard(Dice d, Cell dest, WindowCard windowCard) throws NotEmptyException, IDNotFoundException, SameDiceException {
        try {
            dest.setDice(d);
            findSetNearby(dest, true, windowCard);

            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();

            draft.rmDice(d);
            return true;
        } catch (WrongPositionException | PositionException | EmptyException e) {
            findSetNearby(dest, false, windowCard);
            dest.freeCell();
            draft.addDice(d);
            return false;
        }
    }

    public boolean moveFromDraftToBag(Dice dice, Cell dest, int diceValue, WindowCard windowCard) throws IDNotFoundException, EmptyException, ValueException, SameDiceException, NotEmptyException {
        Dice tmp = dice.copyDice();
        draft.rmDice(dice);
        diceBag.addDice(tmp);

        Dice tmp1 = diceBag.randDice();
        tmp1.changeValue(diceValue);
        dest.setDice(tmp1);

        try {
            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();

            tmp.changeValue(0);
            diceBag.rmDice(tmp1);
            return true;
        } catch (WrongPositionException | PositionException | EmptyException e) {
            dest.freeCell();
            draft.addDice(tmp);
            diceBag.rmDice(tmp);
            return false;
        }
    }

    public void findSetNearby(Cell dest, boolean set, WindowCard windowCard) {
        if (windowCard.numEmptyCells() == 18) {
            for (Iterator<Cell> itr = windowCard.getOrizzItr(); itr.hasNext();) {
                Cell cell = itr.next();
                if (cell.isOccupied())
                    if (set)
                        cell.setIgnoreNearby();
                    else
                        cell.resetIgnoreNearby();
            }
        }
        else if (set)
            dest.setIgnoreNearby();
        else
            dest.resetIgnoreNearby();
    }
}
