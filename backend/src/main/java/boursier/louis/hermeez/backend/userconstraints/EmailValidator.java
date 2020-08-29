package boursier.louis.hermeez.backend.userconstraints;

import boursier.louis.hermeez.backend.utils.EmailValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<EmailValidConstraint, String> {

    @Override
    public void initialize(EmailValidConstraint contactNumber) {
        // Do nothing because all logic can be handle at validation time with the isValid() method
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return EmailValidation.isValidEmailAddress(email);
    }
}

