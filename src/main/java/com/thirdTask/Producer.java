package com.thirdTask;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Producer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    protected static void sendMessage(PooledConnectionFactory pooledConnectionFactory, MessageGenerator messageGenerator, Properties properties) throws JMSException, IOException {

        LocalTime start = LocalTime.now();
        Connection producerConnection = pooledConnectionFactory.createConnection();
        producerConnection.start();
        logger.debug("Connection started");
        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//        Destination producerDestination = producerSession.createQueue(properties.getProperty("nameQueue"));
//        logger.debug("Queue was created");

        MessageProducer producer = createProducer(producerConnection, properties, producerSession);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        int numberOfMessages = Integer.parseInt(new App().readOutputFormat());

        long poisonPill = Long.parseLong(properties.getProperty("poisonPill"));
        System.out.println("POISON!" + poisonPill);
        LocalTime endTime = start.plusSeconds(poisonPill);
        int count = 0;
        System.out.println("TIME " + LocalTime.now());
        System.out.println("TIME END " + endTime);
        for (int i = 0; i < numberOfMessages; i++) {

            while (isNotPoisonPill(endTime)) {
                sendMessageToQueue(producerSession, producer, messageGenerator);
                count++;
                if (numberOfMessages >= count) {
                    break;
                }

            }
            if (!isNotPoisonPill(endTime)) {
                break;
            }
        }
        logger.info("PoisonPill worked");
        System.out.println("TIME2 " + LocalTime.now());
        logger.info("{}", count);
        logger.info("Messages sent");
        closeSession(producer, producerSession, producerConnection);

    }

    private static void sendMessageToQueue(Session producerSession, MessageProducer producer, MessageGenerator messageGenerator) throws JMSException {
        ObjectMessage producerMessage;
        MyMessage messages = messageGenerator.generateMessages();
        producerMessage = producerSession.createObjectMessage(messages);
        producer.send(producerMessage);


    }

    private static boolean isNotPoisonPill(LocalTime endTime) {
        return LocalTime.now().isBefore(endTime);
    }

    private static MessageProducer createProducer(Connection producerConnection, Properties properties, Session producerSession) throws JMSException {


        Destination producerDestination = producerSession.createQueue(properties.getProperty("nameQueue"));
        logger.debug("Queue was created");

        return producerSession.createProducer(producerDestination);
    }

    private static void closeSession(MessageProducer producer, Session producerSession, Connection producerConnection) throws JMSException {
        producer.close();
        producerSession.close();
        producerConnection.close();
        logger.info("Connection closed");
    }


}
