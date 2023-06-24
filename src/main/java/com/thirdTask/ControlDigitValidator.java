package com.thirdTask;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ControlDigitValidator implements ConstraintValidator<ControlDigit, String>  {

    @Override
    public void initialize(ControlDigit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//        long eddr = Long.parseLong(s);
        ArrayList<Integer> digits = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            int digit = Integer.parseInt(String.valueOf(s.charAt(i)));
            if (i % 3 == 0) {
                digits.add(digit * 7);
            } else if (i == 1 || i == 4 || i == 7 || i == 10 || i == 13) {
                digits.add(digit * 3);
            } else {
                digits.add(digit * 1);
            }
        }
        int controlDigit = digits.stream().
                mapToInt(Integer::intValue).sum() % 10;
if (Integer.parseInt(String.valueOf(s.charAt(s.length()-1))) == controlDigit) {
    return true;
        }
        return false;
    }
}
