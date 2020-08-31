package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import boursier.louis.hermeez.backend.usecases.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Matches specified urls to manually defined functions.
 * Other auto generated URLs are available. See {@link boursier.louis.hermeez.backend.UserRepository}.
 */

@RestController
public class Controller {

    @Autowired
    private UserOperations userOperations;

    @PostMapping("/updateEmail")
    @PreAuthorize("authentication.principal == #email")
    void updateEmail(@RequestParam String newEmail) {
        userOperations.updateEmail(newEmail);
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("authentication.principal == #email")
    void updatePassword(@RequestParam String email, @RequestParam  String newPassword) {
        userOperations.updatePassword(email, newPassword);
    }

    @PostMapping("/updatetopremium")
    @PreAuthorize("authentication.principal == #email && (hasAuthority('USER') || !hasAuthority('PREMIUM'))")
    void updateToPremium(@RequestParam(value = "email") String email) {
        userOperations.updateToPremium(email);
    }

    @PostMapping("/signin")
    User signIn(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        User user = userOperations.signIn(email, password);
        if(user == null) {
            response.getWriter().println("wrong credentials");
        }
        return user;
    }

    @PostMapping("/register")
    User register(@RequestParam String email, @RequestParam String password) {
        return userOperations.register(email, password);
    }
}
