package it.polimi.model;

import junit.framework.TestCase;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
>>>>>>> e5fe01a817e2327149cd57a141a9c97860736a96

public class PlayerTest extends TestCase {

    private Player player1 = new Player(1);
    private Player player2 = new Player(2);

    public PlayerTest(String testName){
        super(testName);
    }

<<<<<<< HEAD
    public void testGetId() {
        assertEquals(1, player1.getID());
        assertEquals(2, player2.getID());
    }

    public void testFavorPoint() {
=======
    public void testGetId(){
        assertEquals(1, player1.getId());
        assertEquals(2, player2.getId());
    }

    public void testSetFavor1(){
        assertEquals(0, player1.getFavorPoint());
    }

    public void testSetFavor2(){
>>>>>>> e5fe01a817e2327149cd57a141a9c97860736a96
        player1.setFavorPoint(1);
        assertEquals(1, player1.getFavorPoint());
    }

}
