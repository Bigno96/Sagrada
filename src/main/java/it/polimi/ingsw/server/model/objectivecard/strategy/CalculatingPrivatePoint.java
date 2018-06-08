package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;

public class CalculatingPrivatePoint implements CalculatingPoint, Serializable {

    private final ObjectiveCalculator calculator;

    public CalculatingPrivatePoint() {
        this.calculator = ObjectiveCalculator.getInstance();
    }

    @Override
    public int calcPoint(WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException {
        Colors col = Colors.parseColor(objectiveCard.getType());

        if (col.equals(Colors.WHITE))
            throw new IDNotFoundException("Objective Card id not found");

        return calculator.calcPointPriv(col, windowCard);
    }
}
