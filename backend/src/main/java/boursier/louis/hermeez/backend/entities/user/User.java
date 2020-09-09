package boursier.louis.hermeez.backend.entities.user;

import boursier.louis.hermeez.backend.entities.user.userconstraints.EmailValidConstraint;
import boursier.louis.hermeez.backend.entities.user.userconstraints.PasswordValidConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

// TODO USER DTO (
//  Never expose the Entities directly to the endpoint instead its better to have a wrapper
//  i.e DTO that translates your entity to the required response.)

@Document
@Getter
@Setter
public class User {

    @Id
    private String id;
    // TODO check database indexes and find out why the unique constraint does not work
    @Valid
    @NotEmpty
    // Creates an index on the email for faster queries as it is often used as an id
    @Indexed(unique = true)
    @EmailValidConstraint
    private String email;
    @Valid
    @NotEmpty
    @PasswordValidConstraint
    // Fall back security measure. Prevents from accidentally (DTO role) returning the password (security reasons)
    // but allows for User creation (registration)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role role;
    private DateTime premiumExpirationDate;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.premiumExpirationDate = null;
    }

    public enum Role {USER, PREMIUM, OMNISCIENT}
}
