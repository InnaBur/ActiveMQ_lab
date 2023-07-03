package com.thirdTask;

import com.opencsv.CSVWriter;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class FileProcessing implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessing.class);
    private static final String FILE_NAME = "config.properties";
    private static final String[] HEADER_VALID = {"Name", "Count"};
    private static final String[] HEADER_ERROR = {"Name", "Count", "Error"};
    private static final String FILEPATH_VALID = "valid.csv";
    private static final String FILEPATH_ERROR = "error.csv";
    private static final int UNCOUNTED_FIELD = 1;
    DataProcessing dataProcessing = new DataProcessing();
    BlockingQueue<MyMessage> blockingQueue;

    public FileProcessing() throws IOException {
    }

    public FileProcessing(BlockingQueue<MyMessage> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (!blockingQueue.isEmpty()) {
                Queue<MyMessage> list = new LinkedList<>();
                blockingQueue.drainTo(list);
                writeInFilesAfterValidation(list);
            }
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred in {}", this.getClass(), e);
            Thread.currentThread().interrupt();
        }
    }

    public void createCSVFiles() {
        createCSV(FILEPATH_VALID, HEADER_VALID);
        createCSV(FILEPATH_ERROR, HEADER_ERROR);
    }

    public Properties loadProperties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(FILE_NAME)) {

            assert inputStream != null;
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(reader);
            logger.debug("Properties were loaded");
        } catch (IOException e) {
            logger.error("Properties were not loaded ");
        }
        return properties;
    }

    public boolean createCSV(String filePath, String[] header) {
        File file = new File(filePath);
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);
            writer.writeNext(header);
            logger.info("File {} created", filePath);
            writer.close();
            return true;
        } catch (IOException e) {
            logger.error("File was not created!");
            return false;
        }
    }

    public void writeIntoFile(String filePath, String[] data) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(filePath, true));
        writer.writeNext(data);
        writer.close();
    }

    protected void writeInFilesAfterValidation(Queue<MyMessage> list) {
        MyValidator myValidator = new MyValidator();
        while (!list.isEmpty()) {
            MyMessage message = list.poll();
            Set<ConstraintViolation<MyMessage>> validateMessage = myValidator.validateMessage(message);

            try {
                if (validateMessage.isEmpty()) {
                    writeIntoFile(FILEPATH_VALID, dataProcessing.dataValid(message));
                } else if (new Consumer().isPoisonPill(message)) {
                    break;
                } else {
                    writeIntoFile(FILEPATH_ERROR, dataProcessing.dataInvalid(message, validateMessage));
                }
            } catch (IOException e) {
                logger.warn("Data in file was not written ", e);
            }
        }
    }

    protected int countMessages(String filepath) {
        int count = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            while (bufferedReader.readLine() != null) {
                count++;
            }

        } catch (IOException e) {
            logger.debug("File was not read", e);
        }
        logger.debug("messages in {}: {}", filepath, count - UNCOUNTED_FIELD);
        return count;
    }

}