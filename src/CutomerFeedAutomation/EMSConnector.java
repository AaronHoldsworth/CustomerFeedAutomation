/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation;

import java.util.HashMap;
import java.util.Map;
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

    static String serverUrl = "tcp://10.147.14.31:7222";
    static String user = "tester";
    static String password = "tester123";
    private QueueConnection connection = null;
    private Session session = null;
    private QueueConnectionFactory factory = null;
    private MessageProducer sender = null;
    private Queue queue = null;

    public boolean ConnectToGIP(String queueName) {

        boolean connectionSuccessful = true;
        try {
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
}
