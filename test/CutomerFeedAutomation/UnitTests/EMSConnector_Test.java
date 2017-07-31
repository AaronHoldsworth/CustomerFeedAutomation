/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.UnitTests;

import CutomerFeedAutomation.EMSConnector;
import CutomerFeedAutomation.TestUtils.EMSMessageHandler;
import CutomerFeedAutomation.TestUtils.TestUtilities;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author TTGAHX
 */
public class EMSConnector_Test {

    static EMSConnector emsConnector;
    EMSMessageHandler emsHandler;

    public EMSConnector_Test() {
        emsConnector = new EMSConnector();
        emsConnector.ConnectToGIP();
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
    public void TestConnectToGIP() {
        boolean connectionSuccess = emsConnector.ConnectToGIP();

        assertTrue("Could not create TIBCO Connection", connectionSuccess);
    }

    @Test
    public void TestSendMessageToC4C() {

        emsHandler = new EMSMessageHandler();
        TestUtilities tu = new TestUtilities();
        String file = tu.LoadTestFile("Test.xml");
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        HashMap<String, String> properties = emsHandler.CreatePropertiesHashMap(propertyList);
        String messageBody = splitFile[1].trim();
        boolean messageSuccess = emsConnector.SendEmsMessageToC4C(properties, messageBody);

        assertTrue("Could not send message to TIBCO", messageSuccess);
    }

}
