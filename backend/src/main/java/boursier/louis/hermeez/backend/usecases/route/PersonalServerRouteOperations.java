package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PersonalServerRouteOperations implements RouteOperations {

    @Override
    public ResponseEntity<RouteDTO> route(Coordinates coordinates) {
        for (Coordinate coordinate : coordinates.getCoordinates()) {

        }
        return new ResponseEntity<>(new RouteDTO(), HttpStatus.OK);
    }
}
