package boursier.louis.hermeez.backend.usecases;

import boursier.louis.hermeez.backend.UserRepository;
import boursier.louis.hermeez.backend.apierror.ApiError;
import boursier.louis.hermeez.backend.apierror.CustomRestExceptionHandler;
import boursier.louis.hermeez.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MongoUserOperations implements UserOperations {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO implement one time authorization code delivered upon in app purchase specific for the email involved
    // TODO verify email match
    // TODO Username enumeration vulnerabilities

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * @param email
     */
    @Override
    public void updateToPremium(String email) {
        User user = repository.findByEmail(email);
        user.setRole(User.Role.PREMIUM);
        repository.save(user);
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * For password oversight, the most secure practice is to tell the user something along the lines of:
     * "If a valid e-mail address was entered, instructions to reset you password have been sent"
     *
     * OWASP Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html
     *
     * @param email
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void updatePassword(String email, String newPassword) {
        // TODO
    }

    /**
     * Username enumeration vulnerabilities should be dealt with.
     * For password oversight, the most secure practice is to tell the user something along the lines of:
     * "If a valid e-mail address was entered, instructions to reset you e-mail have been sent"
     *
     * OWASP Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Forgot_Password_Cheat_Sheet.html
     * @param newEmail
     */
    @Override
    public void updateEmail(String newEmail) {
        // TODO
    }

    /**
     * TODO This url is accessible to anyone and should be secured on client side (reCAPTCHA) and server side (IP tracking).
     *
     * Tries to authenticate a user already present in the database with the given credentials.
     * If the credentials are false, it does not identify which entry was invalid, and provides a generic answer
     * For security reasons: Prevent username enumeration vulnerabilities.
     * For logic reasons: Username and password only represent anything when used in combination.
     *
     * @param email
     * @param password
     * @return User object if authenticated, error message otherwise with 401 - Unauthorized.
     */
    @Override
    public User signIn(String email, String password) {
        User user = repository.findByEmail(email);
        if (user == null) { // user not found
            return null;
        } else if (!password.equals(user.getPassword())) { // password does not match the email
            return null;
        } else {
            return user;
        }
    }

    /**
     * TODO This url is accessible to anyone and should be secured on client side (reCAPTCHA) and server side (IP tracking).
     *
     * Registers a new User with the given credentials if the email is not already taken.
     * Username enumeration vulnerabilities should be dealt with (reCAPTCHA on client side and IP tracking on server side).
     *
     * @param email
     * @param password
     * @return User object if success, error message otherwise with 200 - OK and an "email already taken" message.
     */
    @Override
    public User register(String email, String password) {
        User user = repository.findByEmail(email);
        if (user == null) {
            User u = new User(null, null, email, passwordEncoder.encode(password));
            return repository.save(u);
        } else {
            return null;
        }
    }
}
