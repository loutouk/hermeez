package boursier.louis.hermeez.backend.utils;

public class Constants {

    public static final boolean DEBUG = true;

    // Base URL for API
    public static final String API_NAME = "hermeezapiv1";

    //////// OAuth2 token
    /////////////////////////////////////////////////////////////////

    // Our API is only to be used by one type of client: the Hermeez mobile app.
    // So only one pair of credential is required. Even though we do not even need one per se.
    // Receiving those credentials from an outside app is not a proof of it being the legitimate mobile app,
    // as anybody could decompile the mobile app and extract those credentials and use it for basic authentication.
    public static final String JWT_CLIENT_ID = "hermeezmobileapp";
    public static final String JWT_CLIENT_SECRET = "fR90Yf9RefTH20dRTZ0";
    // Short lived access token
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 43200;
    // Long lived refresh token
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 2592000;
    // Scopes are irrelevant because we only have one type of usage (the mobile app)
    // We set a dummy one as Spring requires it even though it is optional for OAuth
    public static final String HERMEEZ_MOBILE_APP_SCOPE = "all";

    //////// User entity validation constraint before persistence
    /////////////////////////////////////////////////////////////////

    // Because password is hashed by BCrypt the minimum length before persistence should never be a problem
    public static final int PASSWD_MIN_LENGTH = 6;
    // To give room for BCrypt SHA1 + salt size (around 60 in length)
    public static final int PASSWD_MAX_LENGTH = 100;
    public static final int EMAIL_MIN_LENGTH = 2;
    public static final int EMAIL_MAX_LENGTH = 36;

    //////// User entity
    /////////////////////////////////////////////////////////////////
    public static final int MAX_VALIDITY_PREMIUM_SECONDS = 365;

    //////// Scheduled tasks
    /////////////////////////////////////////////////////////////////
    public static final int UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS = 60000;

    private Constants() {
    }
}
