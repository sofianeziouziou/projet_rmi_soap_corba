package com.immobilier.producer;


import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("IMMOBILIER_QUEUE");

        MessageConsumer consumer = session.createConsumer(queue);

        System.out.println("📥 Attente de messages JMS...");

        while (true) {
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                System.out.println("📩 Reçu : " + ((TextMessage) msg).getText());
            }
        }
    }
}
