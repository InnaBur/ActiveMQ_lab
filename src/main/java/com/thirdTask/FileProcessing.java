package com.thirdTask;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FileProcessing {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessing.class);
    private static final String FILE_NAME = "config.properties";
    private static final String[] HEADER_VALID = {"Name", "Count"};
    private static final String[] HEADER_ERROR = {"Name", "Count", "Error"};
    private static final String FILEPATH_VALID = "valid.csv";
    private static final String FILEPATH_ERROR = "error.csv";
    CSVWriter writer = new CSVWriter(new FileWriter("valid.csv", true));

    public FileProcessing() throws IOException {
    }

    public void createCSVFiles() {
        createCSV(FILEPATH_VALID, HEADER_VALID);
        createCSV(FILEPATH_ERROR, HEADER_ERROR);
    }

    public Properties loadProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(FILE_NAME)) {

//       try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DIR_NAME + FILE_NAME)) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(reader);
                logger.debug("Properties were loaded");
        } catch (IOException e){
            logger.error("Properties out jar were not loaded ");
        }
       return properties;
    }

    public void createCSV(String filePath,String[] header) {
        File file = new File(filePath);
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);
            writer.writeNext(header);
            logger.info("File {} created", filePath);
            writer.close();
        } catch (IOException e) {
            logger.error("File was not created!");
            e.printStackTrace();
        }
    }

        public static void writeDataLineByLine(CSVWriter writer, String name, String eddr)
    {
        try {

            String [] messageArray = {name, eddr};
            // add data to csv
            writer.writeNext(messageArray);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
//        } catch (JMSException e) {
//            throw new RuntimeException(e);
        }
    }
   }
