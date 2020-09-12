package boursier.louis.hermeez.backend.usecases.route;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.entities.route.OSRMResponse;
import boursier.louis.hermeez.backend.entities.route.Route;
import boursier.louis.hermeez.backend.entities.route.RouteDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class PersonalServerRouteOperations implements RouteOperations {

    private static final String ROUTING_SERVER_BASE_URL = "http://localhost:5000/";
    private static final String ROUTING_SERVER_ROUTE_PATH = "route/v1/bicycling/";

    @Override
    public ResponseEntity<RouteDTO> route(Coordinates coordinates) {
        WebClient webClient = WebClient.builder().baseUrl(ROUTING_SERVER_BASE_URL).build();
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("steps", "true"); // returns route instructions for each trip
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);
        String OSRMFormatCoordinates = coordinates.getRawContent().replaceAll(":", ";");
        String routeURL = ROUTING_SERVER_BASE_URL + ROUTING_SERVER_ROUTE_PATH + OSRMFormatCoordinates;

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(routeURL)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        // TODO inspect response and adjust return type and HttpStatus accordingly (look at code and emit custom OSRMException)
        return new ResponseEntity<>(new RouteDTO(new Route(new OSRMResponse(response))), HttpStatus.OK);
    }
}
