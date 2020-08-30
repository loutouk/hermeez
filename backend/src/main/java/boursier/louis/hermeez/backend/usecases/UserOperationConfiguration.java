package boursier.louis.hermeez.backend.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Defines which implementation of the UserOperations is utilized.
 * See {@link boursier.louis.hermeez.backend.entities.User}.
 * <p>
 * <p>
 * See {@link boursier.louis.hermeez.backend.usecases.UserOperations}.
 */
@Configuration
public class UserOperationConfiguration {
    @Bean
    public UserOperations userOperations() {
        return new MongoUserOperations();
    }
}
