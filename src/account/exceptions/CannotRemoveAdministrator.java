package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't remove ADMINISTRATOR role!")
public class CannotRemoveAdministrator extends RuntimeException {

    public CannotRemoveAdministrator() {
    }

    public CannotRemoveAdministrator(String message) {
        super(message);
    }

    public CannotRemoveAdministrator(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotRemoveAdministrator(Throwable cause) {
        super(cause);
    }

    public CannotRemoveAdministrator(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
