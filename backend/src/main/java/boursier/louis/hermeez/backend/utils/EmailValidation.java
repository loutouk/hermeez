package boursier.louis.hermeez.backend.utils;

public class EmailValidation {
    public static boolean isValidEmail(String email) {
        return !email.isEmpty() &&
                email.length() >= Constants.EMAIL_MIN_LENGTH &&
                email.length() <= Constants.EMAIL_MAX_LENGTH &&
                email.contains("@");
    }
}
