package boursier.louis.hermeez.backend.userconstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUniqueConstraint {
    String message() default "email not unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}