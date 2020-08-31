package boursier.louis.hermeez.backend.apierror.registrationerror;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("email already taken: " + email);
    }
}