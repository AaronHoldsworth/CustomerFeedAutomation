/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author TTGAHX
 */
public class EMSConnector {

    static private String serverUrl;
    static private String user;
    static private String password;
    private QueueConnection connection = null;
    private Session session = null;
    private QueueConnectionFactory factory = null;
    private MessageProducer sender = null;
    private Queue queue = null;
    static private String queueName;

    public boolean ConnectToGIP() {

        boolean connectionSuccessful = true;
        try {
            SetEMSProperties();
            
            factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(serverUrl);

            connection = factory.createQueueConnection(user, password);

            session = connection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            queue = session.createQueue(queueName);

            sender = session.createProducer(queue);

            connection.start();

        } catch (JMSException e) {
            System.out.println(e.getMessage());
            connectionSuccessful = false;
        }

        return connectionSuccessful;
    }

    public boolean SendEmsMessageToC4C(HashMap<String, String> properties, String messageBody) {

        boolean messageSent = true;

        try {

            TextMessage message = session.createTextMessage();

            message.setJMSMessageID("23BA2881");
            message.setJMSTimestamp(System.currentTimeMillis());
            message.setJMSDestination(queue);
            message.setJMSPriority(4);

            for (Map.Entry<String, String> property : properties.entrySet()) {
                message.setStringProperty(property.getKey(), property.getValue());
            }
            message.setText(messageBody);
            sender.send(message);

            connection.close();
        } catch (JMSException | NullPointerException e) {
            System.out.println(e.getMessage());

            messageSent = false;
        }

        return messageSent;
    }
    
        private void SetEMSProperties() {
        Properties prop = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            prop.load(input);

            serverUrl = prop.getProperty("EMSServerUrl");
            password = prop.getProperty("EMSPassword");
            user = prop.getProperty("EMSUser");
            queueName = prop.getProperty("EMSQueue");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
