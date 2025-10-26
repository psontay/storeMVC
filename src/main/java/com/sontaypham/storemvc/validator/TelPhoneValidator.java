package com.sontaypham.storemvc.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelPhoneValidator implements ConstraintValidator<TelPhoneConstraint , String> {
    @Override
    public boolean isValid(String telPhone, ConstraintValidatorContext constraintValidatorContext) {
        if ( telPhone == null || telPhone.isBlank()) return true;
        return telPhone.length() >= 10;
    }

    @Override
    public void initialize(TelPhoneConstraint telPhoneConstraint) {
        ConstraintValidator.super.initialize(telPhoneConstraint);
    }
}
