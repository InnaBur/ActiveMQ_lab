package com.thirdTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MessageGeneratorTest {

    @Test
    void isGeneratedNameLengthIsInBounds() throws IOException {
        MessageGenerator messageGenerator = new MessageGenerator();

        messageGenerator.properties.setProperty("nameMinLength", "7");
        messageGenerator.properties.setProperty("nameMaxLength", "14");

        int nameLength = messageGenerator.textGenerator().length();

        assertTrue(nameLength >= 7 && nameLength < 14);
    }

    @Test
    void eddrGenerator() throws IOException {
        MessageGenerator messageGenerator = new MessageGenerator();

        messageGenerator.properties.setProperty("eddrLength", "13");

        int eddrLength = messageGenerator.eddrGenerator().length();

        assertTrue(eddrLength == 13);
    }

}