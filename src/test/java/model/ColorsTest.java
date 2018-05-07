package model;

import junit.framework.TestCase;

public class ColorsTest extends TestCase {

    public ColorsTest(String testName) {
        super(testName);
    }

    public void testRandomColors() {
        assertNotNull(Colors.random());
    }
}
