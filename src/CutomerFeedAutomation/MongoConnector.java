/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.bson.Document;

public class MongoConnector {

    private MongoCollection<Document> _collection = null;
    private MongoCollection<Document> _droolsCollection = null;
    private MongoCollection<Document> _refDataCollection = null;
    private Document _record = null;
    private Document _droolsTrace = null;
    private Document _refData = null;
    private String _userName;
    private String _password;
    private String _database;
    private String _uri;

    public MongoCollection<Document> getMongoConnection() {

        SetMDMProperties();
        CreateMongoConnection();

        return _collection;
    }

    private void CreateMongoConnection() {

        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Tools\\MongoCertificates\\mongoStore.ts");
            System.setProperty("javax.net.ssl.trustStorePassword", "StorePass");
            System.setProperty("javax.net.ssl.keyStore", "C:\\Tools\\MongoCertificates\\MongoClientKeyCert.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "StorePass");

            MongoClientURI uri = new MongoClientURI("mongodb://" + _userName + ":" + _password + _uri);
            //MongoCredential credential = MongoCredential.createCredential(user, database, password);

            //MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
            // ServerAddress address = new ServerAddress (uri, 27017);
            MongoClient mongoClient = new MongoClient(uri);

            MongoDatabase database = mongoClient.getDatabase(_database);

            _collection = database.getCollection("customer_UK");
            _droolsCollection = database.getCollection("droolsTrace");
            _refDataCollection = database.getCollection("refData");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public Document getMongoRecordByMasterId(String masterId) {

        String key = "customer.customerId.masterKey.id";

        FindRecord(key, masterId);

        return _record;
    }

    public Document getMongoRecordBySourceId(String sourceId) {

        String key = "customer.customerId.sourceKeys.keies.id";

        FindRecord(key, sourceId);

        return _record;
    }

    public Document getDroolsTrace(String sourceId) {
        String key = "droolsTrace.customerId.sourceKeys.keies.id";

        FindDroolsTrace(key, sourceId);

        return _droolsTrace;
    }
    
    public Document getReferenceData(String sourceId) {
        FindIterable<Document> findIterable = _refDataCollection.find(eq("key",sourceId));
        _refData = findIterable.first();
        return _refData;
    }

    private void FindRecord(String key, String id) {
        _collection.find(eq(key, id))
                .forEach(PrintBlock);
    }

    private void FindDroolsTrace(String key, String id) {
        _droolsCollection.find(eq(key, id))
                .forEach(PrintDrools);
    }

    Block<Document> PrintDrools = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            _droolsTrace = document;
        }
    };

    Block<Document> PrintBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            _record = document;
        }
    };

    private void SetMDMProperties() {
        Properties prop = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            prop.load(input);

            _userName = prop.getProperty("MDMUser");
            _password = prop.getProperty("MDMPassword");
            _uri = prop.getProperty("MDMURI");
            _database = prop.getProperty("MDMDatabase");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
