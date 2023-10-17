package com.fawry.couponapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CouponException extends RuntimeException {
    private final HttpStatus statusCode;
    public CouponException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
