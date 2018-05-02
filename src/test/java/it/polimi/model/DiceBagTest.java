package it.polimi.model;

import junit.framework.TestCase;

public class DiceBagTest extends TestCase {

    private DiceBag db1 = null, db2 = null;

    public DiceBagTest( String testName )
    {
        super( testName );
    }

    public void testInstanceUnique()
    {
        db1 = DiceBag.getInstance();
        db2 = DiceBag.getInstance();
        assertSame(db1, db2);
    }


}
