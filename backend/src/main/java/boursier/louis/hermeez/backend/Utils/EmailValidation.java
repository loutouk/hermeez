package boursier.louis.hermeez.backend.Utils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidation {
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
