package boursier.louis.hermeez.backend.config;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * This class is used for Spring to register our validators before entities are saved in the database.
 * User fields to validate are defined in the User class {@link User}.
 * And their constraints can be found in the userconstraints package {@link boursier.louis.hermeez.backend.userconstraints}.
 */
@Configuration
public class ValidatorEventRegister {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}