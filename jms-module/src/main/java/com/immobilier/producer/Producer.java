package com.immobilier.producer;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE = "IMMOBILIER_QUEUE";

    public static void send(String text) {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection conn = null;
        Session session = null;
        try {
            conn = factory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue q = session.createQueue(QUEUE);
            MessageProducer producer = session.createProducer(q);
            TextMessage m = session.createTextMessage(text);
            producer.send(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) session.close();
                if (conn != null) conn.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
