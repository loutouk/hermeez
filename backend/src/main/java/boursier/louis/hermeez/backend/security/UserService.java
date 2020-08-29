package boursier.louis.hermeez.backend.security;


import boursier.louis.hermeez.backend.usecases.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class defines how clients wanting a token and providing their credential are verified against the database.
 * See {@link boursier.louis.hermeez.backend.security.OAuthConfiguration}.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserOperations userOperations;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userOperations.loadUserByUsername(username);
    }
}