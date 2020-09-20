package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step {
    private Intersection[] intersections;
    private StepManeuver[] stepManeuvers;
    private double distance; // The distance traveled by the route, in meters.
    private double duration; // The estimated travel time, in float number of seconds.
    private String summary; // The name of the main road or axis in this leg.
}
