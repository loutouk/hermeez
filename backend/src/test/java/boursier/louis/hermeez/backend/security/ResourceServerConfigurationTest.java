package boursier.louis.hermeez.backend.security;

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
import org.springframework.util.Base64Utils;
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
public class ResourceServerConfigurationTest {

    @Autowired
    private UserRepository userRepository;

    WebClient client;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String email = "test@example.com";
    private final String password = "password";
    private final String role = User.Role.USER.name();

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

    @Test
    public void accessBaseUrlWithoutTokenShouldReturnUnauthorized() throws Exception {
        String baseUrl = "http://localhost:8080/" + Constants.API_NAME + "/";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(baseUrl)
                .body(inserter);

        /**
         * Pay attention to the bodyToMono method,
         * which will throw a WebClientException if the status code is 4xx (client error) or 5xx (Server error).
         * We used the block method on Monos to subscribe and retrieve an actual data which was sent with the response.
         */
        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals("UNAUTHORIZED", root.path("status").asText());
    }

    @Test
    public void accessOAuthTokenUrlWithoutClientCredentialsHeaderShouldReturnUnauthorized() throws Exception {
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        String baseUrl = "http://localhost:8080/" + Constants.API_NAME + "/oauth/token";

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);

        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);

        request.exchange().block().bodyToMono(String.class).block(); // registers the user

        request = WebClient.create().method(HttpMethod.POST).uri(baseUrl).body(inserter);

        /**
         * Pay attention to the bodyToMono method,
         * which will throw a WebClientException if the status code is 4xx (client error) or 5xx (Server error).
         * We used the block method on Monos to subscribe and retrieve an actual data which was sent with the response.
         */
        String response = request.exchange().block().bodyToMono(String.class).block();

        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals("UNAUTHORIZED", root.path("status").asText()); // InsufficientAuthenticationException
    }

    @Test
    public void accessOAuthTokenUrlWithGoodClientCredentialsShouldReturnToken() throws Exception {

        // registers the user
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);
        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);
        request.exchange().block().bodyToMono(String.class).block();


        map.clear();
        map.add("grant_type", "password");
        map.add("scope", Constants.HERMEEZ_MOBILE_APP_SCOPE);
        map.add("password", password);
        map.add("username", email);
        String oauthTokenUrl = "http://localhost:8080/oauth/token";
        String encodedClientData = Base64Utils.encodeToString((Constants.JWT_CLIENT_ID + ":" + Constants.JWT_CLIENT_SECRET).getBytes());
        request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(oauthTokenUrl)
                .header("Authorization", "Basic " + encodedClientData)
                .body(BodyInserters.fromFormData(map));

        String response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(true, root.has("access_token"));
        assertEquals(true, root.has("refresh_token"));
        assertEquals(true, root.has("scope"));
        assertEquals(true, root.has("expires_in"));
    }

    @Test
    public void accessOAuthTokenUrlWithBadClientCredentialsShouldReturnInvalidCredentials() throws Exception {

        // registers the user
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);
        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);
        request.exchange().block().bodyToMono(String.class).block();


        map.clear();
        map.add("grant_type", "password");
        map.add("scope", Constants.HERMEEZ_MOBILE_APP_SCOPE);
        map.add("password", password + "makesItWrong");
        map.add("username", email);
        String oauthTokenUrl = "http://localhost:8080/oauth/token";
        String encodedClientData = Base64Utils.encodeToString((Constants.JWT_CLIENT_ID + ":" + Constants.JWT_CLIENT_SECRET).getBytes());
        request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(oauthTokenUrl)
                .header("Authorization", "Basic " + encodedClientData)
                .body(BodyInserters.fromFormData(map));

        String response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(false, root.has("access_token"));
    }

    @Test
    public void accessBaseUrlWithBadTokenShouldReturnInvalidToken() throws Exception {

        // registers the user
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);
        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);
        request.exchange().block().bodyToMono(String.class).block();

        // makes the request with a bad token
        String accessToken = Base64Utils.encodeToString(("badOAuthToken").getBytes());
        String baseUrl = "http://localhost:8080/" + Constants.API_NAME + "/";
        map.clear();
        map.add("email", email);
        map.add("password", password);
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(baseUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .body(inserter);

        String response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(true, root.has("error"));
        assertEquals("invalid_token", root.path("error").asText());
    }

    @Test
    public void accessBaseUrlWithGoodTokenShouldReturnWebPage() throws Exception {

        // registers the user
        String registerUserUrl = "http://localhost:8080/" + Constants.API_NAME + "/register";
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("email", email);
        map.add("password", password);
        BodyInserters.MultipartInserter inserter = BodyInserters.fromMultipartData(map);
        WebClient.RequestHeadersSpec<?> request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(registerUserUrl)
                .body(inserter);
        request.exchange().block().bodyToMono(String.class).block();

        // gets the token
        map.clear();
        map.add("grant_type", "password");
        map.add("scope", Constants.HERMEEZ_MOBILE_APP_SCOPE);
        map.add("password", password);
        map.add("username", email);
        String oauthTokenUrl = "http://localhost:8080/oauth/token";
        String encodedClientData = Base64Utils.encodeToString((Constants.JWT_CLIENT_ID + ":" + Constants.JWT_CLIENT_SECRET).getBytes());
        request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(oauthTokenUrl)
                .header("Authorization", "Basic " + encodedClientData)
                .body(BodyInserters.fromFormData(map));

        String response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        JsonNode root = objectMapper.readTree(response);
        assertEquals(true, root.has("access_token"));

        // makes the request with the valid token
        String accessToken = root.path("access_token").asText();
        String baseUrl = "http://localhost:8080/" + Constants.API_NAME + "/";
        map.clear();
        map.add("email", email);
        map.add("password", password);
        inserter = BodyInserters.fromMultipartData(map);

        request = WebClient.create()
                .method(HttpMethod.GET)
                .uri(baseUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .body(inserter);

        response = request.exchange().block().bodyToMono(String.class).block();
        assertNotNull(response);
        root = objectMapper.readTree(response);
        assertEquals(true, root.has("_links"));
    }

}
