package boursier.louis.hermeez.backend.entities.route.osrmroute;

import boursier.louis.hermeez.backend.entities.route.AbstractRoute;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import boursier.louis.hermeez.backend.entities.route.RouteDTOConvertible;
import boursier.louis.hermeez.backend.entities.route.routecontent.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

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

        // Way Points
        WayPoint[] wayPoints = new WayPoint[root.get("waypoints").size()];
        for (int i = 0; i < root.get("waypoints").size(); i++) {
            JsonNode wayPointNode = root.get("waypoints").get(i);
            wayPoints[i] = new WayPoint(extractLocation(wayPointNode), wayPointNode.path("name").asText());
        }
        routeContent.setWayPoints(wayPoints);

        // Route
        JsonNode routeNode = root.findValue("routes").get(0); // only considers the first recommended route
        Route route = new Route();
        route.setDuration(routeNode.path("duration").asDouble());
        route.setDistance(routeNode.path("distance").asDouble());

        Leg[] legs = new Leg[routeNode.get("legs").size()];
        for (int i = 0; i < routeNode.get("legs").size(); i++) {
            Leg leg = new Leg();
            JsonNode legsNode = routeNode.get("legs").get(i);
            leg.setDuration(legsNode.path("duration").asDouble());
            leg.setDistance(legsNode.path("distance").asDouble());
            leg.setSummary(legsNode.path("summary").asText());
            // TODO
            legs[i] = leg;
        }
        route.setLegs(legs);


        routeContent.setRoute(route);

        this.routeContent = routeContent;
    }

    private static final Location extractLocation(JsonNode jsonNodeWithLocation) {
        return new Location(
                jsonNodeWithLocation.get("location").get(0).asDouble(),
                jsonNodeWithLocation.get("location").get(1).asDouble()
        );
    }

    @Override
    public RouteDTO toRouteDTO() {
        return new RouteDTO(this.routeContent);
    }
}
