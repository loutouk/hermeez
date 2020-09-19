package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteContent {
    private WayPoint[] wayPoints;
    private Route route;
}
