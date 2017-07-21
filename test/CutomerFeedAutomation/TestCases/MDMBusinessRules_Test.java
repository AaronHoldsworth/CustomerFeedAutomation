/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.TestCases;

import CutomerFeedAutomation.EMSConnector;
import CutomerFeedAutomation.TestUtils.EMSMessageHandler;
import CutomerFeedAutomation.MongoConnector;
import CutomerFeedAutomation.TestUtils.ResultsGenerator;
import CutomerFeedAutomation.TestUtils.TestUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestResult;
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
public class MDMBusinessRules_Test {

    final TestUtilities utilities;
    final EMSConnector emsConnector;
    final MongoConnector mongoConnector;
    static final ResultsGenerator RES_GEN = new ResultsGenerator();
    private HashMap<String, String> properties;
    private String messageBody;
    private String testCaseName;
    private String testCaseResult;
    private boolean testWasSuccesful = false;
    TestResult testResult;
    private String systemId;
    EMSMessageHandler emsMessageHandler;
    static List<String> resultsList = new ArrayList<>();
    private boolean connectionSuccessful;
    private boolean messageSent;

    public MDMBusinessRules_Test() {
        utilities = new TestUtilities();
        emsConnector = new EMSConnector();
        mongoConnector = new MongoConnector();
        mongoConnector.getMongoConnection();
        String queueName = "TUI.CP.MDM.DEV.CUSTOMER.0300.CUSTOMERSOURCEEVENT.UK.Q.ACTION";
        connectionSuccessful = emsConnector.ConnectToGIP(queueName);

    }

    private void CreateMessageForTest(String xmlPath) {

        systemId = utilities.GenerateGuid();
        emsMessageHandler = new EMSMessageHandler(xmlPath, systemId);

        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();

        messageSent = emsConnector.SendEmsMessageToC4C(properties, messageBody);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        RES_GEN.WriteResultsToFile(resultsList);
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        testCaseResult = (testWasSuccesful ? "Pass" : "Fail");

        String result = testCaseName + ",Result:" + testCaseResult + ",CustomerID:" + systemId;
        resultsList.add(result);

    }

    @Test
    public void UK_MDM_01_RecordWithAllMandatoryElements() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();

        CreateMessageForTest("AutomationXmls\\MDM01_AllMandatoryElements.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        if (!messageSent) {
            fail("Message Not Sent to TIBCO");
        } else if (!connectionSuccessful) {
            fail("Connection failed to create");
        } else {
            assertNotNull(record);
            testWasSuccesful = (record != null);
        }
    }

    @Test
    public void UK_MDM_01_RecordMissingTitle() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();

        CreateMessageForTest("AutomationXmls\\MDM01_MissingTitle.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        assertNull(record);

        if (!messageSent) {
            fail("Message Not Sent to TIBCO");
        } else if (!connectionSuccessful) {
            fail("Connection failed to create");
        } else {
            assertNotNull(record);
            testWasSuccesful = (record == null);
        }
    }

}
