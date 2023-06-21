package com.thirdTask;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.Properties;

public class Producer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    protected static void sendMessage(PooledConnectionFactory pooledConnectionFactory, MessageGenerator messageGenerator, Properties properties) throws JMSException, IOException {

        Connection producerConnection = pooledConnectionFactory.createConnection();
        producerConnection.start();
        logger.debug("Connection started");
        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination producerDestination = producerSession.createQueue(properties.getProperty("nameQueue"));
        logger.debug("Queue was created");

        MessageProducer producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        int numberOfMessages = Integer.parseInt(new App().readOutputFormat());
        TextMessage producerMessage;
        for (int i = 0; i < numberOfMessages; i++) {
            String messages = messageGenerator.generateMessages().toString();
            producerMessage = producerSession.createTextMessage(messages);
            producer.send(producerMessage);
        }
        logger.info("Messages sent");
        closeSession(producer, producerSession, producerConnection);

    }

    private static void closeSession(MessageProducer producer, Session producerSession, Connection producerConnection) throws JMSException {
        producer.close();
        producerSession.close();
        producerConnection.close();
        logger.info("Connection closed");
    }


}
