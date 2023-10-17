package com.fawry.couponapi.service.coupon;

import com.fawry.couponapi.model.dto.*;

import java.util.List;

public interface CouponService {

    ReturnedCouponDTO create(RequestedCouponDTO coupon);

    ReturnedCouponDTO get(String code);

    List<ReturnedCouponDTO> getAll();

    List<ReturnedCouponDTO> getAll(Boolean isActive);

    ReturnedCouponDTO deactivate(String code);

    ReturnedCouponDTO activate(String code);

    void checkCouponCode(ValidationRequestDto validationRequestDto);

    ConsumptionDTO consume(OrderRequestDTO orderRequestDTO);

    String generateCouponCode();

    DiscountDTO calculateDiscount(OrderRequestDTO orderRequestDTO);

}
