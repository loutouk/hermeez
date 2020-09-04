package boursier.louis.hermeez.backend.entities.user;

import java.io.Serializable;

public class UserDTO implements Serializable {

    public String email;
    public User.Role role;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.role = user.getRole();
    }


}
