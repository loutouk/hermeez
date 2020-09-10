package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PersonalServerRouteOperations implements RouteOperations {
    @Override
    public ResponseEntity<RouteDTO> shortestPath(Coordinate startCoordinates, Coordinate endCoordinates) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
