package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.Properties;

public class Consumer extends ConnectionProcessing {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    protected static void receiveMessage(ActiveMQConnectionFactory connectionFactory, Properties properties) throws JMSException, IOException {
        FileProcessing fileProcessing = new FileProcessing();
        Connection consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();
        Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination consumerDestination = consumerSession.createQueue(properties.getProperty("nameQueue"));

        // Create a message consumer from the session to the queue.
        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);

        // Begin to wait for messages.
        while (true) {
            final Message consumerMessage = consumer.receive(1000);
            if (consumerMessage instanceof TextMessage) {
                // Get the POJO from the ObjectMessage.
                TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            } else {
                break;
            }
        }
        logger.info("Messages received");
        closeSession(consumer, consumerSession, consumerConnection);

    }

    private static void closeSession(MessageConsumer consumer, Session consumerSession, Connection consumerConnection) throws JMSException {
        consumer.close();
        consumerSession.close();
        consumerConnection.close();
    }


}
