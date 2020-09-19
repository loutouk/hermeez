package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
