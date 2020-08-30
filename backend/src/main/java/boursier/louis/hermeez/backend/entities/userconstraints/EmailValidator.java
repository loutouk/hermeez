package boursier.louis.hermeez.backend.entities.userconstraints;

import boursier.louis.hermeez.backend.utils.EmailValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<EmailValidConstraint, String> {

    @Override
    public void initialize(EmailValidConstraint contactNumber) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(EmailValidation.isValidEmailAddress(email));
        return EmailValidation.isValidEmailAddress(email);
    }
}

