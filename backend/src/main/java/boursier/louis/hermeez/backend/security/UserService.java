package boursier.louis.hermeez.backend.security;


import boursier.louis.hermeez.backend.controllers.UserRepository;
import boursier.louis.hermeez.backend.entities.User;
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
    private UserRepository userRepository;

    /**
     * We need to override this method to specify the way of retrieving the user.
     * Our application finds its users by their email address, so that will do as a username.
     * We retrieve the user object and wrap it in a custom class implementing UserDetails.
     * See {@link boursier.louis.hermeez.backend.security.UserDetailsCustom}.
     *
     * @param username is the email in this implementation
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserDetailsCustom(user);
    }
}