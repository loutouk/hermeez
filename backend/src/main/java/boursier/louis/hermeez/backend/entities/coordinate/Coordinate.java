package boursier.louis.hermeez.backend.entities.coordinate;

import boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinateValidConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CoordinateValidConstraint // Class level constraint for caching coordinate values to fields members during validation
public class Coordinate {

    public static final String LAT_LONG_DELIMITER = ",";

    private final String rawContent; // 90.0,90.0
    private double longitude;
    private double latitude;

    public Coordinate(String rawContent) {
        this.rawContent = rawContent;
    }
}
