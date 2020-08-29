package boursier.louis.hermeez.backend.entities;

import boursier.louis.hermeez.backend.userconstraints.EmailValidConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Document
@Getter
@Setter
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    // TODO check database indexes and find out why the unique constraint does not work
    @Indexed(unique = true) // Creates an index on the email for faster queries as it is often used as an id
    @Valid
    @EmailValidConstraint
    private String email;
    @NotEmpty
    @JsonIgnore
    private String password;
    private Role role;
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    // TODO remove in prod
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {USER, PREMIUM}
}
