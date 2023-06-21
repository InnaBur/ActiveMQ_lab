package com.thirdTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FileProcessing {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessing.class);
    private static final String FILE_NAME = "conf/config.properties";
    private static final String DIR_NAME = "conf/";

//    protected String readProperty(String key) throws IOException {
//        Properties properties = new Properties();
//        try {
//            loadingProperties(properties, key, FILE_NAME);
//               if (properties.getProperty(key) == null) {
//            loadingProperties(properties, key, DIR_NAME + FILE_NAME);
//        }
//        } catch (NullPointerException | IOException e ) {
//            logger.error("Properties were not loaded ", e);
//        }
//        return  properties.getProperty(key);
//    }
//protected String readProperty(String key) throws IOException {
//    Properties properties = new Properties();
//    try {
//        loadingProperties(properties, key, FILE_NAME);
//        if (properties.getProperty(key) == null) {
//            loadingProperties(properties, key, DIR_NAME + FILE_NAME);
//        }
//    } catch (NullPointerException | IOException e ) {
//        logger.error("Properties were not loaded ", e);
//    }
//    return  properties.getProperty(key);
//}
//    public void loadingProperties(Properties properties, String key, String fileName) throws IOException {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
//        if (inputStream != null) {
//            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//            properties.load(reader);
//            if (properties.getProperty(key) != null) {
//                logger.debug("Properties were loaded");
//            }
//        } else {
//            logger.error("Properties out jar were not loaded ");
//        }
//    }

    public Properties loadProperties() throws IOException {
        Properties properties = new Properties();
       try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {;
//        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(reader);
//            if (properties.getProperty(key) != null) {
                logger.debug("Properties were loaded");
//            }
        } catch (IOException e){
            logger.error("Properties out jar were not loaded ");
        }
       return properties;
    }
}
