package it.polimi.ingsw.server.model.toolcard;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class ToolCard extends Observable implements Serializable {

    public enum Actor { WINDOW_CARD, ROUND_TRACK, DRAFT, DICE_BAG }
    public enum Parameter { DICE, CELL, INTEGER, COLOR, BOOLEAN }

    private static final String DUMP_ID_MSG = "ID = ";
    private static final String DUMP_NAME_MSG = " name = ";
    private static final String DUMP_COLOR_MSG = " color = ";
    private static final String DUMP_FAVOR_POINT_MSG = " num of favor point is = ";

    //used to realize useTool()
    private final ToolEffectRealization strategy;
    private Boolean boolWindowCard;
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

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    /**
     * Constructor
     * @param id > 0 && id < 12
     * @param name of the tool card
     * @param color of the dice for single player
     * @param toolEffectRealization used for the implementation of useTool()
     */
    public ToolCard(int id, String name, Colors color, ToolEffectRealization toolEffectRealization) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.favorPoint = 0;
        this.strategy = toolEffectRealization;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        String logMsg = MessageFormat.format("{0}{1}{2}{3}{4}{5}{6}{7}",
                DUMP_ID_MSG, id, DUMP_NAME_MSG, name, DUMP_COLOR_MSG, color, DUMP_FAVOR_POINT_MSG, favorPoint);
        logger.info(logMsg);
    }

    /**
     * @return copy of tool card
     */
    public ToolCard copy() {
        ToolCard ret = new ToolCard(this.id, this.name, this.color, this.strategy);
        ret.setActor(this.boolWindowCard, this.roundTrack, this.draft, this.diceBag);
        return ret;
    }

    /**
     * @return name of this tool card
     */
    public String getName() {
        return name;
    }

    /**
     * @return id of this tool card
     */
    public int getId() {
        return id;
    }

    /**
     * @return color of this tool card, used in single player
     */
    public Colors getColor() {
        return color;
    }

    /**
     * @return number of favor point on this tool card
     */
    public int getFavorPoint() {
        return favorPoint;
    }

    /**
     * @param favorPoint to set on this tool card
     */
    public void setFavorPoint(int favorPoint) {
        this.favorPoint = favorPoint;
    }

    /**
     * Adds 1 or 2 favor points depending on how much there were before and returns how much were added
     * @return how much favor point were added to the tool
     */
    public int addFavorPoint() {
        if (favorPoint == 0) {
            favorPoint++;
            return 1;
        }
        favorPoint += 2;
        return 2;
    }

    /**
     * Checks if player passed has enough favor points to play this tool card
     * @param p player to check
     * @return true if player has enough favor point, false else
     */
    public Boolean checkFavorPoint(Player p) {
        if (p.getFavorPoint() == 0)
            return false;

        if (favorPoint == 0)
            return p.getFavorPoint() >= 1;

        return p.getFavorPoint() >= 2;
    }

    /**
     * Used by the Factory to sets objects involved in this tool card
     * @param roundTrack of the game
     * @param draft of the current Round
     * @param diceBag of the game
     */
    public void setActor(Boolean boolWindowCard, RoundTrack roundTrack, Draft draft, DiceBag diceBag) {
        this.boolWindowCard = boolWindowCard;
        this.draft = draft;
        this.diceBag = diceBag;
        this.roundTrack = roundTrack;
    }

    /**
     * Might return @Nullable Object
     * @return list of Object involved in this tool card.
     */
    public List<Actor> getActor() {
        List<Actor> ret = new ArrayList<>();

        if (boolWindowCard)
            ret.add(Actor.WINDOW_CARD);

        if (roundTrack != null)
            ret.add(Actor.ROUND_TRACK);

        if(diceBag != null)
            ret.add(Actor.DICE_BAG);

        if(draft != null)
            ret.add(Actor.DRAFT);

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
    public List<Parameter> askParameter() {
        List<Parameter> list = new ArrayList<>();
        List<Integer> addOneDice = Arrays.asList(1, 2, 3, 6, 9, 10, 11);
        List<Integer> addTwoDice = Arrays.asList(4, 5, 12);
        List<Integer> addOneCell = Arrays.asList(2, 3, 9, 11);
        List<Integer> addTwoCell = Arrays.asList(4, 12);

        if (addOneDice.contains(id))
            list.add(Parameter.DICE);

        if (addTwoDice.contains(id)) {
            list.add(Parameter.DICE);
            list.add(Parameter.DICE);
        }

        if (addOneCell.contains(id))
            list.add(Parameter.CELL);

        if (addTwoCell.contains(id)) {
            list.add(Parameter.CELL);
            list.add(Parameter.CELL);
        }

        if (id == 1)
            list.add(Parameter.BOOLEAN);

        else if (id == 11)
            list.add(Parameter.INTEGER);

        else if (id == 12)
            list.add(Parameter.COLOR);

        return list;
    }

    /**
     * Checks if correct elements have been selected for this tool
     * @param dices null when not needed
     * @param cells null when not needed
     * @param diceValue 0 when not needed
     * @param diceColor null when not needed
     * @return true if selected elements are correct for this tool, else false
     * @throws PositionException when cells have invalid positions
     */
    public boolean checkTool(List<Dice> dices, List<Cell> cells, int diceValue, Colors diceColor) throws PositionException {
        if (id == 1)
            return dices.size()==1 && strategy.checkDiceDraft(dices.get(0));

        else if (id == 2 || id == 3)
            return dices.size()==1 && cells.size()==1 && strategy.checkDiceWinCard(dices.get(0), windowCard);

        else if (id == 4)
            return dices.size()==2 && cells.size()==2 && strategy.checkDiceWinCard(dices.get(0), windowCard) && strategy.checkDiceWinCard(dices.get(1), windowCard);

        else if (id == 5)
            return dices.size()==2 && strategy.checkDiceDraft(dices.get(0)) && strategy.checkDiceRoundTrack(dices.get(1));  // first dice is in draft, second in round track

        else if (id == 6 || id == 10)
            return dices.size()==1 && strategy.checkDiceDraft(dices.get(0));

        else if (id == 9)
            return dices.size()==1 && strategy.checkDiceDraft(dices.get(0)) && cells.size()==1 && !windowCard.checkNeighbors(cells.get(0));

        else if (id == 11) {
            if (diceValue < 1 || diceValue > 6)
                return false;
            else
                this.diceValue = diceValue;
            return dices.size()==1 && strategy.checkDiceDraft(dices.get(0)) && cells.size()==1;
        }

        else if (id == 12)
            return strategy.checkTool12(dices, cells, diceColor, windowCard);

        return true;
    }

    /**
     * Realize the effect of the tool card on the passed parameter
     * @param dices null when not needed
     * @param up null when not needed
     * @param cells null when not needed
     * @return true if move was successful, else false
     * @throws ValueException when wrong value are chosen
     * @throws IDNotFoundException when couldn't find a dice
     * @throws NotEmptyException when trying to stack dice on the same cell
     * @throws EmptyException when trying to get dice from empty draft or bag
     * @throws SameDiceException when trying to put the same dice twice
     * @throws RoundNotFoundException when wrong round is requested
     */
    public boolean useTool(List<Dice> dices, Boolean up, List<Cell> cells) throws ValueException, IDNotFoundException, NotEmptyException,
            EmptyException, SameDiceException, RoundNotFoundException {
        Boolean ret = false;

        if (id == 1) {
            ret = strategy.changeValue(draft.findDice(dices.get(0).getID()), up);
        }
        else if (id == 2) {
            Dice d = windowCard.getWindow().getCell(dices.get(0)).getDice();
            Cell c = windowCard.getWindow().getCell(cells.get(0).getRow(), cells.get(0).getCol());
            ret = strategy.moveOneDice(d, c, "color", windowCard);
        }
        else if (id == 3) {
            Dice d = windowCard.getWindow().getCell(dices.get(0)).getDice();
            Cell c = windowCard.getWindow().getCell(cells.get(0).getRow(), cells.get(0).getCol());
            ret = strategy.moveOneDice(d, c, "value", windowCard);
        }
        else if (id == 4)
            ret = strategy.moveExTwoDice(dices, cells, windowCard);
        else if (id == 5)
            ret = strategy.moveFromDraftToRound(dices);
        else if (id == 6) {
            Dice d = draft.findDice(dices.get(0).getID());
            d.rollDice();
            draft.setChangedAndNotify();
            ret = true;
        }
        else if (id == 7) {
            draft.rollDraft();
            ret = true;
        }
        else if (id == 8) {
            player.setPlayedDice(false);
            player.setSecondTurn(false);
            ret = true;
        }
        else if (id == 9) {
            Cell dest = windowCard.getWindow().getCell(cells.get(0).getRow(), cells.get(0).getCol());
            ret = strategy.moveFromDraftToCard(dices.get(0), dest, windowCard);
        }
        else if (id == 10) {
            Dice d = draft.findDice(dices.get(0).getID());
            d.changeValue(7 - dices.get(0).getValue());
            draft.setChangedAndNotify();
            ret = true;
        }
        else if (id == 11)
            ret = strategy.moveFromDraftToBagThanPlace(dices.get(0), cells.get(0), diceValue, windowCard);
        else if (id == 12) {
            List<Cell> destinations = new ArrayList<>();
            for (Cell c : cells) {
                if (windowCard.getWindow().getCell(c.getRow(), c.getCol()) != null)
                    destinations.add(windowCard.getWindow().getCell(c.getRow(), c.getCol()));
            }
            ret = strategy.moveUpToTwoDice(dices, destinations, windowCard);
        }

        return ret;
    }

    /**
     * Set changed and notify observers
     */
    public void setChangedAndNotify(String username) {
        setChanged();
        notifyObservers(username);
    }

}
