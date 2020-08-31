package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.apierror.registrationerror.EmailAlreadyTakenException;
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
 * Exceptions are handled separately. See {@link boursier.louis.hermeez.backend.apierror.CustomRestExceptionHandler}.
 */

@RestController
public class Controller {

    @Autowired
    private UserOperations userOperations;

    @PostMapping("/updateemail")
    @PreAuthorize("authentication.principal == #email")
    User updateEmail(@RequestParam String newEmail) {
        // TODO fix
        return userOperations.updateEmail(newEmail);
    }

    @PostMapping("/updatepassword")
    @PreAuthorize("authentication.principal == #email")
    User updatePassword(@RequestParam String email, @RequestParam  String newPassword) {
        return userOperations.updatePassword(email, newPassword);
    }

    @PostMapping("/updatetopremium")
    @PreAuthorize("authentication.principal == #email && (hasAuthority('USER') || !hasAuthority('PREMIUM'))")
    User updateToPremium(@RequestParam(value = "email") String email) {
        return userOperations.updateToPremium(email);
    }

    @PostMapping("/signin")
    User signIn(@RequestParam String email, @RequestParam String password) {
        return userOperations.signIn(email, password);
    }

    @PostMapping("/register")
    User register(@RequestParam String email, @RequestParam String password) {
        return userOperations.register(email, password);
    }
}
