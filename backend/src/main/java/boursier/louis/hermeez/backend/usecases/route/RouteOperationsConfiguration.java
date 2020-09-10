package boursier.louis.hermeez.backend.usecases.route;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Defines which implementation of the RouteOperations is utilized.
 * See {@link boursier.louis.hermeez.backend.entities.route.Route}.
 * <p>
 * <p>
 * See {@link RouteOperations}.
 */
@Configuration
public class RouteOperationsConfiguration {
    @Bean
    public RouteOperations routeOperations() {
        return new PersonalServerRouteOperations();
    }
}