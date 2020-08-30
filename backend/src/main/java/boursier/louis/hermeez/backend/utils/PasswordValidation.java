package boursier.louis.hermeez.backend.utils;

public class PasswordValidation {
    public static boolean isValidPassword(String password) {
        System.out.println(password);
        return !password.isEmpty() &&
                password.length() >= Constants.PASSWD_MIN_LENGTH &&
                password.length() <= Constants.PASSWD_MAX_LENGTH;
    }
}
