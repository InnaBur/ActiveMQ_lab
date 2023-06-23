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
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, JMSException {
        logger.debug("Start");
        MessageGenerator messageGenerator = new MessageGenerator();
        MyValidator myValidator = new MyValidator();
        ArrayList<MyMessage> recievedMessages = new ArrayList<>();
//        MyMessage message1 = messageGenerator.generateMessage();
//        Set<ConstraintViolation<MyMessage>> validateMessage1 = myValidator.validateMessage(message1);
//        String s = validateMessage1.stream()
//                .map(validateMessage2 -> validateMessage2.toString() ).collect(Collectors.joining(",", "[", "]"));
//        System.out.println(s);
//        System.out.println(validateMessage1);
//        System.exit(1);
//        Properties properties = new Properties();
        FileProcessing fileProcessing = new FileProcessing();
        fileProcessing.createCSVFiles();
        Properties properties = fileProcessing.loadProperties();
        Consumer consumer = new Consumer();


        ActiveMQConnectionFactory connectionFactory = Producer.createActiveMQConnectionFactory(properties);
        PooledConnectionFactory pooledConnectionFactory = Producer.createPooledConnectionFactory(connectionFactory);
        Producer.sendMessage(pooledConnectionFactory, messageGenerator, properties);
        recievedMessages = consumer.receiveMessage(connectionFactory, properties);
        pooledConnectionFactory.stop();

        for (MyMessage message : recievedMessages) {
            Set<ConstraintViolation<MyMessage>> validateMessage = myValidator.validateMessage(message);
            if (validateMessage.size() == 0) {
                fileProcessing.writeIntoFile("valid.csv", consumer.dataValid(message));
            } else {
                fileProcessing.writeIntoFile("error.csv", consumer.dataInvalid(message, validateMessage));
            }
        }


        logger.debug("Finish");

    }

    public String readOutputFormat() {
        String outputFormat = System.getProperty("N");
        if (outputFormat == null || Integer.parseInt(outputFormat) < 1000) {
            // delete before send !!!!!!!!
            outputFormat = "50";
//            logger.warn("Output format must be more then 1000");
        }

        return outputFormat;
    }
}
