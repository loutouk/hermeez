package boursier.louis.hermeez.backend.controllers.route;

import boursier.louis.hermeez.backend.controllers.user.UserController;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import boursier.louis.hermeez.backend.usecases.route.RouteOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constants.API_NAME)
public class RouteController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private RouteOperations routeOperations;

    @GetMapping("/shortestpath")
    ResponseEntity<RouteDTO> shortestPath(@Valid @ModelAttribute("startCoordinates") Coordinate startCoordinates,
                                          @Valid @ModelAttribute("endCoordinates") Coordinate endCoordinates) {
        LOGGER.info("shortest path call");
        return routeOperations.shortestPath(startCoordinates, endCoordinates);
    }
}
