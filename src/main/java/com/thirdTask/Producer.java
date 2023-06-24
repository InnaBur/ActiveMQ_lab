package com.thirdTask;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.time.LocalTime;
import java.util.Properties;

public class Producer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);


    protected static void createProducerAndSendMessage(PooledConnectionFactory pooledConnectionFactory,
                                                       MessageGenerator messageGenerator,
                                                       Properties properties) throws JMSException{

        Connection producerConnection = pooledConnectionFactory.createConnection();
        producerConnection.start();
        logger.debug("Connection started");
        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = createProducer(properties, producerSession);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        sendMessage(producerSession, producer, messageGenerator, properties);
        closeSession(producer, producerSession, producerConnection);

    }

    private  static void sendMessage(Session producerSession, MessageProducer producer,
                                     MessageGenerator messageGenerator, Properties properties) throws JMSException {

        DataProcessing dataProcessing = new DataProcessing();
        LocalTime start = LocalTime.now();
        int numberOfMessages = Integer.parseInt(dataProcessing.readOutputFormat());

        long poisonPill = Long.parseLong(properties.getProperty("poisonPill"));

        LocalTime endTime = start.plusSeconds(poisonPill);
        int count = 0;
        logger.debug("Time start {}", LocalTime.now());
        logger.debug("Time end {} ", endTime);

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
        logger.info("Messages sent");
        logger.debug("Sent {} messages", count);
    }

    private static void sendMessageToQueue(Session producerSession, MessageProducer producer, MessageGenerator messageGenerator) throws JMSException {
        ObjectMessage producerMessage;
        MyMessage messages = messageGenerator.generateMessage();
        producerMessage = producerSession.createObjectMessage(messages);
        producer.send(producerMessage);
    }

    private static boolean isNotPoisonPill(LocalTime endTime) {
        return LocalTime.now().isBefore(endTime);
    }

    private static MessageProducer createProducer(Properties properties, Session producerSession) throws JMSException {
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
