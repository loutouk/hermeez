package boursier.louis.hermeez.backend.entities.route;

import boursier.louis.hermeez.backend.entities.route.routecontent.RouteContent;
import lombok.Getter;
import lombok.Setter;

/**
 * Route super class that wraps any implementation of a Route object.
 *
 * See {@link boursier.louis.hermeez.backend.entities.route.osrmroute.OSRMRoute}.
 */
@Getter
@Setter
public abstract class AbstractRoute {

    protected RouteContent routeContent;

    public AbstractRoute() {};

}
