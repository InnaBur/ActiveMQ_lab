package com.thirdTask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

//    @Test
//    void isDataInvalid() throws IOException {
//
//        DataProcessing dataProcessing = new DataProcessing();
//        MyValidator myValidator = new MyValidator();
//
//        MyMessage message = new MyMessage("Wljywc", "1234567890122", 940, "data");
//        Set<ConstraintViolation<MyMessage>> validateMessage = myValidator.validateMessage(message);
//        String[] expected = {"Wljywc", "940", "{\"errors\":[\"Must be at least one letter 'а'\",\"Checksum is wrong\",\"Name length must be longer then 6 symbols\"]}"};
//
//        assertArrayEquals(expected, dataProcessing.dataInvalid(message, validateMessage));
//    }
}