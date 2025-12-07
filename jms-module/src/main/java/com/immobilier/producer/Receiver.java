package com.immobilier.producer;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class Receiver {
    public static void main(String[] args){
        try{
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection conn = factory.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("immobilier.notifications");
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(message -> {
                try{
                    if(message instanceof TextMessage){
                        String text = ((TextMessage) message).getText();
                        System.out.println("Notification JMS: "+text);
                        // Ici tu peux aussi envoyer vers un servlet SSE pour le front
                    }
                }catch(JMSException e){ e.printStackTrace(); }
            });
        }catch(Exception e){ e.printStackTrace(); }
    }
}
