package boursier.louis.hermeez.backend.utils;

public class PasswordValidation {
    public static boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= Constants.PASSWD_MIN_LENGTH &&
                password.length() <= Constants.PASSWD_MAX_LENGTH;
    }
}
