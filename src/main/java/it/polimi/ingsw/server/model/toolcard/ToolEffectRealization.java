package it.polimi.ingsw.server.model.toolcard;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;
import java.util.List;

public class ToolEffectRealization implements Serializable {

    private RoundTrack roundTrack;
    private Draft draft;
    private DiceBag diceBag;

    /**
     * Constructor
     * @param roundTrack != null
     * @param draft != null
     * @param diceBag != null
     */
    public ToolEffectRealization (RoundTrack roundTrack, Draft draft, DiceBag diceBag) {
        this.diceBag = diceBag;
        this.roundTrack = roundTrack;
        this.draft = draft;
    }

    /**
     * Check if the Dice is in the Window Card
     * @param d != null
     * @param windowCard != null
     * @return true if window Card contains Dice, else false
     */
    public boolean checkDiceWinCard(Dice d, WindowCard windowCard) {
        return windowCard.getWindow().containsDice(d);
    }

    /**
     * Check if the Dice is in the Round Track
     * @param d != null
     * @return true if Round Track contains Dice, else false
     */
    public boolean checkDiceRoundTrack(Dice d) {
        try {
            return (roundTrack.findDice(d.getID()) != null);
        } catch (IDNotFoundException e) {
            return false;
        }
    }

    /**
     * Check if the Dice is in the Draft
     * @param d != null
     * @return true if Draft contains Dice, else false
     */
    public boolean checkDiceDraft(Dice d) {
        return draft.findDice(d.getID()) != null;
    }

    /**
     * Check if the condition for the tool number 12 are rights. Dices must be the same color of diceColor and
     * windowCard must contains the dices.
     * @param dices != null && dices.size() < 3
     * @param cells != null && dices.size() == cells.size()
     * @param diceColor != null
     * @param windowCard != null
     * @return true if conditions are respected, else false
     */
    public boolean checkTool12(List<Dice> dices, List<Cell> cells, Colors diceColor, WindowCard windowCard) {
        boolean bool = true;

        if (dices.isEmpty())
            return true;
        if (dices.size() == 2 && cells.size() == 2)
            bool = checkDiceWinCard(dices.get(1), windowCard) && dices.get(1).getColor().equals(diceColor);

        return dices.size() == cells.size() && dices.size()<3 && checkDiceWinCard(dices.get(0), windowCard) && bool &&
            roundTrack.findColor(diceColor) && dices.get(0).getColor().equals(diceColor) && !diceColor.equals(Colors.WHITE);
    }

    /**
     * Change value of a dice into /old+1 or /old-1. Can't change 6 into 1, 1 into 6
     * @param d != null
     * @param up != null
     * @return true if can change the value, else false
     * @throws ValueException when invalid value
     */
    public boolean changeValue(Dice d, boolean up) throws ValueException {
        if ((up && d.getValue() == 6) || (!up && d.getValue() == 1))
            return false;

        if (up)
            d.changeValue(d.getValue()+1);
        else
            d.changeValue(d.getValue()-1);

        draft.setChangedAndNotify();

        return true;
    }

    /**
     * Move a dice from a cell in window Card to a Cell (dest) passed as parameter. Ignore restriction based on the String parameter
     * @param d != null && windowCard.contains(d)
     * @param dest != null
     * @param restrictionIgnored == "color" || "value"
     * @param windowCard != null
     * @return true if can move the Dice
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public boolean moveOneDice(Dice d, Cell dest, String restrictionIgnored, WindowCard windowCard) throws NotEmptyException {
        Cell from;
        boolean colorBool = restrictionIgnored.equals("color");
        boolean valueBool = restrictionIgnored.equals("value");

        from = windowCard.getWindow().getCell(d);
        from.freeCell();
        dest.setDice(d);

        try {
            if (colorBool)
                dest.setIgnoreColor(true);
            else if (valueBool)
                dest.setIgnoreValue(true);

            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();

            windowCard.setPlacement(dest);

            return true;

        } catch (WrongPositionException | PositionException | EmptyException e) {
            if (colorBool)
                dest.setIgnoreColor(false);
            else if (valueBool)
                dest.setIgnoreValue(false);

            from.setDice(d);
            dest.freeCell();

            return false;
        }
    }

    /**
     * Moving exactly two dice inside window Card. Respecting all restrictions.
     * @param dices != null && dices.size() == 2
     * @param dest != null && dest.size() == dices.size()
     * @param windowCard != null
     * @return true if can move the dices
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public boolean moveExTwoDice(List<Dice> dices, List<Cell> dest, WindowCard windowCard) throws NotEmptyException {
        Cell from0 = windowCard.getWindow().getCell(dices.get(0));
        Cell from1 = windowCard.getWindow().getCell(dices.get(1));

        Cell dest0 = windowCard.getWindow().getCell(dest.get(0).getRow(), dest.get(0).getCol());
        Cell dest1 = windowCard.getWindow().getCell(dest.get(1).getRow(), dest.get(1).getCol());

        from0.freeCell();
        dest0.setDice(dices.get(0));

        from1.freeCell();
        dest1.setDice(dices.get(1));

        try {
            windowCard.checkPlaceCond();

            windowCard.setPlacement(dest0);
            windowCard.setPlacement(dest1);

            return true;

        } catch (WrongPositionException | PositionException e) {
            from1.setDice(dices.get(1));
            dest1.freeCell();
            from0.setDice(dices.get(0));
            dest0.freeCell();

            return false;
        }
    }

    /**
     * Moving 0, 1 or 2 dices inside window Card.
     * @param dices has the same colors
     * @param dest dest.size() == dices.size()
     * @param windowCard != null
     * @return true if can move the dices
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public boolean moveUpToTwoDice(List<Dice> dices, List<Cell> dest, WindowCard windowCard) throws NotEmptyException {
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

            windowCard.setPlacement(dest.get(0));

            if (size2)
                windowCard.setPlacement(dest.get(1));

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

    /**
     * Swapping a dice from round Track with one from Draft
     * @param dices dices.size() == 2 && roundTrack.contains(dices.get(0)) && draft.contains(dices.get(1))
     * @return true if the swap is successful
     * @throws IDNotFoundException when dices have wrong id
     * @throws SameDiceException when trying to add the same dice twice to the same object
     * @throws EmptyException when trying to get a dice from an empty draft
     * @throws RoundNotFoundException when wrong round is requested
     */
    public boolean moveFromDraftToRound(List<Dice> dices) throws IDNotFoundException, SameDiceException, EmptyException, RoundNotFoundException {
        int round = roundTrack.getRound(dices.get(1));

        draft.addDice(dices.get(1).copyDice());
        roundTrack.addDice(dices.get(0).copyDice(), round);

        draft.rmDice(dices.get(0));
        roundTrack.rmDice(dices.get(1), round);

        roundTrack.setChangedAndNotify();
        draft.setChangedAndNotify();

        return true;
    }

    /**
     * Place a Dice from draft into window Card, no Dice must be around the destination cell
     * @param d != null && draft.contains(d)
     * @param dest dest.checkNeighbors == false;
     * @param windowCard != null
     * @return true if the move has success, else false
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     * @throws IDNotFoundException when dices have wrong id
     * @throws SameDiceException when trying to add the same dice twice to the same object
     */
    public boolean moveFromDraftToCard(Dice d, Cell dest, WindowCard windowCard) throws NotEmptyException, IDNotFoundException, SameDiceException {
        try {
            dest.setDice(d);
            findSetNearby(dest, true, windowCard);

            if (windowCard.numEmptyCells() == 19)
                windowCard.checkFirstDice();
            else
                windowCard.checkPlaceCond();

            draft.rmDice(d);

            draft.setChangedAndNotify();
            windowCard.setPlacement(dest);

            return true;

        } catch (WrongPositionException | PositionException | EmptyException e) {
            findSetNearby(dest, false, windowCard);
            dest.freeCell();
            draft.addDice(d);

            return false;
        }
    }

    /**
     * Permits to move one dice from draft to the bag, resetting its value, the pick up a random dice from Bag, choose
     * the value and place it, respecting all the restrictions
     * @param dice != null && draft.contains(d)
     * @param dest != null && dest.isFree
     * @param diceValue > 0 && < 7
     * @param windowCard != null
     * @return true if the operation is successful
     * @throws IDNotFoundException when dices have wrong id
     * @throws EmptyException when trying to get a dice from an empty draft or bag
     * @throws ValueException when an incorrect value is being forced to a dice
     * @throws SameDiceException when trying to add the same dice twice to the same object
     * @throws NotEmptyException when trying to set a dice on a cell already occupied
     */
    public boolean moveFromDraftToBagThanPlace(Dice dice, Cell dest, int diceValue, WindowCard windowCard) throws IDNotFoundException, EmptyException, ValueException, SameDiceException, NotEmptyException {
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

            draft.setChangedAndNotify();
            windowCard.setPlacement(dest);

            return true;

        } catch (WrongPositionException | PositionException | EmptyException e) {
            dest.freeCell();
            draft.addDice(tmp);
            diceBag.rmDice(tmp);

            return false;
        }
    }

    /**
     * If two dices are on window Card, ignore or set nearby restriction of both of their Cells. Else, ignore or set
     * nearby restriction only for dest.
     * @param dest != null && dest.isFree
     * @param set true if ignore Nearby Restriction, false if set Nearby Restriction
     * @param windowCard != null
     */
    public void findSetNearby(Cell dest, boolean set, WindowCard windowCard) {
        if (windowCard.numEmptyCells() == 18) {
            windowCard.getHorizontalItr().forEachRemaining(cell -> {
                if (cell.isOccupied())
                    if (set)
                        cell.setIgnoreNearby(true);
                    else
                        cell.setIgnoreNearby(false);
            });
        }
        else if (set)
            dest.setIgnoreNearby(true);
        else
            dest.setIgnoreNearby(false);
    }
}
