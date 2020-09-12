package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import org.springframework.http.ResponseEntity;

public interface RouteOperations {
    /**
     * Finds the fastest route between coordinates in the supplied order.
     *
     * @param coordinates String of format {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
     * @return
     */
    ResponseEntity<RouteDTO> route(Coordinates coordinates);
}
