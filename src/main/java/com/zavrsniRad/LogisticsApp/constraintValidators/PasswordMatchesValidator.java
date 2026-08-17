package com.zavrsniRad.LogisticsApp.constraintValidators;

import com.zavrsniRad.LogisticsApp.annotations.PasswordMatches;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if(value instanceof SignUpRequest dto){
            return dto.getPassword() !=null && dto.getPassword().equals(dto.getConfirmPassword());
        }

        return false;
    }
}
