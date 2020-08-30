package boursier.louis.hermeez.backend.usecases;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Describes which operations the User entity requires in addition to CRUD operations on the User
 * which are automatically generated with the MongoRepository interface.
 * See {@link boursier.louis.hermeez.backend.UserRepository}.
 *
 * Allows to abstract the chosen database system and the logic implementation from the business code.
 */
public interface UserOperations {
    void updateToPremium(String email);

    User signIn(String email, String password);
}
