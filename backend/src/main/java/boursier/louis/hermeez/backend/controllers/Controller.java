package boursier.louis.hermeez.backend.controllers;

import boursier.louis.hermeez.backend.entities.User;
import boursier.louis.hermeez.backend.usecases.UserOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Matches specified urls to manually defined functions.
 * Other auto generated URLs are available. See {@link UserRepository}.
 * Exceptions are handled separately. See {@link boursier.louis.hermeez.backend.apierror.CustomRestExceptionHandler}.
 */


@RestController
@RequestMapping(Constants.API_NAME)
@Validated
public class Controller {

    private static final Logger logger = LogManager.getLogger(Controller.class);
    @Autowired
    private UserOperations userOperations;

    @PostMapping("/updateemail")
    @PreAuthorize("authentication.principal == #email")
    User updateEmail(@RequestParam(value = "email") String email, @RequestParam(value = "newEmail") String newEmail) {
        // TODO fix
        logger.info("update email call");
        return userOperations.updateEmail(email, newEmail);
    }

    @PostMapping("/updatepassword")
    User updatePassword(@RequestParam String email, @RequestParam String newPassword) {
        logger.info("update password call");
        return userOperations.updatePassword(email, newPassword);
    }

    @PostMapping("/updatetopremium")
    @PreAuthorize("hasAuthority('USER')")
    User updateToPremium(@RequestParam(value = "email") String email) {
        logger.info("update to premium call");
        return userOperations.updateToPremium(email);
    }

    @PostMapping("/signin")
    User signIn(@RequestParam @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH, max = Constants.EMAIL_MAX_LENGTH) String email,
                @RequestParam @NotBlank @Size(min = Constants.PASSWD_MIN_LENGTH, max = Constants.PASSWD_MAX_LENGTH) String password) {
        logger.info("sign in call");
        return userOperations.signIn(email, password);
    }

    @PostMapping("/register")
    User register(@RequestParam @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH, max = Constants.EMAIL_MAX_LENGTH) String email,
                  @RequestParam @NotBlank @Size(min = Constants.PASSWD_MIN_LENGTH, max = Constants.PASSWD_MAX_LENGTH) String password) {
        logger.info("registration call");
        return userOperations.register(email, password);
    }
}
