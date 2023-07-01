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

public class Consumer extends ConnectionProcessing implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    BlockingQueue<MyMessage> blockingQueue;
    private int count = 0;

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
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    protected void receiveMessages(MessageConsumer consumer) throws InterruptedException, JMSException, IOException {

//        Thread consum = new Thread();
//        consum.start();
//        consum.join();
        LocalTime start = LocalTime.now();
        while (true) {

            Message consumerMessage = consumer.receive(1000);

            if (consumerMessage != null) {
//                if (((MyMessage) consumerMessage).getName().equals("PoisonPill")) {
//                    break;
//                }
                count = receiveOneMessage(consumerMessage, count);
            } else {
                break;
            }
        }
        double speed = countTime(start, count);
        logger.info("Speed, messages in second {}", speed);
    }

    protected int receiveOneMessage(Message consumerMessage, int count) throws IOException, JMSException, InterruptedException {
        MyValidator myValidator = new MyValidator();
        FileProcessing fileProcessing = new FileProcessing();
//        BlockingQueue<MyMessage> blockingQueue = new LinkedBlockingQueue<>();

        ObjectMessage consumerObjectMessage = (ObjectMessage) consumerMessage;
        MyMessage myMessage = (MyMessage) consumerObjectMessage.getObject();
        blockingQueue.put(myMessage);
//        fileProcessing.writeInFilesAfterValidation(blockingQueue.take(), myValidator);
        count++;
        return count;
    }

    protected double countTime(LocalTime start, int count) {
        LocalTime end = LocalTime.now();
        logger.info("Received {} messages", count);
        double seconds = Duration.between(start, end).toSeconds();
        return count / seconds;
    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
