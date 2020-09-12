package boursier.louis.hermeez.backend.entities.coordinate;

import boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinatesValidConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@CoordinatesValidConstraint // Class level constraint for caching coordinate values to fields members during validation
public class Coordinates {

    public static final String COORDINATES_DELIMITER = ":";

    private final String rawContent; // 90.0,90.0:90.0,90.0
    @Valid // nested validation
    private Coordinate[] coordinates;

    public Coordinates(String rawContent) {
        this.rawContent = rawContent;
    }
}
