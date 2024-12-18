package account.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "Bad Request";
        String message = "Password length must be 12 chars minimum!";

        //Removing 'uri=' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);

//        // Custom error messages based on validation errors
//        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
//                .map(fieldError -> {
//                    if (fieldError.getField().equals("password")){
//                        //Custom error message for password validation
//                        if (fieldError.getRejectedValue().toString().length() < 12) {
//                            return "Password length must be 12 chars minimum!";
//                        }
//                    }
//                    return fieldError.getDefaultMessage();
//                })
//                .collect(Collectors.toList());

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException() {
        // Create a custom error response
        CustomErrorMessage errorResponse = new CustomErrorMessage(
                LocalDateTime.now(),  // timestamp
                HttpStatus.UNAUTHORIZED.value(),  // status
                "Unauthorized",  // error
                "User account is locked",  // custom message
                "cock"  // original message
                // ... other fields if needed
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<CustomErrorMessage> handleUserExists(UserExistException e, WebRequest request) {
        String error = "Bad Request";
        String message = "User exist!";

        //Removing 'uri=' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUser.class)
    public ResponseEntity<CustomErrorMessage> handleUnauthorized(WebRequest request) {
        String error = "Unauthorized";
        String message = "";

        //Removing 'uri=' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(BreachedPassword.class)
    public ResponseEntity<CustomErrorMessage> handlePasswordNotLongEnough(WebRequest request) {
        String error = "Bad Request";
        String message = "The password is in the hacker's database!";

        //Removing 'uri=' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SamePassword.class)
    public ResponseEntity<CustomErrorMessage> handleSamePassword(WebRequest request) {
        String error = "Bad Request";
        String message = "The passwords must be different!";

        //Removing 'uri=' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<CustomErrorMessage> badRequest(WebRequest request) {
        String error = "Bad Request";
        String message = "Error!";

        //Removing 'uri"' from the path
        String path = request.getDescription(false);
        path = path.replace("uri=", "");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class, org.hibernate.exception.ConstraintViolationException.class})
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
