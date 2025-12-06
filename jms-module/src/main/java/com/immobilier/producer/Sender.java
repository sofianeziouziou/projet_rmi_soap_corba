package com.immobilier.producer;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
    public static void main(String[] args) throws Exception {

        // Connexion au broker ActiveMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("IMMOBILIER_QUEUE");

        MessageProducer producer = session.createProducer(queue);

        TextMessage msg = session.createTextMessage("Nouveau contrat signé !");
        producer.send(msg);

        System.out.println("📨 Message envoyé via JMS !");

        session.close();
        connection.close();
    }
}
