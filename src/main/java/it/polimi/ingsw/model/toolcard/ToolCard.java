package it.polimi.ingsw.model.toolcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.ValueException;
import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.dicebag.Draft;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.roundtrack.RoundTrack;
import it.polimi.ingsw.model.windowcard.WindowCard;

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
    private List<Dice> dices;
    private boolean up;


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
    }

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

    // check if the elements are selected correctly
    public boolean checkTool() throws IDNotFoundException {
        if (id == 1)
            return checkDiceIn(dices.get(0), windowCard);

        return true;
    }

    // use tool
    public boolean useTool() throws ValueException {
        if (id == 1)
            changeValue(dices.get(0), up);
        
        return true;
    }

    // check if the tool is activable
    public boolean checkPreCondition(Player player) {
        if (id == 7 && (player.isFirstTurn() || player.isSecondTurn()))
            return false;
        return id != 8 || (!player.isFirstTurn() && player.isSecondTurn());
    }

    public boolean checkDiceIn(Dice d, WindowCard windowCard) throws IDNotFoundException {
        return windowCard.getWindow().containsDice(d);
    }

    private boolean changeValue(Dice d, boolean up) throws ValueException {

        if (up)
            d.changeValue(d.getValue()+1);
        else
            d.changeValue(d.getValue()-1);

        return up;
    }
}
