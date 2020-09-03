package boursier.louis.hermeez.backend.usecases;

import boursier.louis.hermeez.backend.apierror.registrationerror.EmailAlreadyTakenException;
import boursier.louis.hermeez.backend.apierror.signinerror.WrongCredentialsException;
import boursier.louis.hermeez.backend.controllers.UserRepository;
import boursier.louis.hermeez.backend.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MongoUserOperations implements UserOperations {

    private static final Logger LOGGER = LogManager.getLogger(MongoUserOperations.class);
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO implement one time authorization code delivered upon in app purchase specific for the email involved
    // TODO verify email match
    // TODO Username enumeration vulnerabilities

    /**
     * Username enumeration vulnerabilities should be dealt with.
     *
     * @param email
     */
    @Override
    public User updateToPremium(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to upgrade to premium but email was not found");
            throw new WrongCredentialsException();
        }
        user.setRole(User.Role.PREMIUM);
        LOGGER.info("User " + email + " upgraded to premium");
        return repository.save(user);
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * For password oversight, the most secure practice is to tell the user something along the lines of:
     * "If a valid e-mail address was entered, instructions to reset you password have been sent"
     * <p>
     * OWASP Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html
     *
     * @param email
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public User updatePassword(String email, String newPassword) {
        // TODO
        LOGGER.info("Password of user " + email + " updated");
        return null;
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
    public User updateEmail(String email, String newEmail) {
        // TODO
        LOGGER.info("Email of user " + email + " updated to " + newEmail);
        return null;
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
    public User signIn(String email, String password) {
        User user = repository.findByEmail(email);
        if (user == null) {
            LOGGER.error("User " + email + " tried to sign in but email was not found");
            throw new WrongCredentialsException();
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.error("User " + email + " tried to sign in but credentials were invalid");
            throw new WrongCredentialsException();
        } else {
            LOGGER.info("User " + email + " signed in");
            return user;
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
    public User register(String email, String password) {
        User user = repository.findByEmail(email);
        if (user != null) {
            throw new EmailAlreadyTakenException(email);
        } else {
            user = new User(email, passwordEncoder.encode(password));
            return repository.save(user);
        }
    }
}
