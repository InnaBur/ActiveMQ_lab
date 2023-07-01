package com.thirdTask;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String FILEPATH_VALID = "valid.csv";

    private static final String FILEPATH_ERROR = "error.csv";
    private static final BlockingQueue<MyMessage> blockingQueue = new LinkedBlockingQueue<>();


    public static void main(String[] args) throws IOException, JMSException, InterruptedException {
        logger.debug("Start");
        Consumer consumer = new Consumer(blockingQueue);
        Producer producer = new Producer();

        MessageGenerator messageGenerator = new MessageGenerator();
        FileProcessing fileProcessing = new FileProcessing(blockingQueue);

        fileProcessing.createCSVFiles();

        producer.run();

        Thread consum = new Thread(consumer);
        consum.setPriority(Thread.MAX_PRIORITY);
        Thread writerThread = new Thread(fileProcessing);


        consum.start();
        writerThread.start();

        consum.join();
        writerThread.join();

        fileProcessing.countMessages(FILEPATH_VALID);
        fileProcessing.countMessages(FILEPATH_ERROR);
        logger.debug("Finish");
    }


}
