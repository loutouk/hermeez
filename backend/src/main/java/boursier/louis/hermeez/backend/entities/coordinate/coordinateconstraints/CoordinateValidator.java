package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The use of a class value constraint is motivated by the fact that we need to parse coordinates from the raw string
 * in order to validate it. Once parsed and if coordinates are valid, we take the opportunity of having extracting the
 * data to save it to the class members (latitude and longitude). This saves us from re computing it later.
 * <p>
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinateValidConstraint}.
 * See {@link boursier.louis.hermeez.backend.entities.coordinate.Coordinate}.
 */
public class CoordinateValidator implements ConstraintValidator<CoordinateValidConstraint, Coordinate> {

    private static final double MAX_LATITUDE_VALUE = 90.0;
    private static final double MIN_LATITUDE_VALUE = -90.0;
    private static final double MAX_LONGITUDE_VALUE = 180.0;
    private static final double MIN_LONGITUDE_VALUE = -180.0;

    @Override
    public void initialize(CoordinateValidConstraint coordinateValidConstraint) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    @Override
    public boolean isValid(Coordinate coordinateObject, ConstraintValidatorContext constraintValidatorContext) {
        double longitude, latitude;
        if (coordinateObject == null || coordinateObject.getRawContent() == null) {
            return false;
        }
        String[] longitudeLatitude = coordinateObject.getRawContent().split(Coordinate.LAT_LONG_DELIMITER);
        if (longitudeLatitude.length != 2) {
            return false;
        }

        try {
            longitude = Double.parseDouble(longitudeLatitude[0]);
            if (longitude < MIN_LONGITUDE_VALUE || longitude > MAX_LONGITUDE_VALUE) {
                return false;
            }
            latitude = Double.parseDouble(longitudeLatitude[1]);
            if (latitude < MIN_LATITUDE_VALUE || latitude > MAX_LATITUDE_VALUE) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // saves the extracted values when valid
        coordinateObject.setLongitude(longitude);
        coordinateObject.setLatitude(latitude);
        return true;
    }

}
