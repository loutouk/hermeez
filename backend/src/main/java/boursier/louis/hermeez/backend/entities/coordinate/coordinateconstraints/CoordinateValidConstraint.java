package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = CoordinateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoordinateValidConstraint {
    String message() default "coordinate is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}