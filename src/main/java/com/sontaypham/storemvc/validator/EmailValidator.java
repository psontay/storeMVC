package com.sontaypham.storemvc.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    if (email == null || email.isBlank()) return true;
    return email.endsWith("@gmail.com");
  }
  @Override
  public void initialize(EmailConstraint emailConstraint) {
    ConstraintValidator.super.initialize(emailConstraint);
  }
}
