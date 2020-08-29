package boursier.louis.hermeez.backend.userconstraints;

import boursier.louis.hermeez.backend.Utils.EmailValidation;
import boursier.louis.hermeez.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


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

