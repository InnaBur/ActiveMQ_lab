package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.time.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class Consumer extends ConnectionProcessing implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    BlockingQueue<MyMessage> blockingQueue;

    public Consumer(BlockingQueue<MyMessage> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public Consumer() {
    }

    @Override
    public void run() {
        try {
            createConsumerAndReceiveMessages();
        } catch (JMSException e) {
            logger.error("JMSException occurred in {}", this.getClass(), e);
        } catch (IOException e) {
            logger.error("IOException occurred in {}", this.getClass(), e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred in {}", this.getClass(), e);
            Thread.currentThread().interrupt();
        }
    }

    protected void createConsumerAndReceiveMessages() throws JMSException, IOException, InterruptedException {
        Properties properties = new FileProcessing().loadProperties();
        ActiveMQConnectionFactory connectionFactory = createActiveMQConnectionFactory(properties);
        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
        receiveMessages(consumer);

        closeSession(consumer, consumerSession, consumerConnection);
    }

    protected void receiveMessages(MessageConsumer consumer) {
        try {
            LocalTime start = LocalTime.now();
            int count = countMessages(consumer);
            double speed = countTime(start, count);
            logger.info("Speed, messages in second {}", speed);
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred in {}", this.getClass(), e);
            Thread.currentThread().interrupt();
        } catch (JMSException e) {
            logger.error("JMSException occurred in {}", this.getClass(), e);
        }
    }

    protected int countMessages(MessageConsumer consumer) throws JMSException, InterruptedException {
        int count = 0;
        boolean receiveMessages = true;
        while (receiveMessages) {

            Message consumerMessage = consumer.receive(1000);
            if (consumerMessage == null) {
                receiveMessages = false;
            } else {
                ObjectMessage consumerObjectMessage = (ObjectMessage) consumerMessage;
                MyMessage myMessage = (MyMessage) consumerObjectMessage.getObject();
//                if (isPoisonPill(myMessage)) {
//                    logger.info("PoisonPill received");
//                    blockingQueue.put(myMessage);
//                    receiveMessages = false;
//                }
                blockingQueue.put(myMessage);
                count++;
            }
        }
        return count;
    }

    protected double countTime(LocalTime start, int count) {
        LocalTime end = LocalTime.now();
        logger.info("Received {} messages", count);
        double seconds = Duration.between(start, end).toMillis() / 1000.0;
        return count / seconds;
    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }

    protected boolean isPoisonPill(MyMessage message) {
        return message.getName().equals("PoisonPill");
    }

}
