package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

public class ListDiceRoundTest extends TestCase {

    public ListDiceRoundTest(String testName) {
        super(testName);
    }

    public void testListRound() throws IDNotFoundException {
        Dice dice1G = new Dice(1, Colors.YELLOW);
        Dice dice2B = new Dice(2, Colors.BLUE);
        DiceBag db = new DiceBag();
        int nDice = 9;
        Draft draft = new Draft(db, nDice);
        ListDiceRound listDiceRound = new ListDiceRound();


    }
}
