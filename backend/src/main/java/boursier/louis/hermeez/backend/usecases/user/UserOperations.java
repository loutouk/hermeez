package boursier.louis.hermeez.backend.usecases.user;

import boursier.louis.hermeez.backend.controllers.user.UserRepository;
import boursier.louis.hermeez.backend.entities.user.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Describes which operations the User entity requires in addition to CRUD operations on the User
 * which are automatically generated with the MongoRepository interface.
 * See {@link UserRepository}.
 * <p>
 * Allows to abstract the chosen database system and the logic implementation from the business code.
 */
public interface UserOperations {
    User updateToPremium(String email);

    User updatePassword(String email, String newPassword);

    User updateEmail(String email, String newEmail);

    User signIn(String email, String password);

    User register(String email, String password);

    ResponseEntity<String> logout(HttpServletRequest request);


}
