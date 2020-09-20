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
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.IOException;

/**
 * Corresponds to the response of the OSRM API routing server for routing queries.
 * Doc is available at http://project-osrm.org/docs/v5.5.1/api/
 */
@Getter
@Setter
public class OSRMRoute extends AbstractRoute implements RouteDTOConvertible {

    public OSRMRoute(String rawResponse) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(rawResponse);

        // Way Points
        Waypoint[] wayPoints = new Waypoint[root.get("waypoints").size()];
        for (int i = 0; i < root.get("waypoints").size(); i++) {
            JsonNode wayPointNode = root.get("waypoints").get(i);
            wayPoints[i] = new Waypoint(extractLocation(wayPointNode), wayPointNode.path("name").asText());
        }
        // Set Way Points
        this.waypoints = wayPoints;

        // Route for the RouteContent
        JsonNode routeNode = root.findValue("routes").get(0); // only considers the first recommended route
        Route route = new Route();
        route.setDuration(routeNode.path("duration").asDouble());
        route.setDistance(routeNode.path("distance").asDouble());

        // Legs in route
        Leg[] legs = new Leg[routeNode.get("legs").size()];
        for (int i = 0; i < routeNode.get("legs").size(); i++) {
            Leg leg = new Leg();
            JsonNode legNode = routeNode.get("legs").get(i);
            leg.setDuration(legNode.path("duration").asDouble());
            leg.setDistance(legNode.path("distance").asDouble());
            leg.setSummary(legNode.path("summary").asText());

            // Steps in Leg
            Step[] steps = new Step[legNode.get("steps").size()];
            for (int j = 0; j < legNode.get("steps").size(); j++) {
                Step step = new Step();
                JsonNode stepNode = legNode.get("steps").get(j);
                step.setDuration(stepNode.path("duration").asDouble());
                step.setDistance(stepNode.path("distance").asDouble());
                step.setSummary(stepNode.path("summary").asText());

                // Intersections in Step
                Intersection[] intersections = new Intersection[stepNode.get("intersections").size()];
                for (int k = 0; k < stepNode.get("intersections").size(); k++) {
                    Intersection intersection = new Intersection();
                    JsonNode intersectionNode = stepNode.get("intersections").get(k);
                    intersection.setIn(intersectionNode.path("in").asInt());
                    intersection.setOut(intersectionNode.path("out").asInt());
                    intersection.setLocation(extractLocation(intersectionNode));
                    int[] bearings = new ObjectMapper().readValue(intersectionNode.path("bearings").toString(), int[].class);
                    intersection.setBearings(bearings);
                    boolean[] entry = new ObjectMapper().readValue(intersectionNode.path("entry").toString(), boolean[].class);
                    intersection.setEntry(entry);

                    // Set Intersection in Intersections
                    intersections[k] = intersection;
                }

                // StepManeuver in Step
                // TODO
                StepManeuver[] stepManeuvers = new StepManeuver[stepNode.get("maneuver").size()];
                // Set StepManeuver in StepManeuvers

                // Set Intersections to Step
                step.setIntersections(intersections);

                // Set StepManeuvers to Step
                step.setStepManeuvers(stepManeuvers);

                // Set Step in Steps
                steps[j] = step;
            }

            // Set Steps in Leg
            leg.setSteps(steps);

            // Set Leg in Legs
            legs[i] = leg;
        }
        // Set Legs for Route
        route.setLegs(legs);

        // Set Route
        this.route = route;

    }

    @Override
    public RouteDTO toRouteDTO() {
        return new RouteDTO(this.waypoints, this.route);
    }

    private static final Location extractLocation(JsonNode jsonNodeWithLocation) {
        return new Location(
                jsonNodeWithLocation.get("location").get(0).asDouble(),
                jsonNodeWithLocation.get("location").get(1).asDouble()
        );
    }
}
