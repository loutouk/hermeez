package boursier.louis.hermeez.backend.entities.route;

import boursier.louis.hermeez.backend.entities.route.routecontent.Route;
import boursier.louis.hermeez.backend.entities.route.routecontent.Waypoint;
import lombok.Getter;
import lombok.Setter;

/**
 * Route super class that wraps any implementation of a Route object.
 * <p>
 * See {@link boursier.louis.hermeez.backend.entities.route.osrmroute.OSRMRoute}.
 */
@Getter
@Setter
public abstract class AbstractRoute {

    protected Waypoint[] waypoints;
    protected Route route;

    public AbstractRoute() {}

}
