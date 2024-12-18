package account.exceptions;

public class UnauthorizedUser extends RuntimeException {

    public UnauthorizedUser() {
        super();
    }

    public UnauthorizedUser(String message) {
        super(message);
    }

    public UnauthorizedUser(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedUser(Throwable cause) {
        super(cause);
    }

    public UnauthorizedUser(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
