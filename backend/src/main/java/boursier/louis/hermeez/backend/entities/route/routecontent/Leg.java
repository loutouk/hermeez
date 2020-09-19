package boursier.louis.hermeez.backend.entities.route.routecontent;

/**
 * The route between two way points.
 */
public class Leg {
    private double distance; // The distance traveled by the route, in meters.
    private double duration; // The estimated travel time, in float number of seconds.
    private String summary; // The name of the main road or axis in this leg.
    private Step[] steps;
}
