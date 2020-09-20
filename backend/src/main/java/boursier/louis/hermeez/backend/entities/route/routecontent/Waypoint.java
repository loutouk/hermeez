package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

/**
 * The different places that make the route. Consists at least of the starting place and the ending place,
 * and can contain more points in between.
 */
@Getter
@Setter
public class Waypoint {
    private Location location;
    private String name;

    public Waypoint(Location location, String name) {
        this.location = location;
        this.name = name;
    }
}
