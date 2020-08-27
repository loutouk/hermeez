package boursier.louis.hermeez.backend.userconstraints;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


public class EmailValidator implements ConstraintValidator<EmailUniqueConstraint, String> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void initialize(EmailUniqueConstraint contactNumber) {
        // Do nothing because all logic can be handle at validation time with the isValid() method
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (isStringEmpty(email)) {
            return false;
        }
        return isEmailUnique(email);
    }

    private boolean isStringEmpty(String input) {
        return input == null || input.trim().length() == 0;
    }

    private boolean isEmailUnique(String email) {
        Query searchQuery = new Query();
        searchQuery.addCriteria(Criteria.where("email").is(email));
        List<User> existingUsers = mongoOperations.find(searchQuery, User.class);
        return existingUsers.isEmpty();
    }

}

