package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FileProcessingTest {
    private final String filepath = "test.txt";
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

    @Test
    void ifDataIsWroteIntoFile() throws IOException {
        FileProcessing fileProcessing = Mockito.mock(FileProcessing.class);

        String [] data = {"smth data"};

        fileProcessing.writeIntoFile(filepath, data);

        Mockito.verify(fileProcessing).writeIntoFile(filepath, data);
    }

    @Test
    void ifDataIsWroteIntoFileAfterValidation() throws IOException {
MyMessage message = new MyMessage("aaaaaaa", "1987040122223", 12, "1972-01-28T07:15:58");
FileProcessing fileProcessing = Mockito.mock(FileProcessing.class);
DataProcessing dataProcessing = Mockito.mock(DataProcessing.class);
        Queue <MyMessage> list = new LinkedList<>();
        list.add(message);
        MyValidator myValidator = Mockito.mock(MyValidator.class);
        String [] validData = {"name", "count"};
        String [] invalidData = {"not", "valid"};

        when(myValidator.validateMessage(message)).thenReturn(null);
        when(dataProcessing.dataValid(message)).thenReturn(validData);
        when(dataProcessing.dataInvalid(message, null)).thenReturn(invalidData);

        fileProcessing.writeInFilesAfterValidation(list);

        verify(fileProcessing).writeInFilesAfterValidation(list);
        verify(fileProcessing, times(1)).writeInFilesAfterValidation(list);
    }

}