package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import org.springframework.http.ResponseEntity;

public interface RouteOperations {
    ResponseEntity<RouteDTO> shortestPath(Coordinate startCoordinates, Coordinate endCoordinates);
}
