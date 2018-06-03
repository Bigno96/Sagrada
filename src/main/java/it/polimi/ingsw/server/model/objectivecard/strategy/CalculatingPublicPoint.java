package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

public class CalculatingPublicPoint implements CalculatingPoint {

    private ObjectiveCalculator calculator;

    public CalculatingPublicPoint() {
        this.calculator = new ObjectiveCalculator();
    }

    @Override
    public int calcPoint(WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException, PositionException {
        String type = objectiveCard.getType();
        String scope = objectiveCard.getScope();
        String dir = objectiveCard.getDir();
        String grad = objectiveCard.getGrad();

        if (type.equals("color"))
            return parseCol(scope, dir, windowCard, objectiveCard);
        else
            return parseShade(scope, grad, dir, windowCard, objectiveCard);
    }

    private int parseCol(String scope, String dir, WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException, PositionException {
        if (scope.equals("diff"))
            if (dir.equals("col"))
                return calculator.calcDifferentColumnColor(windowCard, objectiveCard);
            else
                return calculator.calcDifferentRowColor(windowCard, objectiveCard);
        if (scope.equals("var"))
            return calculator.calcVarietyColor(windowCard, objectiveCard);
        else // scope.equals("diag")
            return calculator.calcDiagonalColor(windowCard);
    }

    private int parseShade(String scope, String grad, String dir, WindowCard windowCard, ObjectiveCard objectiveCard) throws IDNotFoundException {
        if (scope.equals("diff"))
            if (dir.equals("col"))
                return calculator.calcDifferentColumnShade(windowCard, objectiveCard);
            else
                return calculator.calcDifferentRowShade(windowCard, objectiveCard);
        if (scope.equals("var"))
            return calculator.calcVarietyShade(windowCard, objectiveCard);
        else { //scope.equals("grad")
            if (grad.equals("light"))
                return calculator.calcGradationShade(1 ,2, windowCard, objectiveCard);
            if (grad.equals("medium"))
                return calculator.calcGradationShade(3 ,4, windowCard, objectiveCard);
            else //grad.equals("dark")
                return calculator.calcGradationShade(5 ,6, windowCard, objectiveCard);
        }
    }
}
