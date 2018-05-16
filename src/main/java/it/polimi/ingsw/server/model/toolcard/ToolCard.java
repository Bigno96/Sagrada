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

        return list;
    }

    public boolean checkTool(List<Dice> dices, List<Cell> cells) throws IDNotFoundException {
        if (id == 1) {
            return dices.size()==1 && checkDiceIn(dices.get(0), windowCard);
        }
        else if (id == 2 || id ==3) {
            return dices.size()==1 && cells.size()==1 && checkDiceIn(dices.get(0), windowCard);
        }
        else if (id == 4) {
            return dices.size()==2 && cells.size()==2 && checkDiceIn(dices.get(0), windowCard) && checkDiceIn(dices.get(1), windowCard);
        }

        return true;
    }

    public boolean useTool(List<Dice> dices, Boolean up, List<Cell> cells) throws ValueException, IDNotFoundException, NotEmptyException {
        if (id == 1)
            return changeValue(dices.get(0), up);
        else if (id == 2)
            return moveOneDice(dices.get(0), cells.get(0), "color");
        else if (id == 3)
            return moveOneDice(dices.get(0), cells.get(0), "value");
        else if (id == 4)
            return moveExTwoDice(dices, cells);
        return true;
    }

    private boolean checkDiceIn(Dice d, WindowCard windowCard) throws IDNotFoundException {
        return windowCard.getWindow().containsDice(d);
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
        if (restrictionIgnored.equals("color")) {
            windowCard.getWindow().getCell(d).freeCell();
            dest.setDice(d);
            dest.setIgnoreColor();
            return true;
        }
        else if (restrictionIgnored.equals("value")) {
            windowCard.getWindow().getCell(d).freeCell();
            dest.setDice(d);
            dest.setIgnoreValue();
            return true;
        }
        return false;
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
}
