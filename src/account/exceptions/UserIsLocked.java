package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User account is locked")
public class UserIsLocked extends DisabledException {

    public UserIsLocked(String msg) {
        super(msg);
    }

    public UserIsLocked(String msg, Throwable cause) {
        super(msg, cause);
    }
}

