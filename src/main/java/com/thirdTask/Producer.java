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

        Destination producerDestination = producerSession.createQueue(properties.getProperty("nameQueue"));
        logger.debug("Queue was created");

        MessageProducer producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        int numberOfMessages = Integer.parseInt(new App().readOutputFormat());
        TextMessage producerMessage;
        long poisonPill = Long.parseLong(properties.getProperty("poisonPill"));
        System.out.println("POISON!" + poisonPill);
        LocalTime endTime = start.plusSeconds(poisonPill);
        int count = 0;
        System.out.println("TIME " + LocalTime.now());
        System.out.println("TIME END " + endTime);
        for (int i = 0; i < numberOfMessages; i++) {
            while (LocalTime.now().isBefore(endTime)) {

                String messages = messageGenerator.generateMessages().toString();
                producerMessage = producerSession.createTextMessage(messages);
                producer.send(producerMessage);
                count++;
                if (numberOfMessages >= count) {
                    break;
                }

            }
            if (!LocalTime.now().isBefore(endTime)) {
                break;
            }
        }
        logger.info("PoisonPill worked");
        System.out.println("TIME2 " + LocalTime.now());
        logger.info("{}", count);
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
