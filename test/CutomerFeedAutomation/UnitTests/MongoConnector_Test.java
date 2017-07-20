/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.UnitTests;

import CutomerFeedAutomation.MongoConnector;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONArray;
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
public class MongoConnector_Test {

    static MongoConnector mongoConnector;
    static MongoCollection<Document> _collection;

    public MongoConnector_Test() {
        mongoConnector = new MongoConnector();
        _collection = mongoConnector.getMongoConnection();
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
    public void TestMongoConnection() {

        MongoCollection<Document> collection = mongoConnector.getMongoConnection();
        assertNotNull(collection);
        assertTrue(collection.count() > 0);
    }

    @Test
    public void TestGetRecordByMasterKey() {

        Document record = mongoConnector.getMongoRecordByMasterId("AH0000000140");
        String keyString = "lastName";
        String valueString = "";
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        valueString = jsonRecord.getJSONObject("customer").getString(keyString);

        assertEquals(valueString, "Holdsworth");

    }

    @Test
    public void TestGetRecordBySourceKey_CDM() {

        Document record = mongoConnector.getMongoRecordBySourceId("AH0000000140");
        String keyString = "lastName";
        String valueString = "";
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        valueString = jsonRecord.getJSONObject("customer").getString(keyString);

        assertEquals(valueString, "Holdsworth");

    }

    @Test
    public void TestGetRecordBySourceKey_MDM() {

        Document record = mongoConnector.getMongoRecordBySourceId("594d1971e4b06a7a8a3f1f32");
        String keyString = "lastName";
        String valueString = "";
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        valueString = jsonRecord.getJSONObject("customer").getString(keyString);

        assertEquals(valueString, "Holdsworth");

    }

    @Test
    public void TestGetRecordBySourceKey_C4C() {

        Document record = mongoConnector.getMongoRecordBySourceId("3495211");
        String keyString = "lastName";
        String valueString = "";
        String jsonString = record.toJson();

        JSONObject jsonRecord = new JSONObject(jsonString);

        valueString = jsonRecord.getJSONObject("customer").getString(keyString);

        assertEquals(valueString, "Holdsworth");
    }

    public void getContactPoints() {
        Document record = mongoConnector.getMongoRecordBySourceId("3495211");
        String jsonString = record.toJson();
        String phoneNumber = "";

        JSONObject jsonRecord = new JSONObject(jsonString);


        JSONObject contactPointsObj = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints");

        JSONArray contactPoints = contactPointsObj.getJSONArray("contactPoints");

        for (Object o : contactPoints) {
            if (o instanceof JSONObject) {
                JSONObject contactPoint = (JSONObject) o;

                phoneNumber = contactPoint.getJSONObject("contactPhoneNumber").getJSONObject("unStructuredPhoneNumber").getString("phoneNumber");

            }
        }

        assertEquals(phoneNumber, "++4477144487873");
    }
}
