package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Consumer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    protected List<MyMessage> receiveMessage(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException {

        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);
        int count = 0;
        List<MyMessage> messagesList = new ArrayList<>();

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

        logger.info("Received {} messages", count);
        closeSession(consumer, consumerSession, consumerConnection);
        return messagesList;
    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
