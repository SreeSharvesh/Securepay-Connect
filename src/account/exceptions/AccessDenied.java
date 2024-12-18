package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Access Denied!")
public class AccessDenied extends RuntimeException {
    public AccessDenied() {
    }

    public AccessDenied(String message) {
        super(message);
    }

    public AccessDenied(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDenied(Throwable cause) {
        super(cause);
    }

    public AccessDenied(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
