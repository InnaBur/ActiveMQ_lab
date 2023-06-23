package com.thirdTask;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MyValidator {
//    private static Validator validator;


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    FileProcessing fileProcessing = new FileProcessing();

    public MyValidator() throws IOException {
    }

    protected void validateMessage(MyMessage message) throws IOException {
        Set<ConstraintViolation<MyMessage>> violations = validator.validate(message);

        if (violations.size() == 0) {
            fileProcessing.writeIntoFile(message, "valid.csv");
            System.out.println("valid");
        } else {
            fileProcessing.writeIntoFile(message, "error.csv");
            System.out.println("NO");
        }

    }

}
