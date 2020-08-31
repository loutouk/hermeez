package boursier.louis.hermeez.backend.apierror.signinerror;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super("Authentication failed. Wrong credentials.");
    }
}