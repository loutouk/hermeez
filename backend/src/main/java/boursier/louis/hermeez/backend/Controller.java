package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import boursier.louis.hermeez.backend.usecases.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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

    @GetMapping("/forpremium")
    @PreAuthorize("hasAuthority('PREMIUM')")
    String forPremium(@RequestParam String email, OAuth2Authentication authentication) {
        return "For premium only.";
    }

    @GetMapping("/foruser")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('PREMIUM')")
    String forUser() {
        return "For all users.";
    }

    @GetMapping("/updateEmail")
    @PreAuthorize("authentication.principal == #email")
    String updateEmail() {
        // TODO
        return "todo update email";
    }

    @PostMapping("/updatetopremium")
    @PreAuthorize("hasAuthority('USER') || !hasAuthority('PREMIUM')")
    String updateToPremium(@RequestParam(value = "email") String email) {
        userOperations.updateToPremium(email);
        return "Role updated to premium.";
    }

    @GetMapping("/test")
    String test(OAuth2Authentication authentication) {
        System.out.println(authentication.getPrincipal().toString());
        return "Test.";
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
