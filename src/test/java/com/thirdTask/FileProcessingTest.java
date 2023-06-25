package com.thirdTask;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

class FileProcessingTest {

    @Test
    void isExceptionIfWrongPathWhenCreateCSV() throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        String filePath = "someFile.csv";
        String[] header = {"name", "count"};
        assertTrue(fileProcessing.createCSV(filePath, header));
    }

    @Test
    public void testReadProperty() throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        Properties properties = new Properties();
        String key = "name";
        properties.setProperty(key, "Adjfhkh");
        assertNotNull(fileProcessing.loadProperties());
    }
}