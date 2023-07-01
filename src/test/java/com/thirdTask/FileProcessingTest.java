package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileProcessingTest {

    @Test
    void isExceptionIfWrongPathWhenCreateCSV() throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        String filePath = "someFile.csv";
        String[] header = {"name", "count"};
        assertTrue(fileProcessing.createCSV(filePath, header));
    }

    @Test
    void testReadProperty() throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        Properties properties = new Properties();
        String key = "name";
        properties.setProperty(key, "Adjfhkh");
        assertNotNull(fileProcessing.loadProperties());
    }

    @Test
    void ifMessagesCounted() throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        String filepath = "test.txt";

        assertEquals(2, fileProcessing.countMessages(filepath));

    }
}