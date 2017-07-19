/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.bson.Document;
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
public class MDMBusinessRulesTestCases {

    static TestUtilities utilities;
    static EMSConnector emsConnector;
    static MongoConnector mongoConnector;
    static Properties prop;
    private HashMap<String, String> properties;
    private String messageBody;
    EMSMessageHandler emsMessageHandler;


    public MDMBusinessRulesTestCases() {
        utilities = new TestUtilities();
        emsConnector = new EMSConnector();
        mongoConnector = new MongoConnector();
        mongoConnector.getMongoConnection();

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
    public void UK_MDM_01_RecordWithAllMandatoryElements() {

        String systemId = utilities.GenerateGuid();
        emsMessageHandler = new EMSMessageHandler("C:\\AutomationXmls\\MDM01_AllMandatoryElements.xml", systemId);
        
        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();
        
        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        assertNotNull(record);

    }

    @Test
    public void UK_MDM_01_RecordMissingTitle() {
        String systemId = utilities.GenerateGuid();
        emsMessageHandler = new EMSMessageHandler("C:\\AutomationXmls\\MDM01_MissingTitle.xml", systemId);
        
        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();
        
        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        assertNull(record);
    }
}
