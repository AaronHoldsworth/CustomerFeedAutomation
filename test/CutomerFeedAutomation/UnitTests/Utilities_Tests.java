/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.UnitTests;

import CutomerFeedAutomation.TestUtils.TestUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TTGAHX
 */
public class Utilities_Tests {

    public Utilities_Tests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void TestNameGenerator() {
        TestUtilities tu = new TestUtilities();

        String firstName = tu.GenerateName();
        String lastName =tu.GenerateName();
        
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertNotEquals(firstName, lastName);
    }
}
