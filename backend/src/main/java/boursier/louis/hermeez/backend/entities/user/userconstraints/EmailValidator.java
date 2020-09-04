package boursier.louis.hermeez.backend.entities.user.userconstraints;

import boursier.louis.hermeez.backend.utils.EmailValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidConstraint, String> {

    @Override
    public void initialize(EmailValidConstraint emailValidConstraint) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    /**
     * This validation rule is too simple on purpose.
     * It's better to do a loose email validation rather than to do a strict one and reject some people.
     * The best way to validate an email address is to send it an email.
     * The only goal here is to prevent security threats on String format.
     *
     * @param email
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return EmailValidation.isValidEmail(email);
    }
}

