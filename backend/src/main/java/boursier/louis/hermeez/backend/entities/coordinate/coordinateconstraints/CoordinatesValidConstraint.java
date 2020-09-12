package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinatesValidConstraint}.
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.Coordinates}.
 */
@Documented
@Constraint(validatedBy = CoordinatesValidator.class)
@Target({ElementType.TYPE}) // Class level
@Retention(RetentionPolicy.RUNTIME)
public @interface CoordinatesValidConstraint {
    String message() default "coordinate are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}