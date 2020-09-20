package boursier.louis.hermeez.backend.entities.route;


import boursier.louis.hermeez.backend.entities.route.routecontent.Route;
import boursier.louis.hermeez.backend.entities.route.routecontent.Waypoint;

import java.io.Serializable;

public class RouteDTO extends AbstractRoute implements Serializable {

    public RouteDTO(Waypoint[] waypoints, Route route){
        this.waypoints = waypoints;
        this.route = route;
    }
}
