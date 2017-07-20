/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author TTGAHX
 */
public class EMSConnector_Test {

    static EMSConnector emsConnector;

    public EMSConnector_Test() {
        emsConnector = new EMSConnector();
        String queueName = "TUI.CP.MDM.DEV.CUSTOMER.0300.CUSTOMERSOURCEEVENT.UK.Q.ACTION";
        emsConnector.ConnectToGIP(queueName);
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
        String queueName = "TUI.CP.MDM.DEV.CUSTOMER.0300.CUSTOMERSOURCEEVENT.UK.Q.ACTION";
        emsConnector.ConnectToGIP(queueName);
    }

    @Test
    public void TestSendMessageToC4C() {
        TestUtilities tu = new TestUtilities();
        String file = tu.LoadTestFile("Test.xml");
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        HashMap<String, String> properties = tu.CreatePropertiesHashMap(propertyList);
        String messageBody = splitFile[1].trim();
        emsConnector.SendEmsMessageToC4C(properties, messageBody);
    }

    


}
