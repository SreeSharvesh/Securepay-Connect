package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't lock the ADMINISTRATOR!")
public class CannotLockTheAdmin extends RuntimeException {

    public CannotLockTheAdmin() {
    }

    public CannotLockTheAdmin(String message) {
        super(message);
    }

    public CannotLockTheAdmin(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotLockTheAdmin(Throwable cause) {
        super(cause);
    }

    public CannotLockTheAdmin(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
