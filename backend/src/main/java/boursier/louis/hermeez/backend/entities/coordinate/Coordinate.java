package boursier.louis.hermeez.backend.entities.coordinate;

import boursier.louis.hermeez.backend.entities.coordinate.coordinateconstraints.CoordinateValidConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class Coordinate {

    @Valid
    @CoordinateValidConstraint
    private final String rawContent;
    private double longitude;
    private double latitude;

    /**
     * Latitude and longitude fields will be dynamically built when needed. This is why the raw content coordinates
     * must be validated on object instantiation to guarantee valid coordinates;
     * @param rawContent
     */
    public Coordinate(String rawContent) {
        this.rawContent = rawContent;
        latitude = 0;
        longitude = 0;
    }

    public double getLatitude() {
        if(latitude == 0) {
            // TODO
        }
        return latitude;
    }

    public double getLongitude() {
        if(longitude == 0) {
            // TODO
        }
        return longitude;
    }
}
