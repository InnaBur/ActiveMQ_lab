package com.thirdTask;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MessageTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void checkEddr() {
        MyMessage message = new MyMessage("aaaaaaa", "1987040122223", 12, "1972-01-28T07:15:58");
        Set<ConstraintViolation<MyMessage>> constraintViolations = validator.validate(message);

        assertEquals(0, constraintViolations.size());

        MyMessage message1 = new MyMessage("aaaaaaa", "1987040122222", 12, "1972-01-28T07:15:58");
        constraintViolations = validator.validate(message1);

        assertEquals(1, constraintViolations.size());
        assertEquals("Checksum is wrong", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void messageIsValid() {
        MyMessage message = new MyMessage("abcaefgu", "2000121034567", 20, "2023-06-28T07:15:58");

        Set<ConstraintViolation<MyMessage>> constraintViolations = validator.validate(message);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void checkNameLessThenSevenLetters() {
        MyMessage message = new MyMessage("aaa", "0101200033333", 10, "2023");

        Set<ConstraintViolation<MyMessage>> constraintViolations = validator.validate(message);

        assertEquals(1, constraintViolations.size());
        assertEquals("Name length must be longer then 6 symbols", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testMessageWithNoValues() {
        MyMessage message = new MyMessage(null, null, 0, null);

        Set<ConstraintViolation<MyMessage>> constraintViolations = validator.validate(message);
        assertEquals(5, constraintViolations.size());
    }

    @Test
    public void testMessageWithWrongCount() {
        MyMessage message = new MyMessage("aaaaaaa", "2000121034567", 2, "2023-06-28T07:15:58");

        Set<ConstraintViolation<MyMessage>> constraintViolations = validator.validateProperty(message, "count");

        assertEquals(1, constraintViolations.size());
        assertEquals("Must be more than 9", constraintViolations.iterator().next().getMessage());
    }
}