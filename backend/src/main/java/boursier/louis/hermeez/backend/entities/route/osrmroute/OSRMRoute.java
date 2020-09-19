package boursier.louis.hermeez.backend.entities.route.osrmroute;

import boursier.louis.hermeez.backend.entities.route.AbstractRoute;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import boursier.louis.hermeez.backend.entities.route.RouteDTOConvertible;
import boursier.louis.hermeez.backend.entities.route.routecontent.Location;
import boursier.louis.hermeez.backend.entities.route.routecontent.RouteContent;
import boursier.louis.hermeez.backend.entities.route.routecontent.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Corresponds to the response of the OSRM API routing server for routing queries.
 * Doc is available at http://project-osrm.org/docs/v5.5.1/api/
 */
@Getter
@Setter
public class OSRMRoute extends AbstractRoute implements RouteDTOConvertible {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OSRMRoute(String rawContent) throws JsonProcessingException {
        RouteContent routeContent = new RouteContent();
        JsonNode root = objectMapper.readTree(rawContent);

        // waypoints
        ArrayList<WayPoint> wayPoints = new ArrayList<>();
        for(JsonNode node : root.findValue("waypoints")){
            wayPoints.add(new WayPoint(new Location(), node.path("name").asText()));
        }
        // TODO directly use ArrayList instead of array in RouteContent?
        WayPoint[] wayPointsArray = wayPoints.toArray(new WayPoint[wayPoints.size()]);
        routeContent.setWayPoints(wayPointsArray);


        this.routeContent = routeContent;
    }


    @Override
    public RouteDTO toRouteDTO() {
        return new RouteDTO(this.routeContent);
    }
}
