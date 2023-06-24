package com.thirdTask;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ControlDigitValidator.class)
@Documented
public @interface ControlDigit {

        String message() default "{com.thirdTask.ControlSum." +
                "Checksum is wrong}";

        Class<?>[] groups() default { };

        Class<? extends Payload>[] payload() default { };

    }

