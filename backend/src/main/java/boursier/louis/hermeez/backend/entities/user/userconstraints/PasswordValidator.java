package boursier.louis.hermeez.backend.entities.user.userconstraints;

import boursier.louis.hermeez.backend.utils.PasswordValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidConstraint, String> {

    @Override
    public void initialize(PasswordValidConstraint passwordValidConstraint) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return PasswordValidation.isValidPassword(password);
    }
}

