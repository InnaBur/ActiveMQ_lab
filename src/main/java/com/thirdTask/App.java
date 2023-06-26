package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Properties;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String FILEPATH_VALID = "valid.csv";
    private static final String FILEPATH_ERROR = "error.csv";
    public static void main(String[] args) throws IOException, JMSException, InterruptedException {
        logger.debug("Start");

        Consumer consumer = new Consumer();

        MessageGenerator messageGenerator = new MessageGenerator();
        FileProcessing fileProcessing = new FileProcessing();

        fileProcessing.createCSVFiles();
        Properties properties = fileProcessing.loadProperties();


        ActiveMQConnectionFactory connectionFactory = Producer.createActiveMQConnectionFactory(properties);
        PooledConnectionFactory pooledConnectionFactory = Producer.createPooledConnectionFactory(connectionFactory);
        Producer.createProducerAndSendMessage(pooledConnectionFactory, messageGenerator, properties);
        consumer.createConsumerAndReceiveMessages(connectionFactory, properties);

        pooledConnectionFactory.stop();

        fileProcessing.countMessages(FILEPATH_VALID);
        fileProcessing.countMessages(FILEPATH_ERROR);
        logger.debug("Finish");
    }


}
