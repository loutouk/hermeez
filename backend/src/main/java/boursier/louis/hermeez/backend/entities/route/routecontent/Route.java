package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

/**
 * A possible route between the way points. See {@link WayPoints}.
 */
@Getter
@Setter
public class Route {
    private double distance; // The distance traveled by the route, in meters.
    private double duration; // The estimated travel time, in float number of seconds.
    private Leg[] legs; // The routes between each way points.
}
