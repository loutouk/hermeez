package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The use of a class value constraint is motivated by the fact that we need to parse coordinates from the raw string
 * in order to validate it. Once parsed and if coordinates are valid, we take the opportunity of having extracting the
 * data to save it to the class members (latitude and longitude). This saves us from re computing it later.
 * <p>
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinatesValidConstraint}.
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.Coordinates}.
 */
public class CoordinatesValidator implements ConstraintValidator<CoordinatesValidConstraint, Coordinates> {

    @Override
    public void initialize(CoordinatesValidConstraint coordinatesValidConstraint) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    @Override
    public boolean isValid(Coordinates coordinatesObject, ConstraintValidatorContext constraintValidatorContext) {
        if (coordinatesObject == null || coordinatesObject.getRawContent() == null) {
            return false;
        }
        String[] rawCoordinates = coordinatesObject.getRawContent().split(Coordinates.COORDINATES_DELIMITER);
        Coordinate[] coordinates = new Coordinate[rawCoordinates.length];
        for (int i = 0; i < rawCoordinates.length; i++) {
            coordinates[i] = new Coordinate(rawCoordinates[i]);
        }
        // saves the extracted coordinates when they are valid
        coordinatesObject.setCoordinates(coordinates);
        return true;
    }

}
