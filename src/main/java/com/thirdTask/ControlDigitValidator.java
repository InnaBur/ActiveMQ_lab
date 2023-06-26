package com.thirdTask;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class ControlDigitValidator implements ConstraintValidator<ControlDigit, String> {

    static final int SEVEN = 7;
    static final int THREE = 3;

    @Override
    public void initialize(ControlDigit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        int controlDigit = 0;
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            int digit = Character.getNumericValue(s.charAt(i));
            if (digitShouldMultiplyOnSeven(i)) {
                digits.add(digit * SEVEN);
            } else if (digitShouldMultiplyOnThree(i)) {
                digits.add(digit * THREE);
            } else {
                digits.add(digit);
            }
        }
        controlDigit = digits.stream().
                mapToInt(Integer::intValue).sum() % 10;
        return Integer.parseInt(String.valueOf(s.charAt(s.length() - 1))) == controlDigit;

    }

    private boolean digitShouldMultiplyOnThree(int i) {
        return i % THREE == 1;
    }

    private boolean digitShouldMultiplyOnSeven(int i) {
        return i % THREE == 0;
    }
}
