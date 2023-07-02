package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MessageGeneratorTest {

    @Test
    void isMessageGenerate() throws IOException {
        BlockingQueue<MyMessage> queue = new LinkedBlockingQueue<>();
        DataProcessing dataProcessingTest = Mockito.mock(DataProcessing.class);
        MessageGenerator messageGenerator = new MessageGenerator();
        messageGenerator.dataProcessing = dataProcessingTest;

        when(dataProcessingTest.readOutputFormat()).thenReturn("10");

        queue = messageGenerator.generateMessage();
        assertEquals(10, queue.size());
    }

    @Test
    void isGeneratedNameLengthIsInBounds() throws IOException {
        MessageGenerator messageGenerator = new MessageGenerator();
        Properties propertiesTest = Mockito.mock(Properties.class);
        messageGenerator.properties = propertiesTest;
        when(propertiesTest.getProperty("nameMinLength")).thenReturn("3");
        when(propertiesTest.getProperty("nameMaxLength")).thenReturn("6");


        int nameLength = messageGenerator.textGenerator().length();

        assertTrue(nameLength >= 3 && nameLength <= 6);
    }

    @Test
    void eddrGenerator() throws IOException {
        MessageGenerator messageGenerator = new MessageGenerator();
        messageGenerator.properties.setProperty("eddrLength", "13");

        int eddrLength = messageGenerator.eddrGenerator().length();

        assertTrue(eddrLength == 13);
    }

}