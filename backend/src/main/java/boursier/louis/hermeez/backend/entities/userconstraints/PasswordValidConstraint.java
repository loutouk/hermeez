package boursier.louis.hermeez.backend.entities.userconstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidConstraint {
    String message() default "password is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}