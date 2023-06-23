package com.thirdTask;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, JMSException {
        logger.debug("Start");
        MessageGenerator messageGenerator = new MessageGenerator();


//        Properties properties = new Properties();
        FileProcessing fileProcessing = new FileProcessing();
        fileProcessing.createCSVFiles();
        Properties properties = fileProcessing.loadProperties();
        Consumer consumer = new Consumer();

        logger.info(new App().readOutputFormat());
        logger.info(properties.getProperty("password"));



        ActiveMQConnectionFactory connectionFactory = Producer.createActiveMQConnectionFactory(properties);
        PooledConnectionFactory pooledConnectionFactory = Producer.createPooledConnectionFactory(connectionFactory);
        Producer.sendMessage(pooledConnectionFactory, messageGenerator, properties);
        consumer.receiveMessage(connectionFactory, properties);

        pooledConnectionFactory.stop();


        logger.debug("Finish");

    }

    public String readOutputFormat() {
        String outputFormat = System.getProperty("N");
        if (outputFormat == null || Integer.parseInt(outputFormat) < 1000) {
            // delete before send !!!!!!!!
            outputFormat = "100";
//            logger.warn("Output format must be more then 1000");
        }

        return outputFormat;
    }
}
