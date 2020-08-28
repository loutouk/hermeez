package boursier.louis.hermeez.backend.entities;

import boursier.louis.hermeez.backend.userconstraints.EmailUniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class User {

    public enum Role {USER, PREMIUM}

    @Id
    private String id;
    private String firstName;
    private String lastName;

    @Indexed(unique = true) // Creates an index on the email for faster queries as it is often used as an id
    @Valid
    @EmailUniqueConstraint
    private String email;
    @NotEmpty
    private String password;
    private Role role;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }
}
