package boursier.louis.hermeez.backend.controllers;

import boursier.louis.hermeez.backend.controllers.user.UserRepository;
import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Test server should be running on the matching port (8080) for those tests to run
 */
@SpringBootTest
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String email = "test@example.com";
    private final String password = "password";
    private final String role = User.Role.USER.name();
    @Autowired
    private UserRepository userRepository;
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


    @BeforeEach
    public void deleteUser() {
        userRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        //  simple sanity check test that will fail if the application context cannot start
    }

    // Registration

    @Test
    public void registerShouldReturnUser() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(email, root.path("email").asText());
    }

    @Test
    public void registerWithExistingEmailShouldReturnConflict() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);

        /**
         * Pay attention to the bodyToMono method,
         * which will throw a WebClientException if the status code is 4xx (client error) or 5xx (Server error).
         * We used the block method on Monos to subscribe and retrieve an actual data which was sent with the response.
         */
        request.exchange().block().bodyToMono(String.class).block(); // ok
        String response = request.exchange().block().bodyToMono(String.class).block(); // conflict

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals("CONFLICT", root.path("status").asText());
    }

    @Test
    public void registerShouldGrantBasicUserRole() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(role, root.path("role").asText());
    }
}