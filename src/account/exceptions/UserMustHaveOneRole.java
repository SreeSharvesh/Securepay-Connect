package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user must have at least one role!")
public class UserMustHaveOneRole extends RuntimeException {

    public UserMustHaveOneRole() {
    }

    public UserMustHaveOneRole(String message) {
        super(message);
    }

    public UserMustHaveOneRole(String message, Throwable cause) {
        super(message, cause);
    }

    public UserMustHaveOneRole(Throwable cause) {
        super(cause);
    }

    public UserMustHaveOneRole(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
