package boursier.louis.hermeez.backend.controllers;

import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.entities.user.UserDTO;
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

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    @Autowired
    private UserOperations userOperations;

    @PostMapping("/updateemail")
    @PreAuthorize("authentication.principal == #email")
    UserDTO updateEmail(@RequestParam(value = "email") @NotBlank String email,
                        @RequestParam(value = "newEmail")
                        @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH, max = Constants.EMAIL_MAX_LENGTH) String newEmail) {
        LOGGER.info("update email call");
        User user = userOperations.updateEmail(email, newEmail);
        return new UserDTO(user);
    }

    @PostMapping("/updatepassword")
    @PreAuthorize("authentication.principal == #email")
    UserDTO updatePassword(@RequestParam(value = "email") @NotBlank String email,
                           @RequestParam(value = "newPassword") @NotBlank
                           @Size(min = Constants.PASSWD_MIN_LENGTH, max = Constants.PASSWD_MAX_LENGTH) String newPassword) {
        LOGGER.info("update password call");
        User user = userOperations.updatePassword(email, newPassword);
        return new UserDTO(user);
    }

    @PostMapping("/updatetopremium")
    @PreAuthorize("hasAuthority('USER') && authentication.principal == #email")
    UserDTO updateToPremium(@RequestParam(value = "email") @NotBlank String email) {
        LOGGER.info("update to premium call");
        User user = userOperations.updateToPremium(email);
        return new UserDTO(user);
    }

    @PostMapping("/signin")
    UserDTO signIn(@RequestParam @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH, max = Constants.EMAIL_MAX_LENGTH) String email,
                   @RequestParam @NotBlank @Size(min = Constants.PASSWD_MIN_LENGTH, max = Constants.PASSWD_MAX_LENGTH) String password) {
        LOGGER.info("sign in call");
        User user = userOperations.signIn(email, password);
        return new UserDTO(user);
    }

    @PostMapping("/register")
    UserDTO register(@RequestParam @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH, max = Constants.EMAIL_MAX_LENGTH) String email,
                     @RequestParam @NotBlank @Size(min = Constants.PASSWD_MIN_LENGTH, max = Constants.PASSWD_MAX_LENGTH) String password) {
        LOGGER.info("registration call");
        User user = userOperations.register(email, password);
        return new UserDTO(user);
    }
}
