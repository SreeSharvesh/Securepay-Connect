package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user does not have a role!")
public class UserRoleDoesntExist extends RuntimeException {

    public UserRoleDoesntExist() {
    }

    public UserRoleDoesntExist(String message) {
        super(message);
    }

    public UserRoleDoesntExist(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRoleDoesntExist(Throwable cause) {
        super(cause);
    }

    public UserRoleDoesntExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
