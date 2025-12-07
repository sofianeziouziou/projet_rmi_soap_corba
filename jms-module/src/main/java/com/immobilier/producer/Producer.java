package com.immobilier.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE = "immobilier.notifications";

    public static void sendNotification(String text) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection conn = factory.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE);
            MessageProducer producer = session.createProducer(queue);
            TextMessage msg = session.createTextMessage(text);
            producer.send(msg);
            producer.close();
            session.close();
            conn.close();
            System.out.println("✅ JMS notification sent: " + text);
        } catch (Exception e) {
            System.err.println("❌ JMS send error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
