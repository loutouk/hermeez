package boursier.louis.hermeez.backend.usecases.user;

import boursier.louis.hermeez.backend.apierror.registrationerror.EmailAlreadyTakenException;
import boursier.louis.hermeez.backend.apierror.signinerror.WrongCredentialsException;
import boursier.louis.hermeez.backend.controllers.user.UserRepository;
import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.entities.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.Calendar;
import java.util.Date;

public class MongoUserOperations implements UserOperations {

    private static final Logger LOGGER = LogManager.getLogger(MongoUserOperations.class);
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DefaultTokenServices tokenServices;

    // TODO implement one time authorization code delivered upon in app purchase specific for the email involved
    // TODO verify email match
    // TODO Username enumeration vulnerabilities

    /**
     * Username enumeration vulnerabilities should be dealt with.
     *
     * @param email
     */
    @Override
    public ResponseEntity<UserDTO> updateToPremium(String email, int durationInDays) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to upgrade to premium but email was not found");
            throw new WrongCredentialsException();
        }
        user.setRole(User.Role.PREMIUM);
        user.setPremiumExpirationDate(DateTime.now().plusDays(durationInDays));
        LOGGER.info("User " + email + " upgraded to premium");
        return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     *
     * @param email
     */
    @Override
    public ResponseEntity<UserDTO> updateToUser(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to upgrade to user but email was not found");
            throw new WrongCredentialsException();
        }
        user.setRole(User.Role.USER);
        LOGGER.info("User " + email + " upgraded to user");
        return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * For password oversight, the most secure practice is to tell the user something along the lines of:
     * "If a valid e-mail address was entered, instructions to reset you password have been sent"
     * <p>
     * OWASP Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html
     *
     * @param email
     * @param newPassword
     */
    @Override
    public ResponseEntity<UserDTO> updatePassword(String email, String newPassword) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to update password but email was not found");
            throw new WrongCredentialsException();
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            user = repository.save(user);
            LOGGER.info("User " + email + " changed its password");
            return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
        }
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * For password oversight, the most secure practice is to tell the user something along the lines of:
     * "If a valid e-mail address was entered, instructions to reset you e-mail have been sent"
     * <p>
     * OWASP Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html
     *
     * @param newEmail
     */
    @Override
    public ResponseEntity<UserDTO> updateEmail(String email, String newEmail) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to update email but email was not found");
            throw new WrongCredentialsException();
        } else {
            user.setEmail(newEmail);
            user = repository.save(user);
            LOGGER.info("User " + email + " changed its email with " + newEmail);
            return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
        }
    }

    /**
     * TODO This url is accessible to anyone and should be secured on client side (reCAPTCHA) and server side (IP tracking).
     * <p>
     * Tries to authenticate a user already present in the database with the given credentials.
     * If the credentials are false, it does not identify which entry was invalid, and provides a generic answer
     * For security reasons: Prevent username enumeration vulnerabilities.
     * For logic reasons: Username and password only represent anything when used in combination.
     *
     * @param email
     * @param password
     * @return User object if authenticated, error message.
     */
    @Override
    public ResponseEntity<UserDTO> signIn(String email, String password) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to sign in but credentials were invalid");
            throw new WrongCredentialsException();
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.error("User " + email + " tried to sign in but credentials were invalid");
            throw new WrongCredentialsException();
        } else {
            LOGGER.info("User " + email + " signed in");
            return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
        }
    }

    /**
     * TODO This url is accessible to anyone and should be secured on client side (reCAPTCHA) and server side (IP tracking).
     * <p>
     * Registers a new User with the given credentials if the email is not already taken.
     * Username enumeration vulnerabilities should be dealt with (reCAPTCHA on client side and IP tracking on server side).
     *
     * @param email
     * @param password
     * @return User object if success, error message otherwise.
     */
    @Override
    public ResponseEntity<UserDTO> register(String email, String password) {
        User user = repository.findByEmail(email);
        if (user != null) {
            LOGGER.error("User " + email + " tried to register but email was already taken");
            throw new EmailAlreadyTakenException(email);
        } else {
            user = new User(email, passwordEncoder.encode(password));
            user = repository.save(user);
            LOGGER.info("User " + email + " registered");
            return new ResponseEntity<>(new UserDTO(repository.save(user)), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> logout(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        // removes access token and refresh token if presents
        if (!tokenServices.revokeToken(accessToken)) {
            LOGGER.error("OAuth2AccessToken not found for logging out (" + accessToken + ")");
            return new ResponseEntity<>("OAuth2AccessToken not found", HttpStatus.UNAUTHORIZED);
        }
        LOGGER.info("User logged out");
        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }
}
