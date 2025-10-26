package com.sontaypham.storemvc.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if ( password == null || password.isBlank() ) return true;
        return password.chars().anyMatch( Character::isDigit ) && password.chars().anyMatch( Character::isUpperCase) && password.chars().anyMatch( c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c));
    }

    @Override
    public void initialize(PasswordConstraint passworConstraint) {
        ConstraintValidator.super.initialize(passworConstraint);
    }
}
