package boursier.louis.hermeez.backend.controllers.route;

import boursier.louis.hermeez.backend.controllers.user.UserController;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import boursier.louis.hermeez.backend.usecases.route.RouteOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Constants.API_NAME)
public class RouteController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private RouteOperations routeOperations;


    /**
     * Finds the fastest route between coordinates in the supplied order.
     *
     * @param coordinates String of format {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
     * @return
     */
    @GetMapping("/route")
    ResponseEntity<RouteDTO> route(@NotNull @Valid @ModelAttribute("coordinates") Coordinates coordinates) {
        LOGGER.info("route path call");
        return routeOperations.route(coordinates);
    }


    /**
     * Finds an approximation of the Traveling Salesman problem.
     *
     * @param coordinates String of format {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
     * @return
     */
    @GetMapping("/trip")
    ResponseEntity<RouteDTO> trip(@NotNull @Valid @ModelAttribute("coordinates") Coordinates coordinates) {
        LOGGER.info("trip path call");
        // TODO
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}
