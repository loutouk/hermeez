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

    @Override
    public void updateToPremium(String email) {
        User user = repository.findByEmail(email);
        user.setRole(User.Role.PREMIUM);
        repository.save(user);
    }

    @Override
    public User signIn(String email, String password) {
        User user = repository.findByEmail(email);
        /**
         * TODO find a way to differentiate user not found from wrong password
         */
        if (user == null) {
            System.out.println("user not found");
            return null;
        } else if (password.equals(user.getPassword())) {
            System.out.println("wrong password");
            return null;
        } else {
            return user;
        }
    }

    @Override
    public User register(String email, String password) {
        User user = repository.findByEmail(email);
        if (user == null) {
            User u = new User(null, null, email, passwordEncoder.encode(password));
            return repository.save(u);
        } else {
            // TODO think about what to return when email taken
            System.out.println("email already taken");
            return null;
        }
    }
}
