package com.thirdTask;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DataProcessingTest {

    @Test
    void readOutputFormat() {
        System.setProperty("N", String.valueOf(100));
        String N = System.getProperty("N");

        assertEquals("100", N);
    }

    @Test
    void isDataValid() {
        DataProcessing dataProcessing = new DataProcessing();
        MyMessage message = new MyMessage("Wljywc", "123445678", 940, "data");
        String[] expected = {"Wljywc", "940"};
        assertArrayEquals(expected, dataProcessing.dataValid(message));
    }

    @Test
    void isDataInvalid() throws IOException {

        DataProcessing dataProcessing = new DataProcessing();
        MyValidator myValidator = new MyValidator();
        MyMessage message = new MyMessage("Abcdabcd", "2023010123456", 11, "data");

        Set<ConstraintViolation<MyMessage>> validateMessage = myValidator.validateMessage(message);
        String[] expected = {"Abcdabcd", "11", "{\"errors\":[\"Checksum is wrong\"]}"};

        assertArrayEquals(expected, dataProcessing.dataInvalid(message, validateMessage));
    }
}