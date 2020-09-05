package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.controllers.UserRepository;
import boursier.louis.hermeez.backend.entities.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

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
}
