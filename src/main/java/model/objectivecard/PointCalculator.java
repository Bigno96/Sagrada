package model.objectivecard;

import exception.IDNotFoundException;
import exception.PositionException;
import model.Colors;
import model.windowcard.Cell;
import model.windowcard.WindowCard;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

class PointCalculator {

    public PointCalculator() {
        // simple create an instance of ObjectiveStrategy
    }

    public int calcPointPriv(Colors col, WindowCard winCard) throws IDNotFoundException {
        int sum = 0;

        for (Iterator<Cell> itr = winCard.getOrizzItr(); itr.hasNext();) {
            Cell c = itr.next();
            if (c.getDice().getColor() == col) {
                sum += c.getDice().getValue();
            }
        }
        
        return sum;
    }

    public int calcDifferentColumnColor(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        Cell c;
        int sum = 0;
        HashSet<Colors> colorFound = new HashSet<>();

        for (int j=0; j < 5; j++) {
            for (int i=0; i < 4; i++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied() && colorFound.contains(c.getDice().getColor()))
                    break;
                else
                    colorFound.add(c.getDice().getColor());
            }

            if (colorFound.size() == 4)
                sum += objective.getPoint();

            colorFound.clear();
        }

        return sum;
    }

    public int calcDifferentRowColor(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        Cell c;
        int sum = 0;
        HashSet<Colors> colorFound = new HashSet<>();

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 5; j++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied() && colorFound.contains(c.getDice().getColor()))
                    break;
                else
                    colorFound.add(c.getDice().getColor());
            }

            if (colorFound.size() == 5)
                sum += objective.getPoint();

            colorFound.clear();
        }

        return sum;
    }

    public int calcVarietyColor(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        int sum = 0;
        HashSet<Colors> var1 = new HashSet<>();
        HashSet<Colors> var2 = new HashSet<>();
        HashSet<Colors> var3 = new HashSet<>();
        HashSet<Colors> var4 = new HashSet<>();
        Iterator<Cell> itr = winCard.getOrizzItr();

        while (itr.hasNext()) {
            Cell c = itr.next();
            if (c.isOccupied()) {
                Colors col = c.getDice().getColor();
                if (!var1.contains(col)) {
                    var1.add(col);
                } else if (!var2.contains(col)) {
                    var2.add(col);
                } else if (!var3.contains(col)) {
                    var3.add(col);
                } else if (!var4.contains(col)) {
                    var4.add(col);
                }
            }
        }

        if (var1.size()==6)
            sum += objective.getPoint();
        if (var2.size()==6)
            sum += objective.getPoint();
        if (var3.size()==6)
            sum += objective.getPoint();
        if (var4.size()==6)
            sum += objective.getPoint();

        return sum;
    }

    public int calcDiagonalColor(WindowCard winCard, ObjectiveCard objective) throws PositionException, IDNotFoundException {
        int sum = 0;

        for (Iterator<Cell> itr = winCard.getOrizzItr(); itr.hasNext();) {
            Cell c = itr.next();
            List<Cell> diagonal = winCard.getWindow().retDiagonal(c.getRow(), c.getCol());
            for (Cell cell : diagonal) {
                if (cell.getDice().getColor().equals(c.getDice().getColor())) {
                    sum++;
                    break;
                }
            }
        }

        return sum;
    }

    public int calcDifferentColumnShade(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        Cell c;
        int sum = 0;
        HashSet<Integer> valueFound = new HashSet<>();

        for (int j=0; j < 5; j++) {
            for (int i=0; i < 4; i++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied() && valueFound.contains(c.getDice().getValue()))
                    break;
                else
                    valueFound.add(c.getDice().getValue());
            }

            if (valueFound.size() == 4)
                sum += objective.getPoint();

            valueFound.clear();
        }

        return sum;
    }

    public int calcDifferentRowShade(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        Cell c;
        int sum = 0;
        HashSet<Integer> valueFound = new HashSet<>();

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 5; j++) {
                c = winCard.getWindow().getCell(i ,j);
                if (c.isOccupied() && valueFound.contains(c.getDice().getValue()))
                    break;
                else
                    valueFound.add(c.getDice().getValue());
            }

            if (valueFound.size() == 5)
                sum += objective.getPoint();

            valueFound.clear();
        }

        return sum;
    }

    public int calcVarietyShade(WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        int sum = 0;
        HashSet<Integer> var1 = new HashSet<>();
        HashSet<Integer> var2 = new HashSet<>();
        HashSet<Integer> var3 = new HashSet<>();
        Iterator<Cell> itr = winCard.getOrizzItr();

        while (itr.hasNext()) {
            Cell c = itr.next();
            if (c.isOccupied()) {
                int val = c.getDice().getValue();
                if (!var1.contains(val)) {
                    var1.add(val);
                } else if (!var2.contains(val)) {
                    var2.add(val);
                } else if (!var3.contains(val)) {
                    var3.add(val);
                }
            }
        }

        if (var1.size()==6)
            sum += objective.getPoint();
        if (var2.size()==6)
            sum += objective.getPoint();
        if (var3.size()==6)
            sum += objective.getPoint();

        return sum;
    }


    public int calcGradationShade(int val1, int val2, WindowCard winCard, ObjectiveCard objective) throws IDNotFoundException {
        int sum;
        int num1 = 0;
        int num2 = 0;
        Iterator<Cell> itr = winCard.getOrizzItr();

        while (itr.hasNext()) {
            Cell c = itr.next();
            if (c.isOccupied()) {
                if (c.getDice().getValue() == val1)
                    num1++;
                if (c.getDice().getValue() == val2)
                    num2++;
            }
        }

        if (num1 >= num2) {
            sum = objective.getPoint() * (num1 - (num1-num2));
        }
        else {
            sum = objective.getPoint() * (num2 - (num2-num1));
        }

        return sum;
    }
 }
