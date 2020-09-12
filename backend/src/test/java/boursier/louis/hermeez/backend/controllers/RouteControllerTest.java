package boursier.louis.hermeez.backend.controllers;

import boursier.louis.hermeez.backend.entities.coordinate.Coordinate;
import boursier.louis.hermeez.backend.entities.coordinate.Coordinates;
import boursier.louis.hermeez.backend.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test server should be running on the matching port (8080) for those tests to run
 */
@SpringBootTest
public class RouteControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebClient client;

    @BeforeClass
    public void init() {

        // Oftentimes, the default HTTP timeouts of 30 seconds are too slow for our needs
        // this is a signal timeout, not an HTTP connection or read/write timeout
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });

        client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    @Test
    public void routeRequestWithMissingLongitudeOrLatitudeShouldReturnBadRequest() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/route";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("coordinates", "170.50"); // delimiter and lat/long is missing
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());


        map.clear();
        map.add("coordinates", "170.50" + Coordinate.LAT_LONG_DELIMITER); // lat/long is missing
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());


        map.clear();
        map.add("coordinates", "90" + Coordinate.LAT_LONG_DELIMITER + "90" +
                Coordinates.COORDINATES_DELIMITER + "170.50" + Coordinate.LAT_LONG_DELIMITER);
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());
    }

    @Test
    public void routeRequestWithWrongFormatShouldReturnBadRequest() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/route";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("coordinates", "shouldBeSomeCoordinate");
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());


        map.clear();
        map.add("coordinates", "shouldBeSomeCoordinate" + Coordinate.LAT_LONG_DELIMITER + "shouldBeSomeCoordinate");
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());

        map.clear();
        map.add("coordinates", "110.50" + Coordinate.LAT_LONG_DELIMITER + "72.48" +
                Coordinates.COORDINATES_DELIMITER + "shouldBeSomeCoordinate");
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        root = objectMapper.readTree(response);
        assertEquals("BAD_REQUEST", root.path("status").asText());
    }

    @Test
    public void routeRequestWithOneGoodCoordinateShouldReturnOk() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/route";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("coordinates", "-112.498781" + Coordinate.LAT_LONG_DELIMITER + "80");
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertNotEquals("BAD_REQUEST", root.path("status").asText());
    }

    @Test
    public void routeRequestWithMultipleGoodCoordinatesShouldReturnOk() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/route";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("coordinates", "-112.498781" + Coordinate.LAT_LONG_DELIMITER + "80" +
                Coordinates.COORDINATES_DELIMITER + "120" + Coordinate.LAT_LONG_DELIMITER + "-4.498498");
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertNotEquals("BAD_REQUEST", root.path("status").asText());
    }
}