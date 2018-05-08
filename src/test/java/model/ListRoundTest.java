package model;

import exception.IDNotFoundException;
import junit.framework.TestCase;

public class ListRoundTest extends TestCase {

    public void testListRound() throws IDNotFoundException {
        Dice dice1G = new Dice(1, Colors.YELLOW);
        Dice dice2B = new Dice(2, Colors.BLUE);
        DiceBag db = new DiceBag();
        int nDice = 9;
        Draft draft = new Draft(db, nDice);
        ListRound listRound = new ListRound();


    }
}
