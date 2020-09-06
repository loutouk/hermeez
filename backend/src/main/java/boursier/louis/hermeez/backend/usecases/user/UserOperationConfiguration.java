package boursier.louis.hermeez.backend.usecases.user;

import boursier.louis.hermeez.backend.entities.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Defines which implementation of the UserOperations is utilized.
 * See {@link User}.
 * <p>
 * <p>
 * See {@link UserOperations}.
 */
@Configuration
public class UserOperationConfiguration {
    @Bean
    public UserOperations userOperations() {
        return new MongoUserOperations();
    }
}
