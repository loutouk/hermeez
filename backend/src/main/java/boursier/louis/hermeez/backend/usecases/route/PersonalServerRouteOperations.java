package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.apierror.routeoperationserror.OSRMQueryException;
import boursier.louis.hermeez.backend.apierror.routeoperationserror.OSRMResponseException;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.entities.route.OSRMResponse;
import boursier.louis.hermeez.backend.entities.route.Route;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import boursier.louis.hermeez.backend.usecases.user.MongoUserOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

public class PersonalServerRouteOperations implements RouteOperations {

    private static final Logger LOGGER = LogManager.getLogger(MongoUserOperations.class);
    private static final String ROUTING_SERVER_BASE_URL = "http://localhost:5000/";
    private static final String ROUTING_SERVER_ROUTE_PATH = "route/v1/bicycling/";
    private static final String ROUTING_SERVER_STEPS_DETAILS_PARAMETER = "steps=true";
    private static final String OSRM_OK_RESPONSE_CODE = "Ok";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<RouteDTO> route(Coordinates coordinates) {
        String OSRMFormatCoordinates = formatCoordinatesToOSRMCoordinates(coordinates.getRawContent());
        String routeURL = ROUTING_SERVER_BASE_URL + ROUTING_SERVER_ROUTE_PATH + OSRMFormatCoordinates + "?" +
                ROUTING_SERVER_STEPS_DETAILS_PARAMETER;
        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(routeURL)
                .contentType(MediaType.MULTIPART_FORM_DATA);
        return processRequestToOSRMServer(request);
    }

    private ResponseEntity<RouteDTO> processRequestToOSRMServer(WebClient.RequestHeadersSpec<?> request) {
        String response = request.exchange().onErrorResume(e -> {
            LOGGER.error("Route endpoint error while requesting the OSRM server: " + e.getMessage());
            throw new OSRMQueryException();
        }).block().bodyToMono(String.class).block();

        JsonNode root = null;
        try {
            root = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while reading OSRM response for route endpoint: " + e.getMessage());
            throw new OSRMResponseException();
        }
        if(root.has("code")) {
            if(root.path("code").asText().equals(OSRM_OK_RESPONSE_CODE)) {
                LOGGER.info("routing server called for route endpoint");
                return new ResponseEntity<>(new RouteDTO(new Route(new OSRMResponse(response))), HttpStatus.OK);
            } else {
                LOGGER.error("Error while looking at OSRM response for route endpoint: " + root.path("code").asText());
                throw new OSRMResponseException();
            }
        } else {
            LOGGER.error("Error while looking at OSRM response for route endpoint. No error code available.");
            throw new OSRMResponseException();
        }
    }

    /**
     * Our internal Coordinates entity may use a different format than the OSRM server we are calling.
     * This function translates our local format to the OSRM format.
     * See {@link Coordinates#rawContent}.
     * @param coordinates
     * @return
     */
    private static String formatCoordinatesToOSRMCoordinates(String coordinates) {
        return coordinates.replaceAll(Coordinates.COORDINATES_DELIMITER, Constants.OSRM_COORDINATES_DELIMITER);
    }
}
