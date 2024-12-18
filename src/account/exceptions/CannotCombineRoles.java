package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user cannot combine administrative and business roles!")
public class CannotCombineRoles extends RuntimeException {

    public CannotCombineRoles() {
    }

    public CannotCombineRoles(String message) {
        super(message);
    }

    public CannotCombineRoles(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCombineRoles(Throwable cause) {
        super(cause);
    }

    public CannotCombineRoles(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
