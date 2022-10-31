package nl.rabo.accountinformation.exceptions.handlers;


import nl.rabo.accountinformation.exceptions.AccountNotFoundException;
import nl.rabo.accountinformation.exceptions.BalanceNotEnoughException;
import nl.rabo.accountinformation.exceptions.UserNotFoundException;
import nl.rabo.accountinformation.utils.JsonConverterUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Exception Handler helps to customize error code and the body for the response
 * based on different exception for common nl.rabo.accountinformation.services runtime exceptions
 */

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for {@link BalanceNotEnoughException}
     *
     * @param ex  the exception
     * @param req the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(BalanceNotEnoughException.class)
    private ResponseEntity<Object> handleMissingParameterException(RuntimeException ex, WebRequest req) {
        return new ResponseEntity<>(JsonConverterUtil.convertToJson(
                ErrorDetails.builder()
                        .timestamp(new Date())
                        .message(
                                ex.getMessage() != null && !ex.getMessage().isEmpty() ?
                                        ex.getMessage() : "Request Parameters are invalid"
                        )
                        .build()),
                new HttpHeaders(),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }


    /**
     * Customize the response for JsonConversionException.
     * <p>
     * This method logs a warning, sets the "Allow" header, and delegates to	 *
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<Object> handleDataIntegrityViolationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Data can not be deleted due to it has child items",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Violation> errors = new ArrayList<>();

        errors.addAll(ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage())).toList());

        errors.addAll(ex.getBindingResult().getGlobalErrors().stream()
                .map(fieldError -> new Violation(fieldError.getCode(), fieldError.getDefaultMessage())).toList());


        return new ResponseEntity<>(
                ErrorDetails.builder().timestamp(new Date()).message("Validation failed").violations(errors).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<Object> handleUserNotFoundException(RuntimeException ex, WebRequest req) {
        return new ResponseEntity<>(JsonConverterUtil
                .convertToJson(ErrorDetails.builder()
                        .timestamp(new Date())
                        .message(ex.getMessage() != null ? ex.getMessage() : "User not found with provided UUID")
                        .build()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    private ResponseEntity<Object> handleAccountNotFoundException(RuntimeException ex, WebRequest req) {
        return new ResponseEntity<>(JsonConverterUtil
                .convertToJson(ErrorDetails.builder()
                        .timestamp(new Date())
                        .message(ex.getMessage() != null ? ex.getMessage() : "Account not found with provided UUID")
                        .build()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }


}
