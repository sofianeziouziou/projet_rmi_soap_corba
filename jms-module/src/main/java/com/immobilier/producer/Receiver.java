package com.immobilier.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE = "immobilier.notifications";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection conn = factory.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE);
            MessageConsumer consumer = session.createConsumer(queue);

            System.out.println("🔔 JMS Receiver started, waiting messages...");
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        System.out.println("🔔 JMS: " + ((TextMessage) message).getText());
                    }
                } catch (JMSException e) { e.printStackTrace(); }
            });
            // do not close — waits indefinitely
        } catch (Exception e) {
            System.err.println("❌ JMS receiver error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
