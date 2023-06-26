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
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    protected void createConsumerAndReceiveMessages(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException, IOException, InterruptedException {

        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
        receiveMessages(consumer);

        closeSession(consumer, consumerSession, consumerConnection);
    }

    private void receiveMessages(MessageConsumer consumer) throws InterruptedException, JMSException, IOException {
        MyValidator myValidator = new MyValidator();
        FileProcessing fileProcessing = new FileProcessing();
        BlockingQueue<MyMessage> blockingQueue = new LinkedBlockingQueue<>();
        int count = 0;
        LocalTime start = LocalTime.now();
        while (true) {
            Message consumerMessage = consumer.receive(2000);
            if (consumerMessage instanceof ObjectMessage) {
                ObjectMessage consumerTextMessage = (ObjectMessage) consumerMessage;
                MyMessage myMessage = (MyMessage) consumerTextMessage.getObject();
                blockingQueue.put(myMessage);
                fileProcessing.writeInFilesAfterValidation(blockingQueue.take(), myValidator);
                count++;
            } else {
                break;
            }
        }
        double speed = countTime(start, count);
        logger.info("Speed, messages in millisecond {}", speed);
    }

    protected double countTime(LocalTime start, int count) {
        LocalTime end = LocalTime.now();
        logger.info("Received {} messages", count);
        double millis = Duration.between(start, end).toMillis();
        return count / millis;
    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }
}
