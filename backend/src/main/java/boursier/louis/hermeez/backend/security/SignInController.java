package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.entities.User;
import boursier.louis.hermeez.backend.usecases.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
public class SignInController {

    @Autowired
    private UserOperations userOperations;

    @PostMapping
    User signIn(@RequestParam String email, @RequestParam String password) {
        return userOperations.signIn(email, password);
    }
}