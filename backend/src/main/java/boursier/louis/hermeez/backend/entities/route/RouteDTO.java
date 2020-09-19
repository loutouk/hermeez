package boursier.louis.hermeez.backend.entities.route;

import boursier.louis.hermeez.backend.entities.route.routecontent.RouteContent;

import java.io.Serializable;

public class RouteDTO extends AbstractRoute implements Serializable {

    public RouteDTO(RouteContent routeContent) { this.routeContent = routeContent; }
}
