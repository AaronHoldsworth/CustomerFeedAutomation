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
import org.json.JSONObject;
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
        emsMessageHandler = new EMSMessageHandler();
        emsMessageHandler.CreateEMSMessage(xmlPath, systemId);
        properties = emsMessageHandler.Properties();
        messageBody = emsMessageHandler.MessageBody();

        messageSent = emsConnector.SendEmsMessageToC4C(properties, messageBody);
    }

    private void CheckTibcoSuccess() {
        if (!connectionSuccessful) {
            fail("Failed to create TIBCO Connection");
        } else {
            if (!messageSent) {
                fail("Message Not Sent to TIBCO");
            }
        }
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

        String result = testCaseName + "," + testCaseResult + "," + systemId;
        resultsList.add(result);

    }

    
    @Test
    public void UK_MDM_01_RecordWithAllMandatoryElements() {
        testCaseName = "SCV-2670,MDM-01 Verify All Mandatory Elements";
        
        CreateMessageForTest("AutomationXmls\\MDM01_AllMandatoryElements.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
        CheckTibcoSuccess();
        assertNotNull(record);
        testWasSuccesful = (record != null);
    }
    
    @Test
    public void UK_MDM_01_RecordMissingTitle() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing Title is not created";
        
        CreateMessageForTest("AutomationXmls\\MDM01_MissingTitle.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
        CheckTibcoSuccess();
        
        assertNull(record);
        testWasSuccesful = (record == null);
    }
    
    @Test
    public void UK_MDM_01_RecordMissingFirstName() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing First Name is not created";
        
        CreateMessageForTest("AutomationXmls\\MDM01_MissingFirstName.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
        CheckTibcoSuccess();
        
        assertNull(record);
        testWasSuccesful = (record == null);
        
    }
    
    @Test
    public void UK_MDM_01_RecordMissingLastName() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing Last Name is not created";
        
        CreateMessageForTest("AutomationXmls\\MDM01_MissingLastName.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
        CheckTibcoSuccess();
        
        assertNull(record);
        testWasSuccesful = (record == null);
        
    }
    
    @Test
    public void UK_MDM_01_RecordMissingLContactPoint() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer with No Contact Points is not created";
        
        CreateMessageForTest("AutomationXmls\\MDM01_MissingContactPoint.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
        CheckTibcoSuccess();
        
        assertNull(record);
        testWasSuccesful = (record == null);
        
    }
    
    public void UK_MDM_02_MissingFirstNameOnEmit() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        
        CreateMessageForTest("AutomationXmls\\MDM02_MissingFirstNameOnEmit.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
    }
    
    public void UK_MDM_02_MissingLastNameOnEmit() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        
        CreateMessageForTest("AutomationXmls\\MDM02_MissingLastNameOnEmit.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
    }
    
    public void UK_MDM_04_RemoveNonAlphanumericCharactersFromFirstName() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        
        CreateMessageForTest("AutomationXmls\\MDM04_RemoveNonAlphaFirstName.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
    }
    
    public void UK_MDM_04_RemoveNonAlphanumericCharactersFromLastName() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        
        CreateMessageForTest("AutomationXmls\\MDM04_RemoveNonAlphaLastName.xml");
        
        utilities.WaitForMessage();
        
        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        
    }

    @Test
    public void UK_MDM_06_VerifyDummyNameForFirstName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for First Name";

        CreateMessageForTest("AutomationXmls\\MDM06_DummyFirstName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
       String lastNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        CheckTibcoSuccess();

        assertNull(firstNameValue);
        assertTrue(lastNameValue.equalsIgnoreCase("sher"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase("sher"));

    }
    
    @Test
    public void UK_MDM_06_VerifyDummyNameForLastName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Last Name";

        CreateMessageForTest("AutomationXmls\\MDM06_DummyLastName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
        String lastNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        CheckTibcoSuccess();

        assertNull(lastNameValue);
        assertTrue(firstNameValue.equalsIgnoreCase("sher"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase("sher"));

    }
    
    @Test
    public void UK_MDM_06_VerifyDummyNameForMiddletName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Middle Name";

        CreateMessageForTest("AutomationXmls\\MDM06_DummyMiddleName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
        String lastNameValue;
        String middleNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        middleNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        
        CheckTibcoSuccess();

        assertNull(middleNameValue);
        
        assertTrue(firstNameValue.equalsIgnoreCase("sher"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase("sher"));
        assertTrue(lastNameValue.equalsIgnoreCase("Navin"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase("Navin"));

    }
    
    @Test
    public void UK_MDM_06_VerifyTestNameForFirstName() {
        testCaseName = "SCV-2676,MDM-06 Verify Test is nulled for First Name";

        CreateMessageForTest("AutomationXmls\\MDM06_TestFirstName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
       String lastNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        CheckTibcoSuccess();

        assertNull(firstNameValue);
        assertTrue(lastNameValue.equalsIgnoreCase("Lion"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase("Lion"));

    }
    
    @Test
    public void UK_MDM_06_VeriyTestNameForLastName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Last Name";

        CreateMessageForTest("AutomationXmls\\MDM06_TestLastName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
        String lastNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        CheckTibcoSuccess();

        assertNull(lastNameValue);
        assertTrue(firstNameValue.equalsIgnoreCase("Tiger"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase("Tiger"));

    }
    
    @Test
    public void UK_MDM_06_VerifyTestNameForMiddletName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Middle Name";

        CreateMessageForTest("AutomationXmls\\MDM06_TestMiddleName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
        String lastNameValue;
        String middleNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        middleNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        
        CheckTibcoSuccess();

        assertNull(middleNameValue);
        
        assertTrue(firstNameValue.equalsIgnoreCase("Luton"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase("Luton"));
        assertTrue(lastNameValue.equalsIgnoreCase("Wens"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase("Wns"));

    }
    
    @Test
    public void UK_MDM_06_VerifyTestNameForAllName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Middle Name";

        CreateMessageForTest("AutomationXmls\\MDM06_AllTestName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue; 
        String lastNameValue;
        String middleNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        middleNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        
        CheckTibcoSuccess();
        
        assertNull(firstNameValue);
        assertNull(middleNameValue);
        assertNull(lastNameValue);

    }
    
}
