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

    //used to realize useTool()
    private ToolStrategy strat;
    private WindowCard windowCard;
    private RoundTrack roundTrack;
    private Draft draft;
    private DiceBag diceBag;
    private Player player;

    //attribute of the card
    private String name;
    private int id;
    private Colors color;
    private int favorPoint;
    private int diceValue;

    /**
     * Constructor
     * @param id > 0 && id < 12
     * @param name of the tool card
     * @param color of the dice for single player
     * @param toolStrategy used for the implementation of useTool()
     */
    public ToolCard(int id, String name, Colors color, ToolStrategy toolStrategy) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.favorPoint = 0;
        this.strat = toolStrategy;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Colors getColor() {
        return color;
    }       // single player dice color

    public int getFavorPoint() {
        return favorPoint;
    }

    public void setFavorPoint(int favorPoint) {
        this.favorPoint = favorPoint;
    }

    /**
     * Used by the Factory to sets objects involved in this tool card
     * @param roundTrack of the game
     * @param draft of the current Round
     * @param diceBag of the game
     */
    public void setActor(RoundTrack roundTrack, Draft draft, DiceBag diceBag) {
        this.draft = draft;
        this.diceBag = diceBag;
        this.roundTrack = roundTrack;
    }

    /**
     * Might return @Nullable Object
     * @return list of Object involved in this tool card.
     */
    public List<Object> getActor() {
        List<Object> ret = new ArrayList<>();

        ret.add(windowCard);
        ret.add(roundTrack);
        ret.add(diceBag);
        ret.add(draft);

        return ret;
    }

    /**
     * Checks if Tool card can be used
     * @param player != null && currentPlayer = player
     * @return true is tool is usable by Player in this turn, else false
     */
    public boolean checkPreCondition(Player player, WindowCard windowCard) {
        this.windowCard = windowCard;
        this.player = player;
        if (id == 7 && (player.isFirstTurn() || player.isSecondTurn()))
            return false;
        return id != 8 || (player.isPlayedDice() && (!player.isFirstTurn() && player.isSecondTurn()));
    }

    /**
     * Permits to get information on what is needed by the tool card
     * @return List<Object> needed to make the move specified by the tool Card
     */
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
        else if (id == 9) {
            list.add(new Dice(0, Colors.NULL));
        }
        else if (id == 10) {
            list.add(new Dice(0, Colors.NULL));
        }
        else if (id == 11) {
            list.add(new Dice(0, Colors.NULL));
            list.add(new Cell(0, Colors.NULL, 0, 0));
            list.add(0);
        }
        else if (id == 12) {
            list.add(new Dice(0, Colors.NULL));
            list.add(new Dice(0, Colors.NULL));
            list.add(new Cell(0, Colors.NULL, 0, 0));
            list.add(new Cell(0, Colors.NULL, 0, 0));
            list.add(Colors.NULL);
        }

        return list;
    }

    /**
     * Checks if correct elements have been selected for this tool
     * @param dices null when not needed
     * @param cells null when not needed
     * @param diceValue null when not needed
     * @param diceColor null when not needed
     * @return true if selected elements are correct for this tool, else false
     * @throws IDNotFoundException when non existent Dices are selected
     * @throws PositionException when cells have invalid positions
     */
    public boolean checkTool(List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) throws IDNotFoundException, PositionException {
        if (id == 1) {
            return dices.size()==1 && strat.checkDiceDraft(dices.get(0));
        }
        else if (id == 2 || id ==3) {
            return dices.size()==1 && cells.size()==1 && strat.checkDiceWinCard(dices.get(0), windowCard);
        }
        else if (id == 4) {
            return dices.size()==2 && cells.size()==2 && strat.checkDiceWinCard(dices.get(0), windowCard) && strat.checkDiceWinCard(dices.get(1), windowCard);
        }
        else if (id == 5) {
            return dices.size()==2 && strat.checkDiceDraft(dices.get(0)) && strat.checkDiceRoundTrack(dices.get(1));  // first dice is in draft, second in round track
        }
        else if (id == 6 || id==10) {
            return dices.size()==1 && strat.checkDiceDraft(dices.get(0));
        }
        else if (id == 9) {
            return dices.size()==1 && strat.checkDiceDraft(dices.get(0)) && cells.size()==1 && !windowCard.checkNeighbors(cells.get(0));
        }
        else if (id == 11) {
            if (diceValue < 1 || diceValue > 6)
                return false;
            else
                this.diceValue = diceValue;
            return dices.size()==1 && strat.checkDiceDraft(dices.get(0)) && cells.size()==1;
        }
        else if (id == 12) {
            return strat.checkTool12(dices, cells, diceColor, windowCard);
        }

        return true;
    }

    /**
     * Realize the effect of the tool card on the passed parameter
     * @param dices null when not needed
     * @param up null when not needed
     * @param cells null when not needed
     * @return true if move was sucessfull, else false
     * @throws ValueException when wrong value are chosen
     * @throws IDNotFoundException when couldn't find a dice
     * @throws NotEmptyException when trying to stack dice on the same cell
     * @throws EmptyException when trying to get dice from empty draft or bag
     * @throws SameDiceException when trying to put the same dice twice
     */
    public boolean useTool(List<Dice> dices, Boolean up, List<Cell> cells) throws ValueException, IDNotFoundException, NotEmptyException, EmptyException, SameDiceException {
        if (id == 1)
            return strat.changeValue(dices.get(0), up);
        else if (id == 2)
            return strat.moveOneDice(dices.get(0), cells.get(0), "color", windowCard);
        else if (id == 3)
            return strat.moveOneDice(dices.get(0), cells.get(0), "value", windowCard);
        else if (id == 4)
            return strat.moveExTwoDice(dices, cells, windowCard);
        else if (id == 5)
            return strat.moveFromDraftToRound(dices);
        else if (id == 6) {
            dices.get(0).rollDice();
            return true;
        }
        else if (id == 7) {
            draft.rollDraft();
            return true;
        }
        else if (id == 8) {
            player.resetPlayedDice();
            player.endSecondTurn();
        }
        else if (id == 9) {
            return strat.moveFromDraftToCard(dices.get(0), cells.get(0), windowCard);
        }
        else if (id==10) {
            dices.get(0).changeValue(7 - dices.get(0).getValue());
            return true;
        }
        else if (id == 11) {
            return strat.moveFromDraftToBag(dices.get(0), cells.get(0), diceValue, windowCard);
        }
        else if (id == 12) {
            return strat.moveUpToTwoDice(dices, cells, windowCard);
        }

        return true;
    }

}
