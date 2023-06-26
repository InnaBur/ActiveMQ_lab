package com.thirdTask;

import com.opencsv.CSVWriter;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

public class FileProcessing {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessing.class);
    private static final String FILE_NAME = "config.properties";
    private static final String[] HEADER_VALID = {"Name", "Count"};
    private static final String[] HEADER_ERROR = {"Name", "Count", "Error"};
    private static final String FILEPATH_VALID = "valid.csv";
    private static final String FILEPATH_ERROR = "error.csv";
    DataProcessing dataProcessing = new DataProcessing();

    public FileProcessing() throws IOException {
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

    protected void writeInFilesAfterValidation(MyMessage message, MyValidator myValidator) {
        Set<ConstraintViolation<MyMessage>> validateMessage = myValidator.validateMessage(message);
        try {
            if (validateMessage.isEmpty()) {
                writeIntoFile(FILEPATH_VALID, dataProcessing.dataValid(message));
            } else {
                writeIntoFile(FILEPATH_ERROR, dataProcessing.dataInvalid(message, validateMessage));
            }
        } catch (IOException e) {
            logger.warn("Data in file was not written ", e);
        }
    }

    protected void countMessages(String filepath) {
        int count = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            while (bufferedReader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        logger.debug("messages in {}: {}", filepath, count - 1);
    }


}