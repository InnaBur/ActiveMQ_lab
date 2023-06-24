package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, JMSException {
        logger.debug("Start");

        MessageGenerator messageGenerator = new MessageGenerator();
        MyValidator myValidator = new MyValidator();
        FileProcessing fileProcessing = new FileProcessing();
        Consumer consumer = new Consumer();
        List<MyMessage> receivedMessages;

        fileProcessing.createCSVFiles();
        Properties properties = fileProcessing.loadProperties();

        ActiveMQConnectionFactory connectionFactory = Producer.createActiveMQConnectionFactory(properties);
        PooledConnectionFactory pooledConnectionFactory = Producer.createPooledConnectionFactory(connectionFactory);
        Producer.createProducerAndSendMessage(pooledConnectionFactory, messageGenerator, properties);
        receivedMessages = consumer.receiveMessage(connectionFactory, properties);
        pooledConnectionFactory.stop();

        fileProcessing.writeInFilesAfterValidation(receivedMessages, myValidator);

        logger.debug("Finish");
    }
}
