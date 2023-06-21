package com.thirdTask;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

class MessageTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Test
//    void checkEddr() {
//        Message message = new Message("aaaaaaa", "19870401-22222", 12, "1972-01-28T07:15:58");
//        String eddr = "19870401-22222";
//        Set<ConstraintViolation<Message>> constraintViolations =
//                validator.validate(message);
//
//        assertEquals(0, constraintViolations.size());
////        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
//    }

//        @Test
//        public void manufacturerIsNull() {
//            Message message = new Message( "aaaaaaa", "1234567891234", 10, Data.class);
//
//            Set<ConstraintViolation<Car>> constraintViolations =
//                    validator.validate( car );
//
//            assertEquals( 1, constraintViolations.size() );
//            assertEquals( "must not be null", constraintViolations.iterator().next().getMessage() );
//        }

//        @Test
//        public void licensePlateTooShort() {
//            Car car = new Car( "Morris", "D", 4 );
//
//            Set<ConstraintViolation<Car>> constraintViolations =
//                    validator.validate( car );
//
//            assertEquals( 1, constraintViolations.size() );
//            assertEquals(
//                    "size must be between 2 and 14",
//                    constraintViolations.iterator().next().getMessage()
//            );
//        }


}