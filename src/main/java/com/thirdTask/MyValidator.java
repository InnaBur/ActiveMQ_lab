package com.thirdTask;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class MyValidator {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    protected Set<ConstraintViolation<MyMessage>> validateMessage(MyMessage message) {
        return validator.validate(message);
    }

}
