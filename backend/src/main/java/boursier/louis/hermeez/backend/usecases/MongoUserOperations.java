package boursier.louis.hermeez.backend.usecases;

import boursier.louis.hermeez.backend.UserRepository;
import boursier.louis.hermeez.backend.entities.User;
import boursier.louis.hermeez.backend.security.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

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
        if (user == null) {
            User u = new User(null, null, email, passwordEncoder.encode(password));
            return repository.save(u);
        } else {
            // TODO return error with email taken
            System.out.println("email already taken");
            return null;
        }
    }
}
