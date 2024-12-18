package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Role not found!")
public class RoleNotFound extends RuntimeException{

    public RoleNotFound() {
    }

    public RoleNotFound(String message) {
        super(message);
    }

    public RoleNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFound(Throwable cause) {
        super(cause);
    }

    public RoleNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
