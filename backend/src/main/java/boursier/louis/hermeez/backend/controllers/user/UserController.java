package boursier.louis.hermeez.backend.controllers.user;

import boursier.louis.hermeez.backend.entities.user.User;
import boursier.louis.hermeez.backend.entities.user.UserDTO;
import boursier.louis.hermeez.backend.security.UserDetailsCustom;
import boursier.louis.hermeez.backend.usecases.user.UserOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserOperations userOperations;

    @PostMapping("/updateemail")
    UserDTO updateEmail(@RequestParam(value = "newEmail") @NotBlank @Size(min = Constants.EMAIL_MIN_LENGTH,
            max = Constants.EMAIL_MAX_LENGTH) String newEmail, OAuth2Authentication authentication) {
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getUserAuthentication().getPrincipal();
        String email = userDetailsCustom.getUsername();
        LOGGER.info("update email call (" + email + ")");
        User user = userOperations.updateEmail(email, newEmail);
        return new UserDTO(user);
    }

    @PostMapping("/updatepassword")
    UserDTO updatePassword(@RequestParam(value = "newPassword") @NotBlank @Size(min = Constants.PASSWD_MIN_LENGTH,
            max = Constants.PASSWD_MAX_LENGTH) String newPassword, OAuth2Authentication authentication) {
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getUserAuthentication().getPrincipal();
        String email = userDetailsCustom.getUsername();
        LOGGER.info("update password call (" + email + ")");
        User user = userOperations.updatePassword(email, newPassword);
        return new UserDTO(user);
    }

    @PostMapping("/updatetopremium")
    UserDTO updateToPremium(OAuth2Authentication authentication) {
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getUserAuthentication().getPrincipal();
        String email = userDetailsCustom.getUsername();
        LOGGER.info("update to premium call (" + email + ")");
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(OAuth2Authentication authentication) {
        LOGGER.info("logout call");
        return userOperations.logout(authentication);
    }
}
