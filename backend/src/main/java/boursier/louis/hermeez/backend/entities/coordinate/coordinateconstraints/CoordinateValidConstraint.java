package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinateValidConstraint}.
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.Coordinate}.
 */
@Documented
@Constraint(validatedBy = CoordinateValidator.class)
@Target({ElementType.TYPE}) // Class level
@Retention(RetentionPolicy.RUNTIME)
public @interface CoordinateValidConstraint {
    String message() default "coordinate are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}