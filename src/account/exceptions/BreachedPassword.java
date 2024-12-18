package account.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The password is in the hacker's database!")
public class BreachedPassword extends RuntimeException {

    public BreachedPassword() {
    }

    public BreachedPassword(String message) {
        super(message);
    }

    public BreachedPassword(String message, Throwable cause) {
        super(message, cause);
    }

    public BreachedPassword(Throwable cause) {
        super(cause);
    }

    public BreachedPassword(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
