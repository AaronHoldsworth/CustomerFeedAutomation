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
import java.util.Properties;
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

    static TestUtilities utilities;
    static EMSConnector emsConnector;
    static MongoConnector mongoConnector;
    static Properties prop;
    static ResultsGenerator resGen = new ResultsGenerator();
    ;
    private HashMap<String, String> properties;
    private String messageBody;
    private String testCaseName;
    private String testCaseResult;
    private boolean testWasSuccesful;
    TestResult testResult;
    private String systemId;
    EMSMessageHandler emsMessageHandler;
    static List<String> resultsList = new ArrayList<>();

    public MDMBusinessRules_Test() {
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
        resGen.WriteResultsToFile(resultsList);
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

        systemId = utilities.GenerateGuid();
        emsMessageHandler = new EMSMessageHandler("AutomationXmls\\MDM01_AllMandatoryElements.xml", systemId);

        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();

        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        assertNotNull(record);

        testWasSuccesful = (record != null);
    }

    @Test
    public void UK_MDM_01_RecordMissingTitle() {

        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();

        systemId = utilities.GenerateGuid();
        emsMessageHandler = new EMSMessageHandler("AutomationXmls\\MDM01_MissingTitle.xml", systemId);

        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();

        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        assertNull(record);

        testWasSuccesful = (record == null);
    }
}
