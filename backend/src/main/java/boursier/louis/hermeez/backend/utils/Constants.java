package boursier.louis.hermeez.backend.utils;

public class Constants {
    public static final boolean DEBUG = true;
    public static final int PASSWD_MIN_LENGTH = 6;
    public static final int PASSWD_MAX_LENGTH = 100; // To give room for BCrypt SHA1 + salt size
    public static final int EMAIL_MIN_LENGTH = 2;
    public static final int EMAIL_MAX_LENGTH = 36;
}
