package boursier.louis.hermeez.backend.entities.user;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {

    public final String email;
    public final User.Role role;
    public final String premiumExpirationDate;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.role = user.getRole();
        if(user.getPremiumExpirationDate() != null) {
            this.premiumExpirationDate =
                    user.getPremiumExpirationDate().getYear() + "/" +
                    user.getPremiumExpirationDate().getDayOfMonth() + "/" +
                    user.getPremiumExpirationDate().getMonthOfYear();
        } else {
            this.premiumExpirationDate = null;
        }

    }

}
