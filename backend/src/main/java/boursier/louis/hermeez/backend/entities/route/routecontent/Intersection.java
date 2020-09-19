package boursier.louis.hermeez.backend.entities.route.routecontent;

/**
 * An intersection gives a full representation of any cross-way the path passes bay. For every step,
 * the very first intersection (intersections[0]) corresponds to the location of the StepManeuver.
 * See {@link StepManeuver}.
 */
public class Intersection {
    private Location location;
    // bearings: A list of bearing values (e.g. [0,90,180,270]) that are available at the intersection.
    // The bearings describe all available roads at the intersection.
    private int[] bearings;
    // a list of entry flags, corresponding in a 1:1 relationship to the bearings.
    // a value of true indicates that the respective road could be entered on a valid route.
    // false indicates that the turn onto the respective road would violate a restriction.
    private int[] entry;
    // index into bearings/entry array. Used to calculate the bearing just before the turn. Namely,
    // the clockwise angle from true north to the direction of travel immediately before the maneuver/passing the
    // intersection. Bearings are given relative to the intersection. To get the bearing in the direction of driving,
    // the bearing has to be rotated by a value of 180. The value is not supplied for depart maneuvers
    private int in;
    // index into the bearings/entry array. Used to extract the bearing just after the turn. Namely,
    // The clockwise angle from true north to the direction of travel immediately after
    // the maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
    private int out;
    private Lane[] lanes;
}
