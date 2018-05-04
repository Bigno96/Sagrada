package it.polimi.model;

public class PlayerTest {

    private Player player1 = new Player(1);
    private Player player2 = new Player(2);

    public PlayerTest(String testName){
        super(testName);
    }

    public void testGetId(){
        assertEquals(1, player1.getID());
        assertEquals(2, player2.getID());
    }

    public void testSetFavor(){
        assertEquals(0, player1.getFavorPoint());
    }

    public void testSetFavor(){
        player1.setFavorPoint(1);
        assertEquals(1, player1.getFavorPoint());
    }

    public void testToString() {
        assertEquals("it.polimi.model.Player@ " + Player1.hashCode(), Player1.toString());
    }

    public void testToString() {
        assertEquals("it.polimi.model.Player@ " + Player2.hashCode(), Player2.toString());
    }

    public void testDump(){ player1.dump(); }

}
