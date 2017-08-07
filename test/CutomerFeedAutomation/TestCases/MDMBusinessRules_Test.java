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
import CutomerFeedAutomation.TestUtils.TestUtilities.eContactType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import junit.framework.TestResult;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
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
    private String genName;
    private boolean messageSent;

    public MDMBusinessRules_Test() {
        utilities = new TestUtilities();
        //emsConnector = new EMSConnector();
        emsConnector = null;
        mongoConnector = new MongoConnector();
        mongoConnector.getMongoConnection();
        //connectionSuccessful = emsConnector.ConnectToGIP();

    }

    private void CreateMessageForTest(String xmlPath) {

        systemId = utilities.GenerateGuid();
        genName = utilities.GenerateName();
        emsMessageHandler = new EMSMessageHandler();
        emsMessageHandler.CreateEMSMessage(xmlPath, systemId, genName);
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

    };

    @After
    public void tearDown() {
        testCaseResult = (testWasSuccesful ? "Pass" : "Fail");

        String result = testCaseName + "," + testCaseResult + "," + systemId;
        resultsList.add(result);

    }
    
    

    //@Test
    public void UK_MDM_01_RecordWithAllMandatoryElements() {
        testCaseName = "SCV-2670,MDM-01 Verify All Mandatory Elements";

        CreateMessageForTest("AutomationXmls\\MDM01_AllMandatoryElements.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        CheckTibcoSuccess();
        assertNotNull(record);
        testWasSuccesful = (record != null);
    }

    //@Test
    public void UK_MDM_01_RecordMissingTitle() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing Title is not created";

        CreateMessageForTest("AutomationXmls\\MDM01_MissingTitle.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        CheckTibcoSuccess();

        assertNull(record);
        testWasSuccesful = (record == null);
    }

    //@Test
    public void UK_MDM_01_RecordMissingFirstName() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing First Name is not created";

        CreateMessageForTest("AutomationXmls\\MDM01_MissingFirstName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        CheckTibcoSuccess();

        assertNull(record);
        testWasSuccesful = (record == null);

    }

    //@Test
    public void UK_MDM_01_RecordMissingLastName() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer Missing Last Name is not created";

        CreateMessageForTest("AutomationXmls\\MDM01_MissingLastName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        CheckTibcoSuccess();

        assertNull(record);
        testWasSuccesful = (record == null);

    }

    //@Test
    public void UK_MDM_01_RecordMissingLContactPoint() {
        testCaseName = "SCV-2671,MDM-01 Verify Customer with No Contact Points is not created";

        CreateMessageForTest("AutomationXmls\\MDM01_MissingContactPoint.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        CheckTibcoSuccess();

        assertNull(record);
        testWasSuccesful = (record == null);

    }
        
    //@Test
    public void UK_MDM_04_VerifySpecialCharacterstTrimmedForAllNames() {
        testCaseName = "SCV-XXX,MDM-04 Verify Speica Character are trimmed in all names ";

        CreateMessageForTest("AutomationXmls\\MDM04_AllName_TrimAllSpecialCharacter.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue;
        String lastNameValue;
        String jsonString = record.toJson();

        boolean expectedMiddleName = false;

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        List<String> middleNames = utilities.GetExtraNames(jsonRecord);

        for (String name : middleNames) {
            if (name.equalsIgnoreCase(genName)) {
                expectedMiddleName = true;
            }
        }

        CheckTibcoSuccess();

        assertTrue(firstNameValue.equalsIgnoreCase("Tiger"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase("Tiger"));
        assertTrue(lastNameValue.equalsIgnoreCase("Lioness"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase("Lioness"));
        assertTrue(expectedMiddleName);
    }

    //@Test
    public void UK_MDM_04_VerifyAllowedSpecialCharacters() {
        testCaseName = "SCV-XXX,MDM-04 Verify some special character allowed in Names";

        CreateMessageForTest("AutomationXmls\\MDM04_AllName_AcceptedSpecialCharacter.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue;
        String lastNameValue;
        String jsonString = record.toJson();
        boolean expectedMiddleName = false;

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        List<String> middleNames = utilities.GetExtraNames(jsonRecord);

        for (String name : middleNames) {
            if (name.equalsIgnoreCase(genName+"'ns")) {
                expectedMiddleName = true;
            }
        }

        CheckTibcoSuccess();

        assertTrue(firstNameValue.equalsIgnoreCase(genName+"'s"));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase(genName+"'s"));
        assertTrue(lastNameValue.equalsIgnoreCase(genName+"-e"));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase(genName+"-e"));
        assertTrue(expectedMiddleName);

    }

    //@Test
    public void UK_MDM_06_VerifyDummyNameForFirstName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for First Name";

        CreateMessageForTest("AutomationXmls\\MDM06_DummyFirstName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue;
        String lastNameValue;
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        try {
            firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            firstNameValue = null;
        }

        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        CheckTibcoSuccess();

        assertNull(firstNameValue);
        assertTrue(lastNameValue.equalsIgnoreCase(genName));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase(genName));

    }

    //@Test
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
        try {
            lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        } catch (Exception e) {
            lastNameValue = null;
        }

        CheckTibcoSuccess();

        assertNull(lastNameValue);
        assertTrue(firstNameValue.equalsIgnoreCase(genName));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase(genName));

    }

    //@Test
    public void UK_MDM_06_VerifyDummyNameForMiddleName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Middle Name";

        CreateMessageForTest("AutomationXmls\\MDM06_DummyMiddleName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue;
        String lastNameValue;
        JSONArray extraNames;
        List<String> middleNames = new ArrayList<>();
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");

        middleNames = utilities.GetExtraNames(jsonRecord);

        CheckTibcoSuccess();

        assertTrue(middleNames.isEmpty());

        assertTrue(firstNameValue.equalsIgnoreCase(genName));
        testWasSuccesful = (firstNameValue.equalsIgnoreCase(genName));
        assertTrue(lastNameValue.equalsIgnoreCase(genName));
        testWasSuccesful = (lastNameValue.equalsIgnoreCase(genName));

    }

    //@Test
    public void UK_MDM_06_VerifyTestNameForAllName() {
        testCaseName = "SCV-2676,MDM-06 Verify Dummy is nulled for Middle Name";

        CreateMessageForTest("AutomationXmls\\MDM06_AllTestName.xml");

        utilities.WaitForMessage();

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        String firstNameValue;
        String lastNameValue;
         List<String> middleNames = new ArrayList<>();
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        try {
            firstNameValue = jsonRecord.getJSONObject("customer").getString("firstName");
        } catch (JSONException e) {
            firstNameValue = null;

        }
        try {
            lastNameValue = jsonRecord.getJSONObject("customer").getString("lastName");
        } catch (JSONException e) {
            lastNameValue = null;
        }

        middleNames = utilities.GetExtraNames(jsonRecord);

        CheckTibcoSuccess();

                assertTrue(middleNames.isEmpty());

        assertNull(firstNameValue);
        assertNull(lastNameValue);

    }
    
    //@Test
    public void UK_MDM_11_UKPhoneNumberGreaterThan12Chars() {
        testCaseName = "SCV-XXX,MDM-11 Verify phone number is trimmed if it contains more than 12 chars ";

        //CreateMessageForTest("AutomationXmls\\MDM11_MoreThan12Chars.xml");

        //utilities.WaitForMessage();
        systemId = "20110000039";

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        //CheckTibcoSuccess();
        String jsonString = record.toJson();
        boolean ruleExecuted = false;
        boolean phoneNull = false;

        JSONObject jsonRecord = new JSONObject(jsonString);


        List<String> phoneNumbers = utilities.GetContactPoints(jsonRecord,eContactType.phoneNumber);
        
        if(phoneNumbers.isEmpty())
        {
            phoneNull = true;
        }
        else{assertTrue(phoneNull);
            testWasSuccesful = phoneNull;}
        
        Document droolsRecord = mongoConnector.getDroolsTrace(systemId);
        jsonString = droolsRecord.toJson();
        jsonRecord = new JSONObject(jsonString);
        List<String> rules = utilities.GetDroolsTraceMessage(jsonRecord);
        for (String rule : rules) {
            if (Pattern.compile(Pattern.quote("UK-TRANS-11"), Pattern.CASE_INSENSITIVE).matcher(rule).find())
            {
                ruleExecuted = true;
            }
        }
        assertTrue(phoneNull);
        assertTrue(ruleExecuted);
        testWasSuccesful = ruleExecuted;
    }
    
    //@Test
    public void UK_MDM_13_UKEmailCorrectDomain() {
        testCaseName = "SCV-XXX,MDM-13 Verify email domain is corrected from ukk to uk ";

        //CreateMessageForTest("AutomationXmls\\MDM13_EmailCorrectDomain_ukk.xml");

        //utilities.WaitForMessage();
        systemId = "20110000041";

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        //CheckTibcoSuccess();
        String jsonString = record.toJson();
        boolean ruleExecuted = false;
        boolean expectedEmail = false;

        JSONObject jsonRecord = new JSONObject(jsonString);


        List<String> emails = utilities.GetContactPoints(jsonRecord,eContactType.email);
        
        for (String email : emails) {
            if (emails.contains(".ukk")) {
                expectedEmail = false;
            }else expectedEmail = true;
        }
        
        Document droolsRecord = mongoConnector.getDroolsTrace(systemId);
        jsonString = droolsRecord.toJson();
        jsonRecord = new JSONObject(jsonString);
        List<String> rules = utilities.GetDroolsTraceMessage(jsonRecord);
        for (String rule : rules) {
            if (Pattern.compile(Pattern.quote("UK-TRANS-13"), Pattern.CASE_INSENSITIVE).matcher(rule).find())
            {
                ruleExecuted = true;
            }
        }
        assertTrue(expectedEmail);
        assertTrue(ruleExecuted);
        testWasSuccesful = ruleExecuted;
    }
    
    @Test
    public void UK_MDM_14_UKEmailCorrectDomain() {
        testCaseName = "SCV-XXX,MDM-14 Verify email domain is corrected from uuk to uk ";

        //CreateMessageForTest("AutomationXmls\\MDM13_EmailCorrectDomain_ukk.xml");

        //utilities.WaitForMessage();
        systemId = "20110000042";

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);

        //CheckTibcoSuccess();
        String jsonString = record.toJson();
        boolean ruleExecuted = false;
        boolean expectedEmail = false;

        JSONObject jsonRecord = new JSONObject(jsonString);


        List<String> emails = utilities.GetContactPoints(jsonRecord,eContactType.email);
        
        for (String email : emails) {
            if (emails.contains(".uuk")) {
                expectedEmail = false;
            }else expectedEmail = true;
        }
        
        Document droolsRecord = mongoConnector.getDroolsTrace(systemId);
        jsonString = droolsRecord.toJson();
        jsonRecord = new JSONObject(jsonString);
        List<String> rules = utilities.GetDroolsTraceMessage(jsonRecord);
        for (String rule : rules) {
            if (Pattern.compile(Pattern.quote("UK-TRANS-13"), Pattern.CASE_INSENSITIVE).matcher(rule).find())
            {
                ruleExecuted = true;
            }
        }
        assertTrue(expectedEmail);
        assertTrue(ruleExecuted);
        testWasSuccesful = ruleExecuted;
    }
}
