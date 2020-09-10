package boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CoordinateValidator implements ConstraintValidator<CoordinateValidConstraint, String> {

    private static final double MAX_LATITUDE_VALUE = 90.0;
    private static final double MIN_LATITUDE_VALUE = -90.0;
    private static final double MAX_LONGITUDE_VALUE = 180.0;
    private static final double MIN_LONGITUDE_VALUE = -180.0;

    @Override
    public void initialize(CoordinateValidConstraint coordinateValidConstraint) {
        // Do nothing because all logic can be handled at validation time with the isValid() method
    }

    // TODO not do the verification twice: maybe try to save the values or do the validation directly in the Constructor


    /**
     * @param coordinate should be of the format: {longitude},{latitude}
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String coordinate, ConstraintValidatorContext constraintValidatorContext) {
        if(coordinate == null) {
            System.out.println("null");
            return false;
        }

        // TODO extract from brackets

        String[] longitudeLatitude = coordinate.split(",");
        if(longitudeLatitude.length != 2) {
            System.out.println("longitudeLatitude.length != 2");
            return false;
        }
        try {
            double longitude = Double.parseDouble(longitudeLatitude[0]);
            if(longitude < MIN_LONGITUDE_VALUE || longitude > MAX_LONGITUDE_VALUE) { return false; }
            double latitude = Double.parseDouble(longitudeLatitude[1]);
            if(latitude < MIN_LATITUDE_VALUE || latitude > MAX_LATITUDE_VALUE) { return false; }
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException");
            return false;
        }
        return true;
    }


}
