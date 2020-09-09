package boursier.louis.hermeez.backend.usecases.user;

import boursier.louis.hermeez.backend.controllers.user.UserRepository;
import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.entities.user.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Describes which operations the User entity requires in addition to CRUD operations on the User
 * which are automatically generated with the MongoRepository interface.
 * See {@link UserRepository}.
 * <p>
 * Allows to abstract the chosen database system and the logic implementation from the business code.
 */
public interface UserOperations {
    ResponseEntity<UserDTO> updateToPremium(String email, int durationInDays);

    ResponseEntity<UserDTO> updateToUser(String email);

    ResponseEntity<UserDTO> updatePassword(String email, String newPassword);

    ResponseEntity<UserDTO> updateEmail(String email, String newEmail);

    ResponseEntity<UserDTO> signIn(String email, String password);

    ResponseEntity<UserDTO> register(String email, String password);

    ResponseEntity<String> logout(OAuth2Authentication authentication);


}
