package it.polimi.model;

import junit.framework.TestCase;

public class RoomTest extends TestCase{

    private Room room1 = new Room(1, Room.status.BOOT, 1);
    private Room room2 = new Room(2, Room.status.START, 3);

    public RoomTest( String testName )
    {
        super( testName );
    }

    public void testGetId()
    {
        assertEquals( 1, room1.getId());
    }

    public void testGetNPlayer(){
        assertEquals(3, room2.getNPlayer());
    }

    public void testGetStatus(){
        assertEquals(Room.status.BOOT, room1.getStatus());
    }

    public void testToString()
    {
        assertEquals( "it.polimi.model.Room@ ID: 1 Status: " + Room.status.BOOT + " nPlayer: 1", room1.toString() );
    }

    public void testDump()
    {
        assertEquals( "ID: 2 Col: " + Room.status.START + " nPlayer: 3", room2.dump() );
    }

    public void testGetDelay()
    {
        assertEquals( 60000, room1.getDelay());
    }

    public void testStartTimer(){
        room1.StartTimer();
        assertTrue(room1.getDelay() < 60000);
    }

}