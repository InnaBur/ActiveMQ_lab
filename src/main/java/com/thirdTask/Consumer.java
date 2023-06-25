package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.time.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


public class Consumer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
FileProcessing fileProcessing = new FileProcessing();

    public Consumer() throws IOException {
    }

    protected List<MyMessage> receiveMessage(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException, IOException {
MyValidator myValidator = new MyValidator();
        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
        int count = 0;
        List<MyMessage> messagesList = new LinkedList<>();
        LocalTime start = LocalTime.now();
        while (true) {
            Message consumerMessage = consumer.receive(2000);
            if (consumerMessage instanceof ObjectMessage) {
                ObjectMessage consumerTextMessage = (ObjectMessage) consumerMessage;
                MyMessage myMessage = (MyMessage) consumerTextMessage.getObject();
                messagesList.add(myMessage);

                count++;
            } else {
                break;
            }

        }
        LocalTime end = LocalTime.now();
        logger.info("Received {} messages", count);
        double milisec = Duration.between(start,end).toMillis();
        double mesInMillisec = count/ milisec;
//        double sec = duration.toMillis()/messagesList.size() ;

        logger.info("Speed, messages in millisecond {}", mesInMillisec);
        closeSession(consumer, consumerSession, consumerConnection);
        return messagesList;


    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
