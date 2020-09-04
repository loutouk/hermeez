package boursier.louis.hermeez.backend.controllers;

import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String email = "test@example.com";
    private final String password = "password";
    private final String role = User.Role.USER.name();

    @BeforeEach
    public void deleteUser() {
        userRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        //  simple sanity check test that will fail if the application context cannot start
    }

    @Test
    public void registerShouldReturnUser() throws Exception {
        String registerUserUrl = "http://localhost:" + port + "/" + Constants.API_NAME + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(registerUserUrl, request, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        assertNotNull(response.getBody());
        assertEquals(email, root.path("email").asText());
    }

    @Test
    public void registerShouldGrantBasicUserRole() throws Exception {
        String registerUserUrl = "http://localhost:" + port + "/" + Constants.API_NAME + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(registerUserUrl, request, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        assertNotNull(response.getBody());
        assertEquals(role, root.path("role").asText());
    }
}