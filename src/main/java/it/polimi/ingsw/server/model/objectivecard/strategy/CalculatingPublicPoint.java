package it.polimi.ingsw.server.model.objectivecard.strategy;

import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.io.Serializable;

public class CalculatingPublicPoint implements CalculatingPoint, Serializable {

    private static final String COLOR = "color";
    private static final String DIFF = "diff";
    private static final String COLUMN = "col";
    private static final String VAR = "var";
    private static final String LIGHT = "light";
    private static final String MEDIUM = "medium";

    private final ObjectiveCalculator calculator;

    public CalculatingPublicPoint() {
        this.calculator = ObjectiveCalculator.getInstance();
    }

    /**
     * @param windowCard    where to apply the objective card
     * @param objectiveCard to apply
     * @return sum of points
     * @throws PositionException when error occurs in finding cells in window card
     */
    @Override
    public int calcPoint(WindowCard windowCard, ObjectiveCard objectiveCard) throws PositionException {
        String type = objectiveCard.getType();
        String scope = objectiveCard.getScope();
        String dir = objectiveCard.getDir();
        String grad = objectiveCard.getGrad();

        if (type.equals(COLOR))
            return parseCol(scope, dir, windowCard, objectiveCard);
        else
            return parseShade(scope, grad, dir, windowCard, objectiveCard);
    }

    /**
     * Used to parse parameter in case type = color
     * @param scope json parameter scope
     * @param dir json parameter direction
     * @param windowCard where apply the objective card
     * @param objectiveCard to apply
     * @return point calculated basing on what parsed
     * @throws PositionException when error occurs in finding cells in window card
     */
    private int parseCol(String scope, String dir, WindowCard windowCard, ObjectiveCard objectiveCard) throws PositionException {
        if (scope.equals(DIFF))
            if (dir.equals(COLUMN))
                return calculator.calcDifferentColumnColor(windowCard, objectiveCard);
            else
                return calculator.calcDifferentRowColor(windowCard, objectiveCard);
        if (scope.equals(VAR))
            return calculator.calcVarietyColor(windowCard, objectiveCard);
        else // scope.equals("diag")
            return calculator.calcDiagonalColor(windowCard);
    }

    /**
     * Used to parse parameter in case type = shade
     * @param scope  json parameter scope
     * @param grad  json parameter gradation
     * @param dir  json parameter direction
     * @param windowCard where apply the objective card
     * @param objectiveCard to apply
     * @return point calculated basing on what parsed
     */
    private int parseShade(String scope, String grad, String dir, WindowCard windowCard, ObjectiveCard objectiveCard) {
        if (scope.equals(DIFF))
            if (dir.equals(COLUMN))
                return calculator.calcDifferentColumnShade(windowCard, objectiveCard);
            else
                return calculator.calcDifferentRowShade(windowCard, objectiveCard);
        if (scope.equals(VAR))
            return calculator.calcVarietyShade(windowCard, objectiveCard);
        else { //scope.equals("grad")
            if (grad.equals(LIGHT))
                return calculator.calcGradationShade(1 ,2, windowCard, objectiveCard);
            if (grad.equals(MEDIUM))
                return calculator.calcGradationShade(3 ,4, windowCard, objectiveCard);
            else //grad.equals("dark")
                return calculator.calcGradationShade(5 ,6, windowCard, objectiveCard);
        }
    }
}
