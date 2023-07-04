package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class Producer extends ConnectionProcessing implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public Producer() throws IOException {
    }

    @Override
    public void run() {
        try {
            createProducerAndSendMessage();
        } catch (JMSException e) {
            logger.error("JMSException occurred in {}", this.getClass(), e);
        } catch (IOException e) {
            logger.error("IOException occurred in {}", this.getClass(), e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred in {}", this.getClass(), e);
            Thread.currentThread().interrupt();
        }
    }

    protected void createProducerAndSendMessage() throws JMSException, IOException, InterruptedException {
        MessageGenerator messageGenerator = new MessageGenerator();
        FileProcessing fileProcessing = new FileProcessing();
        Properties properties = fileProcessing.loadProperties();
        ActiveMQConnectionFactory activeMQConnectionFactory = createActiveMQConnectionFactory(properties);
        PooledConnectionFactory pooledConnectionFactory = createPooledConnectionFactory(activeMQConnectionFactory);
        Connection producerConnection = pooledConnectionFactory.createConnection();
        producerConnection.start();
        logger.debug("Connection started");

        Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = createProducer(properties, producerSession);


        sendMessage(producerSession, producer, messageGenerator, properties);
        pooledConnectionFactory.clear();
        closeSession(producer, producerSession, producerConnection);

    }

    protected void sendMessage(Session producerSession, MessageProducer producer,
                                      MessageGenerator messageGenerator, Properties properties) throws JMSException, InterruptedException {
        BlockingQueue<MyMessage> blockingQueueProducer = messageGenerator.generateMessage();

        long poisonPill = Long.parseLong(properties.getProperty("poisonPill"));
        logger.debug("PoisonPill is {}", poisonPill);

        LocalTime start = LocalTime.now();
        LocalTime estimatedEndTime = start.plusSeconds(poisonPill);
        int count = 0;
        logger.debug("Time start {}", LocalTime.now());
        logger.debug("Estimated time end {} ", estimatedEndTime);

        while (isNotTimePoisonPill(estimatedEndTime) && !blockingQueueProducer.isEmpty()) {
            if (!isNotTimePoisonPill(estimatedEndTime) || blockingQueueProducer.isEmpty()) {
                break;
            }
            count = sendMessageToQueue(count, blockingQueueProducer, producerSession, producer);
        }
        count = sendPoisonPill(count, producerSession, producer);
        logger.info("PoisonPill has been sent");

        LocalTime end = LocalTime.now();
        if (!isNotTimePoisonPill(estimatedEndTime)) {
            logger.info("PoisonPill worked");
        }

        logger.info("Messages sent at {}, for {} milliseconds", LocalTime.now(), Duration.between(start, end).toMillis());
        logger.debug("Sent {} messages", count);
    }

    protected int sendPoisonPill(int count, Session producerSession, MessageProducer producer) throws JMSException {
        MyMessage poison = new MyMessage();
        poison.setName("PoisonPill");
        ObjectMessage poisonPillMessage = producerSession.createObjectMessage(poison);
        producer.send(poisonPillMessage);
        count++;
        return count;
    }

    protected int sendMessageToQueue(int count, BlockingQueue<MyMessage> blockingQueueProduser, Session producerSession, MessageProducer producer) throws InterruptedException, JMSException {
        ObjectMessage producerMessage;
        MyMessage message = blockingQueueProduser.take();
        producerMessage = producerSession.createObjectMessage(message);
        producer.send(producerMessage);
        count++;
        return count;
    }

    protected boolean isNotTimePoisonPill(LocalTime endTime) {
        return LocalTime.now().isBefore(endTime);
    }

    protected static MessageProducer createProducer(Properties properties, Session producerSession) throws JMSException {
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
