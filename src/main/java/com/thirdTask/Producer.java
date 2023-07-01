package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Producer extends ConnectionProcessing implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);


    public Producer() throws IOException {
    }


//    public Producer(BlockingQueue<MyMessage> blockingQueue) {
//        this.blockingQueue = blockingQueue;
//    }

    @Override
    public void run() {
        try {
            createProducerAndSendMessage();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void createProducerAndSendMessage() throws JMSException, IOException, InterruptedException {
        MessageGenerator messageGenerator = new MessageGenerator();
        FileProcessing fileProcessing = new FileProcessing();
        Properties properties = fileProcessing.loadProperties();
        ActiveMQConnectionFactory activeMQConnectionFactory = createActiveMQConnectionFactory(properties);
//        PooledConnectionFactory pooledConnectionFactory = createPooledConnectionFactory()
        Connection producerConnection = activeMQConnectionFactory.createConnection();
        producerConnection.start();
        logger.debug("Connection started");
        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = createProducer(properties, producerSession);


        sendMessage(producerSession, producer, messageGenerator, properties);
        closeSession(producer, producerSession, producerConnection);

    }

    protected static void sendMessage(Session producerSession, MessageProducer producer,
                                      MessageGenerator messageGenerator, Properties properties) throws JMSException, InterruptedException {
        BlockingQueue<MyMessage> blockingQueueProduser = messageGenerator.generateMessage();
        DataProcessing dataProcessing = new DataProcessing();

        int numberOfMessages = Integer.parseInt(dataProcessing.readOutputFormat());
        long poisonPill = Long.parseLong(properties.getProperty("poisonPill"));

        LocalTime start = LocalTime.now();
        LocalTime estimatedEndTime = start.plusSeconds(poisonPill);
        int count = 0;
        logger.debug("Time start {}", LocalTime.now());
        logger.debug("Estimated time end {} ", estimatedEndTime);


//        for (int i = 0; i < numberOfMessages; i++) {
        ObjectMessage producerMessage;
        while (isNotPoisonPill(estimatedEndTime) && !blockingQueueProduser.isEmpty()) {
            //sendMessageToQueue(producerSession, producer, messageGenerator);
            if (!isNotPoisonPill(estimatedEndTime) || blockingQueueProduser.isEmpty()) {
                break;
            }
            MyMessage message = blockingQueueProduser.take();
            producerMessage = producerSession.createObjectMessage(message);
            producer.send(producerMessage);
            count++;
//                if (numberOfMessages >= count) {
//                    break;
//                }
        }

//        if (!isNotPoisonPill(estimatedEndTime)) {
            logger.info("PoisonPill worked");
            MyMessage poison = new MyMessage();
            poison.setName("PoisonPill");
            ObjectMessage poisonPillMessage = producerSession.createObjectMessage(poison);
            producer.send(poisonPillMessage);
//        }
//        }
        logger.info("Messages sent");
        logger.info("Messages sent for {} sec", LocalTime.now());
        logger.debug("Sent {} messages", count);

    }

//    private static void sendMessageToQueue(Session producerSession, MessageProducer producer, MessageGenerator messageGenerator) throws JMSException, InterruptedException {
//        ObjectMessage producerMessage;
//
//        MyMessage message = blockingQueueProduser.take();
//        producerMessage = producerSession.createObjectMessage(message);
//        producer.send(producerMessage);
//    }


    private static boolean isNotPoisonPill(LocalTime endTime) {
        return LocalTime.now().isBefore(endTime);
    }

    private static MessageProducer createProducer(Properties properties, Session producerSession) throws JMSException {
        Destination producerDestination = producerSession.createQueue(properties.getProperty("nameQueue"));
        logger.debug("Queue was created");
        MessageProducer producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return producer;
    }

    private static void closeSession(MessageProducer producer, Session producerSession, Connection producerConnection) throws JMSException {
        producer.close();
        producerSession.close();
        producerConnection.close();
        logger.info("Connection closed");
    }


}
