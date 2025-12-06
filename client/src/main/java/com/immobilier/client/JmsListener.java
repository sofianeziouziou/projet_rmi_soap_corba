package com.immobilier.client;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsListener {
    private final String broker = "tcp://localhost:61616";

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(broker);
            Connection conn = factory.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination q = session.createQueue("IMMOBILIER_QUEUE");
            MessageConsumer consumer = session.createConsumer(q);
            consumer.setMessageListener(msg -> {
                if (msg instanceof TextMessage) {
                    try { System.out.println("[JMS] " + ((TextMessage) msg).getText()); }
                    catch(Exception e){ e.printStackTrace(); }
                }
            });
        } catch(Exception e){ e.printStackTrace(); }
    }
}
