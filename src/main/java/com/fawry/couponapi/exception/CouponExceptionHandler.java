package com.fawry.couponapi.exception;

import com.fawry.couponapi.error.GeneralError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;


@ControllerAdvice
public class CouponExceptionHandler {

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<GeneralError> handleCouponException(CouponException ex) {
        GeneralError generalError= GeneralError.generateGeneralError(ex.getStatusCode().value(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(generalError);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody GeneralError handleConstraintViolationException(ConstraintViolationException e) {
        String message = ((ConstraintViolation<?>) e.getConstraintViolations().toArray()[0]).getMessage();
        return GeneralError.generateGeneralError(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody GeneralError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return GeneralError.generateGeneralError(HttpStatus.BAD_REQUEST.value(), message);
    }
}
