package com.thirdTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    protected Set<ConstraintViolation<MyMessage>> validateMessage(MyMessage message) {
//        Set<ConstraintViolation<MyMessage>> violations = validator.validate(message);
return validator.validate(message);


    }

    protected String getErrorMessages(Set<ConstraintViolation<MyMessage>> constraintViolations) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        ArrayList<String> errors = new ArrayList<>();
             for (ConstraintViolation<MyMessage> constraintViolation : constraintViolations) {
                 errors.add(constraintViolations.iterator().next().getMessage());
                 json = objectMapper.writeValueAsString(errors);
             }
       return json;
    }
}
