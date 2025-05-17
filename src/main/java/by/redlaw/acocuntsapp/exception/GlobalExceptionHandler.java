package by.redlaw.acocuntsapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(HttpServletRequest req, ResponseStatusException ex) {
        return build(getApiError(ex, (HttpStatus) ex.getStatusCode(), req));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(HttpServletRequest req, HttpServletResponse res,
                                                            IllegalArgumentException ex) {
        return build(getApiError(ex, HttpStatus.BAD_REQUEST, req));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(HttpServletRequest req, MethodArgumentNotValidException ex) {
        ApiError error = getApiError(ex, HttpStatus.BAD_REQUEST, req);
        error.setMessage("Validation failed");
        error.setErrors(ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .toList());

        return build(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnhandled(HttpServletRequest req, Exception ex) {
        return build(getApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, req));
    }

    private ApiError getApiError(Exception ex, HttpStatus status, HttpServletRequest req) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status)
                .message(ex.getMessage())
                .path(req.getServletPath())
                .build();
    }

    private ResponseEntity<Object> build(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getError());
    }
}