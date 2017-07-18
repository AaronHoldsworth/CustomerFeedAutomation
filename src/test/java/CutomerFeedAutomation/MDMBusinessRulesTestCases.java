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

    public void SaveIdToProp() {
                try {

            prop.setProperty("LatestId", Integer.toString(latestIdNumber));

            prop.store(new FileOutputStream("config.properties"), null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    int latestIdNumber;
    static int lastIdNumber;

    public MDMBusinessRulesTestCases() {
        utilities = new TestUtilities();
        emsConnector = new EMSConnector();
        mongoConnector = new MongoConnector();
        mongoConnector.getMongoConnection();

        String queueName = "TUI.CP.MDM.DEV.CUSTOMER.0300.CUSTOMERSOURCEEVENT.UK.Q.ACTION";
        emsConnector.ConnectToGIP(queueName);

        prop = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            prop.load(input);

            latestIdNumber = Integer.parseInt(prop.getProperty("LatestId"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        SaveIdToProp();
    }

    @Test
    public void UK_MDM_01_RecordWithAllMandatoryElements() {
        String file = utilities.LoadTestFile("C:\\AutomationXmls\\MDM01_AllMandatoryElements.xml");
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        HashMap<String, String> properties = utilities.CreatePropertiesHashMap(propertyList);
        String messageBody = splitFile[1].trim();
        String systemId = "AUTO" + Integer.toString(latestIdNumber);
        messageBody = messageBody.replaceAll(":systemid:", systemId);
        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        assertNotNull(record);
        latestIdNumber++;

    }

    @Test
    public void UK_MDM_01_RecordMissingTitle() {
        String file = utilities.LoadTestFile("C:\\AutomationXmls\\MDM01_MissingTitle.xml");
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        HashMap<String, String> properties = utilities.CreatePropertiesHashMap(propertyList);
        String messageBody = splitFile[1].trim();
        String systemId = "AUTO" + Integer.toString(latestIdNumber);
        messageBody = messageBody.replaceAll(":systemid:", systemId);
        emsConnector.SendEmsMessageToC4C(properties, messageBody);

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Document record = mongoConnector.getMongoRecordByMasterId(systemId);
        assertNull(record);
        latestIdNumber++;
    }
}
