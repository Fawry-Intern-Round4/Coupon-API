package com.fawry.couponapi.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessages {
    COUPON_NOT_FOUND("Coupon not found"),
    COUPON_NOT_ACTIVE("Coupon is not active currently"),
    COUPON_USED_BY_CUSTOMER("Coupon is already used by this customer"),
    COUPON_EXPIRED("Coupon has expired"),
    COUPON_FULLY_REDEEMED("Coupon is fully redeemed"),
    NO_CONSUMPTIONS_FOR_ORDER("There's no consumption for this order");

    private final String message;
}
